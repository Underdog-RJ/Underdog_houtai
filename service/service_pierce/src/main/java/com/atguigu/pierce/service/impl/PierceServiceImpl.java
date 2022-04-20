package com.atguigu.pierce.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.pierce.entity.Client;
import com.atguigu.pierce.entity.ClientMQ;
import com.atguigu.pierce.mapper.ClientMapper;
import com.atguigu.pierce.netty.metrics.Metrics;
import com.atguigu.pierce.service.PierceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PierceServiceImpl extends ServiceImpl<ClientMapper, Client> implements PierceService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private final static Integer MAX_PORT = 12100;
    private final static String PORT_KEY = "nxt_key";
    private final static String METRICS_PREFIX = "metrics:collect";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ClientMapper clientMapper;


    @Override
    public R hasPort() {
        int nxtPort = Integer.parseInt(redisTemplate.opsForValue().get(PORT_KEY));
        if (nxtPort < MAX_PORT) {
            redisTemplate.opsForValue().set(PORT_KEY, String.valueOf(nxtPort + 1));
            return R.ok().data("flag", true).data("nxt_key", nxtPort + 1);
        } else {
            return R.ok().data("flag", false);
        }
    }

    @Override
    public R createTunnel(Client client, HttpServletRequest httpServletRequest) {
//        String token = httpServletRequest.getHeader("token");
//        String userId = JwtUtils.getMemberIdFromToken(token);
        String userId = "1400099616044650497";
        /**1.判读当前登录的用户是否可以创建新的端口
         *
         */
        // 1.1查询用户当前创建的隧道数量
        int hasUserCreateTunnelNum = baseMapper.selectList(new QueryWrapper<Client>().lambda().eq(Client::getUserId, userId)).size();
        //TODO 1.2用户可以创建的数量
        int userMaxTunnelNum = 100;
        if (hasUserCreateTunnelNum >= userMaxTunnelNum)
            return R.ok().message("您已经创建达到了您所能创建的最大数量隧道，您可以通过U币购买呦！");
        /**
         * 判断redis是否还有足够的公网端口数量
         */
        int nxtPort = Integer.parseInt(redisTemplate.opsForValue().get(PORT_KEY));
        if (nxtPort >= MAX_PORT)
            return R.ok().message("当前服务器的公用端口已经用完了，创建失败,您可以联系RJ创建多的公网端口");
        int inetPort = nxtPort + 1;
        // 更新redis的公网端口
        redisTemplate.opsForValue().set(PORT_KEY, String.valueOf(inetPort));
        client.setInetPort(inetPort);

        // 获取UUID
        String clientKey = UUID.randomUUID().toString().replace("-", "");
        client.setClientKey(clientKey);
        client.setUserId(userId);
        client.setStatus(0);
        clientMapper.insert(client);
        return R.ok().message("创建成功");
    }


    @Override
    public R getUserChannel(HttpServletRequest request, Integer page, Integer size) {
//        String token = request.getHeader("token");
//        String userId = JwtUtils.getMemberIdFromToken(token);
        Page<Client> pageInfo = new Page<>(page, size);
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Client::getUserId, "1400099616044650497");

        IPage<Client> clientIPage = clientMapper.selectPage(pageInfo, wrapper);
        List<Client> records = clientIPage.getRecords();
        long total = clientIPage.getTotal();


        return R.ok().data("list", records).data("total", total);
    }

    @Override
    public R changeClientKey(Client client) {
        Client targetClient = clientMapper.selectById(client.getId());
        String replace = UUID.randomUUID().toString().replace("-", "");
        ClientMQ clientMQ = new ClientMQ();
        clientMQ.setType(0);
        clientMQ.setClientKeyBefore(targetClient.getClientKey());
        clientMQ.setClientKeyNow(replace);
        clientMQ.setLanNow(targetClient.getLan());
        clientMQ.setLanBefore(targetClient.getLan());
        clientMQ.setInetPort(targetClient.getInetPort());
        // 判读当前的channel是否是激活状态，如果是直接关闭，
        rabbitTemplate.convertAndSend("pierce-event-exchange", "pierce.changeTarget", clientMQ);
        targetClient.setClientKey(replace);
        baseMapper.updateById(targetClient);
        return R.ok().data("client", targetClient);
    }

    @Override
    public R getUserMetrics(Integer page, Integer size, HttpServletRequest request) {
        Page<Client> pageInfo = new Page<>(page, size);
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Client::getUserId, "1400099616044650497");

        IPage<Client> clientIPage = clientMapper.selectPage(pageInfo, wrapper);
        long total = clientIPage.getTotal();
        List<Integer> collect = clientIPage.getRecords().stream().map(Client::getInetPort).collect(Collectors.toList());
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(METRICS_PREFIX);
        List<Metrics> results = new ArrayList<>();
        for (Integer integer : collect) {
            String portStr = String.valueOf(integer);
            if (ops.hasKey(portStr)) {
                String jsonStr = (String) ops.get(portStr);
                Metrics metrics = JSONObject.parseObject(jsonStr, Metrics.class);
                results.add(metrics);
            } else {
                Metrics metrics = new Metrics();
                metrics.setPort(integer);
                results.add(metrics);
            }
        }
        return R.ok().data("list", results).data("total", total);
    }

    @Override
    public R updateClient(Client client) {
        String lan = client.getLan();
        Client mapperClient = clientMapper.selectById(client.getId());
        String targetLan = mapperClient == null ? null : mapperClient.getLan();
        if (!Objects.equals(lan, targetLan)) {
            ClientMQ clientMQ = new ClientMQ();
            clientMQ.setClientKeyBefore(client.getClientKey());
            rabbitTemplate.convertAndSend("pierce-event-exchange", "pierce.changeTarget", clientMQ);
        }
        mapperClient.setName(client.getName());
        mapperClient.setLan(client.getLan());
        clientMapper.updateById(mapperClient);
        return R.ok().message("更新成功");
    }
}
