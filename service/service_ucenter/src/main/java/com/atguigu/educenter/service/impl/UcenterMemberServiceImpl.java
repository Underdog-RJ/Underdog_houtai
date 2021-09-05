package com.atguigu.educenter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.client.EnjoyBlogClient;
import com.atguigu.educenter.config.ConstantPropertiesUtil;
import com.atguigu.educenter.entity.*;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.*;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.educenter.utils.MailUtils;
import com.atguigu.educenter.utils.RandomUtil;
import com.atguigu.educenter.vo.CountInfo;
import com.atguigu.educenter.vo.RegisterVo;

import com.atguigu.educenter.vo.UcentmentberVo;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-30
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private TbFriendService tbFriendService;

    @Autowired
    private TbFriendReqService tbFriendReqService;

    @Autowired
    private UcenterKechengService ucenterKechengService;

    @Autowired
    private UcenterShuoshuoService ucenterShuoshuoService;

    @Autowired
    private EnjoyBlogClient enjoyBlogClient;

    @Override
    public R getWeather(String location)  {

        try {
            String url="https://api.seniverse.com/v3/weather/daily.json?key=SFpSV9yPtU8NFwgBl&location=%s";

            String finalUrl=String.format(url,location);
            String result = HttpClientUtils.get(finalUrl);
            Gson gson=new Gson();
            HashMap hashMap = gson.fromJson(result, HashMap.class);
            return R.ok().data(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String login(UcenterMember ucenterMember) {

        //获取登录手机号和密码
        String password = ucenterMember.getPassword();
        String mobile = ucenterMember.getMobile();

        //手机号和密码是否为空
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)){
            throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobieMember = baseMapper.selectOne(wrapper);
        if(mobieMember==null){
            throw new GuliException(20001,"登录失败");
        }

        //判断密码
        if(!MD5.encrypt(password).equals(mobieMember.getPassword()))
        {
            throw new GuliException(20001,"登录失败");
        }

        //判断用户是否禁用
        if(mobieMember.getIsDisabled()){
            throw new GuliException(20001,"登录失败");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String token = JwtUtils.getJwtToken(mobieMember.getId(), mobieMember.getNickname());
        return token;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if(StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码
        //获取验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!redisCode.equals(code)){
            throw new GuliException(20001,"注册失败");
        }

        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"注册失败");
        }

        //添加到数据库
        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        member.setUCoin(200);
        this.save(member);
    }
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegister(String day) {

        return baseMapper.countRegister(day);
    }

    @Override
    public List<UcenterMember> getUserByName(String nickname,String memeberId) {
        QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("nickname",nickname);
        List<UcenterMember> ucenterMembers = baseMapper.selectList(queryWrapper);
        List<UcenterMember> collect = ucenterMembers.stream().filter(ucenterMember -> !ucenterMember.getId().equals(memeberId)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R addFriend(String friendid, String memberId) {

        QueryWrapper<TbFriend> tbFriendQueryWrapper=new QueryWrapper<>();

        tbFriendQueryWrapper.eq("userid",memberId);
        tbFriendQueryWrapper.eq("friends_id",friendid);

        TbFriend one = tbFriendService.getOne(tbFriendQueryWrapper);
        if(!StringUtils.isEmpty(one)){
            return R.ok().message("已经是您的好友了");
        }

        QueryWrapper<TbFriendReq> tbFriendReqQueryWrapper=new QueryWrapper<>();

        tbFriendReqQueryWrapper.eq("from_userid",memberId);
        tbFriendReqQueryWrapper.eq("to_userid",friendid);
        tbFriendReqQueryWrapper.eq("status",0);

        TbFriendReq tbFriendReq = tbFriendReqService.getOne(tbFriendReqQueryWrapper);

        if(!StringUtils.isEmpty(tbFriendReq)){
            return R.ok().message("您已经申请过了");
        }


        TbFriendReq tbF=new TbFriendReq();
        tbF.setFromUserid(memberId);
        tbF.setToUserid(friendid);
        tbF.setStatus(0);
        tbFriendReqService.save(tbF);
        return R.ok().message("申请成功");
    }

    @Override
    public List<UcentmentberVo> findFriendReqByUserid(String memberId) {
        QueryWrapper<TbFriendReq> tbFriendReqQueryWrapper=new QueryWrapper<>();
        tbFriendReqQueryWrapper.eq("to_userid",memberId);
        tbFriendReqQueryWrapper.eq("status",0);

        List<TbFriendReq> listReq = tbFriendReqService.list(tbFriendReqQueryWrapper);
        List<String> ids = listReq.stream().map(tbFriendReq -> tbFriendReq.getFromUserid()).collect(Collectors.toList());

        if(ids.size()==0){
           return null;
        }
        List<UcenterMember> ucenterMemberList = baseMapper.selectBatchIds(ids);
        List<UcentmentberVo> finalList=new ArrayList<>();
        for (TbFriendReq tbFriendReq : listReq) {
            for (UcenterMember ucenterMember : ucenterMemberList) {
                if(tbFriendReq.getFromUserid().equals(ucenterMember.getId())){
                    UcentmentberVo ucentmentberVo=new UcentmentberVo();
                    BeanUtils.copyProperties(ucenterMember,ucentmentberVo);
                    ucentmentberVo.setReqId(tbFriendReq.getId());
                    finalList.add(ucentmentberVo);
                }
            }
        }
        return finalList;
    }

    @Override
    public R acceptFriendReq(String reqId) {
        //1.将好友请求的status标志设置为1，表示已经处理了该还有请求

        TbFriendReq one = tbFriendReqService.getById(reqId);
        if(!one.getStatus().equals(0)){
            return R.ok().message("已处理");
        }
        one.setStatus(1);
        tbFriendReqService.updateById(one);
        String friendId = one.getFromUserid();
        String memberId = one.getToUserid();
        //2.互相添加好友，在tb_friend表中应该有两条记录
        TbFriend oneFriend =new TbFriend();
        oneFriend.setFriendsId(friendId);
        oneFriend.setUserid(memberId);

        TbFriend twoFriend =new TbFriend();
        twoFriend.setFriendsId(memberId);
        twoFriend.setUserid(friendId);

        List<TbFriend> list=new ArrayList<>();
        list.add(oneFriend);
        list.add(twoFriend);

        tbFriendService.saveBatch(list);
        return R.ok().message("添加成功");
    }

    @Override
    public R ignoreFriendReq(String reqId) {
        //1.将好友请求的status标志设置为1，表示已经处理了该还有请求

        TbFriendReq one = tbFriendReqService.getById(reqId);
        if(!one.getStatus().equals(0)){
            return R.ok().message("已处理");
        }
        one.setStatus(1);
        tbFriendReqService.updateById(one);
        return R.ok().message("已拒绝");
    }

    @Override
    public List<UcenterMember> ucenterMemberService(String memberId) {

         QueryWrapper<TbFriend> tbFriendQueryWrapper=new QueryWrapper<>();
         tbFriendQueryWrapper.eq("userid",memberId);
        List<TbFriend> list = tbFriendService.list(tbFriendQueryWrapper);
        List<String> ids = list.stream().map(tbFriend -> tbFriend.getFriendsId()).collect(Collectors.toList());
        if(ids.size()==0){
            return null;
        }
        List<UcenterMember> memberList = baseMapper.selectBatchIds(ids);
        return memberList;
    }

    @Override
    public R setMail(String mail) {

        String code = redisTemplate.opsForValue().get(mail);
        if(!StringUtils.isEmpty(code)){
            return R.error().message("以为您的邮箱发送验证码，请勿重复发送");
        }


        code = RandomUtil.getFourBitRandom();

        boolean flag = MailUtils.sendMail(mail, "此邮件为UnderdogEdu验证邮箱，您的验证码为："+code, "UnderdogEdu邮箱验证");
        if(flag){
            redisTemplate.opsForValue().set(mail,code,5, TimeUnit.MINUTES);
            return R.ok().message("发送成功,5分钟内有效");
        }else {
            return R.error().message("发送失败");
        }
    }

    @Override
    public List<UcenterVo> getUserInfoByIds(List<UnReadMessage> list) {

        List<UcenterVo> listResul=new ArrayList<>();
        for (UnReadMessage unReadMessage : list) {
            UcenterVo temp=new UcenterVo();
            UcenterMember ucenterMember = baseMapper.selectById(unReadMessage.getName());
            BeanUtils.copyProperties(ucenterMember,temp);
            temp.setCount(unReadMessage.getCount());
            listResul.add(temp);
        }

        return listResul;
    }

    @Override
    public CountInfo getUserCountInfo(String userId) {

        QueryWrapper<UcenterShuoshuo> wrapperShuoshuo=new QueryWrapper<>();
        wrapperShuoshuo.eq("acl_user_id",userId);
        int countShuoshuo = ucenterShuoshuoService.count(wrapperShuoshuo);

        QueryWrapper<UcenterKecheng> wrapperKecheng=new QueryWrapper<>();
        wrapperKecheng.eq("user_id",userId);
        wrapperKecheng.eq("is_collect",1);
        int countKecheng = ucenterKechengService.count(wrapperKecheng);

        Integer countBlog = enjoyBlogClient.EnjoyBlogCountById(userId);
        CountInfo countInfo=new CountInfo();
        countInfo.setBlog(countBlog);
        countInfo.setKecheng(countKecheng);
        countInfo.setShuoshuo(countShuoshuo);
        return countInfo;
    }

    @Override
    public R setMobile(String mobile, String code,String userId) {
        //判断验证码
        //获取验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!redisCode.equals(code)){
            return R.error().message("验证码错误");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            return R.error().message("手机号重复");
        }
        UcenterMember ucenterMember = baseMapper.selectById(userId);
        if(StringUtils.isEmpty(ucenterMember.getMobile())){
            int uCoin = ucenterMember.getUCoin();
            ucenterMember.setUCoin(uCoin+100);
        }
        ucenterMember.setMobile(mobile);
        baseMapper.updateById(ucenterMember);
        return R.ok().message("绑定成功");
    }

    @Override
    public R valideOwnMail(String memeberId, String mail, String code) {
        String redisCode = redisTemplate.opsForValue().get(mail);
        if(!redisCode.equals(code)){
            return R.error().message("验证码错误");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mail",mail);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            return R.error().message("邮箱重复");
        }
        UcenterMember ucenterMember = baseMapper.selectById(memeberId);
        if(StringUtils.isEmpty(ucenterMember.getMail())){
            int uCoin = ucenterMember.getUCoin();
            ucenterMember.setUCoin(uCoin+100);
        }
        ucenterMember.setMail(mail);
        baseMapper.updateById(ucenterMember);
        return R.ok().message("绑定成功");

    }

    @Override
    public UcenterMember updateUserPassword(String userId, String password) {
        UcenterMember ucenterMember = baseMapper.selectById(userId);
        if(ucenterMember!=null){
            if(StringUtils.isEmpty(ucenterMember.getPassword())){
                int uCoin = ucenterMember.getUCoin();
                ucenterMember.setUCoin(uCoin+100);
            }
            ucenterMember.setPassword(MD5.encrypt(password));
            baseMapper.updateById(ucenterMember);
            return ucenterMember;
        }else {
            throw new GuliException();
        }
    }


    @Override
    public R resetPassword(ResetPasswordVo resetPasswordVo) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(resetPasswordVo.getMail())){
            wrapper.eq("mail",resetPasswordVo.getMail());
        }
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if(ucenterMember==null){
            return R.error().message("该用户为空");
        }
        String redisCode = redisTemplate.opsForValue().get(resetPasswordVo.getMail());

        if(redisCode.equals(resetPasswordVo.getCode())){
            ucenterMember.setPassword(MD5.encrypt(resetPasswordVo.getPassword()));
            baseMapper.updateById(ucenterMember);
            return R.ok().message("小lg,哥哥已经为你重置密码");
        }else {
            return R.error().message("小lg,验证码不对");
        }
    }

    @Override
    public UcenterMember loginUser(String code) {
        String baseURL = "http://open.51094.com/user/auth.html";
        String params = "type=get_user_info&code="+code+"&appid="+ ConstantPropertiesUtil.OAUTH_APPID+"&token="+ConstantPropertiesUtil.OAUTH_TOKEN;
        String result = HttpRequest.sendPost(baseURL, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String openid = jsonObject.getString("uniq");
        UcenterMember member = getOpenIdMember(openid);
        if(member==null){
            String nickname = jsonObject.getString("name");
            String avatar = jsonObject.getString("img");
            Integer sex = jsonObject.getInteger("sex");
            member=new UcenterMember();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setSex(sex);
            member.setUCoin(100);
            baseMapper.insert(member);
        }
        return member;
    }


    @Override
    public R userSign(HttpServletRequest request) {

        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)){
            return R.ok().code(28004);
        }
        boolean flag = doSign(userId, LocalDate.now());
        long continuousSignCount = getContinuousSignCount(userId, LocalDate.now());

        int count=(int)continuousSignCount;
        UcenterMember ucenterMember = baseMapper.selectById(userId);
        if(flag){
            switch (count){
                case 1:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+1));
                    break;
                case 2:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+2));
                    break;
                case 3:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+3));
                    break;
                case 4:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+4));
                    break;
                case 5:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+5));
                    break;
                case 6:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+6));
                    break;
                case 7:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+7));
                    break;
                default:
                    baseMapper.updateById(ucenterMember.setUCoin(ucenterMember.getUCoin()+7));
                    break;
            }
        }

        return R.ok().data("flag",flag).data("userInfo",ucenterMember);
    }

    @Override
    public R checkSign(HttpServletRequest request) {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)){
            return R.ok().code(28004);
        }
        boolean flag = checkSign(userId, LocalDate.now());

        return R.ok().data("flag",flag);
    }

    @Override
    public R userSignCount(HttpServletRequest request) {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)){
            return R.ok().code(28004);
        }
        long signCount = getSignCount(userId, LocalDate.now());
        long continuousSignCount = getContinuousSignCount(userId, LocalDate.now());
        return R.ok().data("countMonth",signCount).data("countCountinuous",continuousSignCount);
    }

    /**
     * 获取用户签到次数
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当前的签到次数
     */
    public long getSignCount(String uid, LocalDate date) {
        String key = buildSignKey(uid, date);
        return getBitCount(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public Long getBitCount(String key){

        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }

    /**
     * 获取当月连续签到次数
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当月连续签到次数
     */
    public long getContinuousSignCount(String uid, LocalDate date) {
        int signCount = 0;
        int max=0;
        String type = String.format("u%d", date.getDayOfMonth());
        List<Long> list = bitfield(buildSignKey(uid, date), date.getDayOfMonth(),0);
        if (list != null && list.size() > 0) {
            // 取低位连续不为0的个数即为连续签到次数，需考虑当天尚未签到的情况
            long v = list.get(0) == null ? 0 : list.get(0);
            for (int i = 0; i < date.getDayOfMonth(); i++) {
                System.out.println(v>>1);
                System.out.println(v<<1);
                System.out.println(v>>1<<1);
                if (v >> 1 << 1 == v) {
                    // 低位为0且非当天说明连续签到中断了
                    if (i > 0){
                        if(signCount>max){
                            max=signCount;
                            signCount=0;
                        }
                    }
                } else {
                    signCount += 1;
                }
                v >>= 1;
            }
        }
        return max;
    }

    /**
     *
     * @param buildSignKey bitmap的key
     * @param limit 到那位结束
     * @param offset
     * @return
     */
    public List<Long> bitfield(String buildSignKey,int limit,int offset){
        return redisTemplate.execute(
                (RedisCallback<List<Long>>) con-> con.bitField(buildSignKey.getBytes(),
                        BitFieldSubCommands.
                                create().
                                get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset)));
    }
    /**
     * 检查用户是否签到
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当前的签到状态
     */
    public boolean checkSign(String uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;
        return redisTemplate.opsForValue().getBit(buildSignKey(uid, date), offset);
    }


    /**
     * 用户签到
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 之前的签到状态
     */
    public boolean doSign(String uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;

        return redisTemplate.opsForValue().setBit(buildSignKey(uid, date), offset, true);
    }


    /**
     * String.format()  %s代表字符串，%d代表数字
     * @param uid
     * @param date
     * @return
     */
    private static String buildSignKey(String uid, LocalDate date) {
        return String.format("u:sign:%s:%s", uid, formatDate(date));
    }
    private static String formatDate(LocalDate date) {
        return formatDate(date, "yyyyMM");
    }

    private static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }




    /**
     * 远程调用，购买课程的接口
     * @param request
     * @param count
     * @return
     */
    @Override
    public boolean updateUseruCoin(HttpServletRequest request, Integer count) {

        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)){
            return false;
        }
        UcenterMember ucenterMember = baseMapper.selectById(userId);
        ucenterMember.setUCoin(count);
        int i = baseMapper.updateById(ucenterMember);
        if(i>0){
            return true;
        }else {
            return false;
        }


    }

    @Override
    public boolean updateUseruCoinById(Integer count, String id) {
        if(StringUtils.isEmpty(id)){
            return false;
        }
        UcenterMember ucenterMember = baseMapper.selectById(id);
        ucenterMember.setUCoin(count);
        int i = baseMapper.updateById(ucenterMember);
        if(i>0){
            return true;
        }else {
            return false;
        }
    }
}
