package com.wenka.web.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.wenka.commons.web.AuthNotRequired;
import com.wenka.commons.web.AuthToken;
import com.wenka.domain.model.User;
import com.wenka.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @Value("${token.timeout}")
    private String tokenTimeout = "14400";

    /**
     * 新增或者修改用户
     *
     * @param user
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody User user) {
        this.userService.saveOrUpdate(user);
    }

    /**
     * 注销用户
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        this.userService.delete(id);
    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable String id) {
        User user = userService.get(id);
        return user;
    }

    @AuthNotRequired
    @RequestMapping({"/login"})
    public User login(HttpServletRequest request, HttpServletResponse response) {
        String account = StringUtils.trimToNull(request.getParameter("account"));
        String password = StringUtils.trimToNull(request.getParameter("password"));
        String remberMe = StringUtils.trimToNull(request.getParameter("remberMe"));
        if (account == null) {
            throw new RuntimeException("用户名不能为空");
        } else if (password == null) {
            throw new RuntimeException("密码不能为空");
        } else {
            boolean rember = false;
            if ("true".equalsIgnoreCase(remberMe)) {
                rember = true;
            }

            User user = this.userService.getByAccountPswd(account, password);
            if (user == null) {
                throw new RuntimeException("用户名或密码错误");
            } else {
                user = this.userService.get(user.getId());
                AuthToken token = new AuthToken();
                token.setId(UUID.randomUUID().toString());
                token.setScope("/");
                token.setUid(user.getId());
                if (rember) {
                    token.setTimeout(Integer.valueOf(this.tokenTimeout));
                }

                request.setAttribute("authToken", token);
                response.addCookie(token.toCookie());
                return user;
            }
        }
    }

    @AuthNotRequired
    @RequestMapping({"/logout"})
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Object o = request.getAttribute("authToken");
        if (o != null) {
            AuthToken authToken = (AuthToken) o;
            authToken.setTimeout(-1);
            request.setAttribute("authToken", authToken);
            Cookie var10 = authToken.toCookie();
            var10.setMaxAge(0);
            response.addCookie(var10);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Cookie[] clean = cookies;
                int length = cookies.length;
                for (int i = 0; i < length; ++i) {
                    Cookie cookie = clean[i];
                    if ("m-at-id".equals(cookie.getName())) {
                        cookie.setValue(null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * 修改密码
     *
     * @param id
     * @param password
     */
    @RequestMapping(value = "/updatePswd", method = RequestMethod.GET)
    public void updatePswd(@RequestParam(required = true) String id,
                           @RequestParam(required = true) String password) {
        String pswd = StringUtils.trimToNull(password);

        if (StringUtils.isBlank(pswd)){
            throw new RuntimeException("密码不能为空");
        }
        this.userService.updatePswd(id,pswd);
    }

    @AuthNotRequired
    @RequestMapping(value = "/existed/{username}",method = RequestMethod.GET)
    public Boolean existed(@PathVariable String username){
        Boolean exist = userService.exist(username);
        return exist;
    }
}
