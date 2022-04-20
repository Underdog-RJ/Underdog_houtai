package com.atguigu.pierce.netty.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.pierce.entity.Client;
import com.atguigu.pierce.entity.ClientProxyMapping;
import com.atguigu.pierce.entity.ConfigChangedListener;
import com.atguigu.pierce.mapper.ClientMapper;
import com.atguigu.pierce.service.PierceService;
import com.atguigu.pierce.utils.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 * 目前存在到了配置文件中
 */
@Component
public class ProxyConfig implements Serializable {

    @Autowired
    ClientMapper clientMapper;

    private static final long serialVersionUID = 1L;

    /**
     * 配置文件为config.json
     */
//    public static final String CONFIG_FILE;

    private static Logger logger = LoggerFactory.getLogger(ProxyConfig.class);

    static {

        // 代理配置信息存放在用户根目录下
      /*  String dataPath = System.getProperty("user.home") + "/" + ".underdog/";
        File file = new File(dataPath);
        if (!file.isDirectory()) {
            file.mkdir();
        }

        CONFIG_FILE = dataPath + "/config.json";*/
        //通过SpringUtil工具类获取Spring上下文容器
//        clientMapper = SpringUtil.getBean(PierceService.class);
    }


    /**
     * 代理客户端，支持多个客户端
     */
    private List<Client> clients;

    /**
     * 更新配置后保证在其他线程即时生效
     */
//    private static ProxyConfig instance = new ProxyConfig();

    /**
     * 代理服务器为各个代理客户端（key）开启对应的端口列表（value）
     */
    private volatile Map<String, List<Integer>> clientInetPortMapping = new HashMap<String, List<Integer>>();

    /**
     * 代理服务器上的每个对外端口（key）对应的代理客户端背后的真实服务器信息（value）
     */
    private volatile Map<Integer, String> inetPortLanInfoMapping = new HashMap<Integer, String>();

    /**
     * 配置变化监听器
     */
    private List<ConfigChangedListener> configChangedListeners = new ArrayList<ConfigChangedListener>();
//
//    private ProxyConfig() {
//        update(null);
//    }

    /**
     * 解析配置文件
     */
//    public void update(String proxyMappingConfigJson) {
//        File file = new File(CONFIG_FILE);
//        try {
//            if (proxyMappingConfigJson == null && !file.exists()) {
//                InputStream in = new FileInputStream(file);
//                byte[] buf = new byte[1024];
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                int readIndex;
//                while ((readIndex = in.read(buf)) != -1) {
//                    out.write(buf, 0, readIndex);
//                }
//                in.close();
//                proxyMappingConfigJson = new String(out.toByteArray(), Charset.forName("UTF-8"));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        List<Client> clients = JSONObject.parseObject(proxyMappingConfigJson, new TypeReference<List<Client>>() {
//        });
//
//        if (clients == null) {
//            clients = new ArrayList<>();
//        }
//
//        Map<String, List<Integer>> clientInetPortMapping = new HashMap<String, List<Integer>>();
//        Map<Integer, String> inetPortLanInfoMapping = new HashMap<Integer, String>();
//
//        // 构造端口映射关系
//        for (Client client : clients) {
//            String clientKey = client.getClientKey();
//            if (clientInetPortMapping.containsKey(clientKey)) {
//                throw new IllegalArgumentException("密钥同时作为客户端表示，不能重复:" + clientKey);
//            }
//            List<ClientProxyMapping> mappings = client.getProxyMappings();
//            List<Integer> ports = new ArrayList<>();
//            clientInetPortMapping.put(clientKey, ports);
//            for (ClientProxyMapping mapping : mappings) {
//                Integer port = mapping.getInetPort();
//                ports.add(port);
//                if (inetPortLanInfoMapping.containsKey(port))
//                    throw new IllegalArgumentException("一个公网端口只能映射一个后端信息，不能重复:" + port);
//                inetPortLanInfoMapping.put(port, mapping.getLan());
//            }
//        }
//
//        // 替换之前的配置关系
//        this.clientInetPortMapping = clientInetPortMapping;
//        this.inetPortLanInfoMapping = inetPortLanInfoMapping;
//        this.clients = clients;
//
//        if (proxyMappingConfigJson != null) {
//            try {
//                FileOutputStream out = new FileOutputStream(file);
//                out.write(proxyMappingConfigJson.getBytes(Charset.forName("UTF-8")));
//                out.flush();
//                out.close();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        notifyconfigChangedListeners();
//
//    }


    /**
     * 配置更新通知
     */
    private void notifyconfigChangedListeners() {
        List<ConfigChangedListener> changedListeners = new ArrayList<ConfigChangedListener>(configChangedListeners);
        for (ConfigChangedListener changedListener : changedListeners) {
            changedListener.onChanged();
        }
    }

    /**
     * 添加配置变化监听器
     *
     * @param configChangedListener
     */
    public void addConfigChangedListener(ConfigChangedListener configChangedListener) {
        configChangedListeners.add(configChangedListener);
    }

    /**
     * 移除配置变化监听器
     *
     * @param configChangedListener
     */
    public void removeConfigChangedListener(ConfigChangedListener configChangedListener) {
        configChangedListeners.remove(configChangedListener);
    }


    /**
     * 获取代理客户端对应的代理服务器端口
     *
     * @param clientKey
     * @return
     */
    public Integer getClientInetPorts(String clientKey) {
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Client::getClientKey, clientKey);
        Client client = clientMapper.selectOne(queryWrapper);
        return client == null ? null : client.getInetPort();
//        return clientInetPortMapping.get(clientKey);
    }


    /**
     * 获取所有的clientKey
     *
     * @return
     */
    public Set<String> getClientKeySet() {
//        return clientMapper.selectList(null).stream().map(Client::getClientKey).collect(Collectors.toSet());
        return clientInetPortMapping.keySet();
    }

    /**
     * 根据端口号返回实际需要的代理信息
     *
     * @param port
     * @return
     */
    public String getLanInfo(Integer port) {
        QueryWrapper<Client> clientQueryWrapper = new QueryWrapper<>();
        clientQueryWrapper.lambda().eq(Client::getInetPort, port);
        return clientMapper.selectOne(clientQueryWrapper).getLan();
//        return inetPortLanInfoMapping.get(port);
    }

    /**
     * TODO 应该不需要，反转为，当客户端启动是，根据client反查port进行监听。
     * 返回需要绑定在代理服务器上的端口
     * 返回当前绑定的公网端口号。
     *
     * @return
     */
    public List<Integer> getUserPorts() {
        return inetPortLanInfoMapping.keySet().stream().collect(Collectors.toList());
    }

    public void updatePortStatus(Integer port, int i) {
        Client client = clientMapper.selectOne(new QueryWrapper<Client>().lambda().eq(Client::getInetPort, port));
        client.setStatus(i);
        clientMapper.updateById(client);
    }

//
//    public ProxyConfig getInstance() {
//        return instance;
//    }
}
