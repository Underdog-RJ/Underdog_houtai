package controller;


import com.atguigu.commonutils.R;
import config.AliyunLiveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import util.AliyunLiveUtil;

import java.util.Map;

@RestController
@RequestMapping("/live/aliyunLive")
public class AliyunLiveController {
    @Autowired
    private AliyunLiveConfig aliyunLiveConfig;

    @GetMapping("addLive")
    public R addLive(Integer courseId) {

        /**
         * 注意，推流要在播流域名里面生成
         */
        String pushUrl = AliyunLiveUtil.createPushUrl(courseId, aliyunLiveConfig);
        return R.ok().data("pushUrl",pushUrl);
    }

    @GetMapping("getLive")
    public R getLive(Integer courseId) {

        /**
         * 注意，推流要在播流域名里面生成
         */

        Map<String, String> pullUrl = AliyunLiveUtil.createPullUrl(courseId,  aliyunLiveConfig);

        return R.ok().data("pullUrl",pullUrl);
    }

}
