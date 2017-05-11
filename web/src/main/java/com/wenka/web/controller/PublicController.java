package com.wenka.web.controller;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.wenka.commons.util.RandomCode;
import com.wenka.commons.web.AuthNotRequired;
import com.wenka.domain.model.User;
import com.wenka.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共访问控制器
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/10.
 */
@RestController
@AuthNotRequired
@RequestMapping("/pub")
public class PublicController {

//    @Value("${taobao.appKey}")
    private String appKey = "23554880";

//    @Value("${taobao.appSecret}")
    private String appSecret = "c3ad75ff1e00b7d36830da0884ee16d7";

//    @Value("${taobao.reqUrl}")
    private String reqUrl = "http://gw.api.taobao.com/router/rest";

//    @Value("${smsFreeSignName}")
    private String smsFreeSignName = "文卡";

//    @Value("${smsTemplateCode}")
    private String smsTemplateCode = "SMS_32750063";

//    @Value("${smsType}")
    private String smsType = "normal";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sendCode/{tel}",method = RequestMethod.GET)
    public Map sendCodeMsg(@PathVariable String tel){

        Map<String,Object> args = new HashMap<String,Object>();

        String code = RandomCode.getRandomCode();

        //发送手机验证码
        TaobaoClient client = new DefaultTaobaoClient(reqUrl, appKey, appSecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType(smsType);
        req.setSmsFreeSignName(smsFreeSignName);
        req.setSmsParamString("{code:'" + code + "'}");
        req.setRecNum(tel);
        req.setSmsTemplateCode(smsTemplateCode);

        AlibabaAliqinFcSmsNumSendResponse response;
        try {
            response = client.execute(req);
        }catch (ApiException e){
            throw new RuntimeException(e);
        }

        args.put("code",code);
        args.put("response",response);
        return args;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User save(@RequestBody User user){
        this.userService.saveSegister(user);
        return user;
    }
}
