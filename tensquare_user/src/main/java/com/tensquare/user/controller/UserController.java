package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }
    
    /**
     * 批量修改
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result update(@RequestBody List userList) {
        userService.batchUpdate(userList);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
//        删除用户，必须拥有管理员权限，否则不能删除。
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null) {
//            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
//        }
//        if (!authHeader.startsWith("Bearer ")) {
//            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
//        }
//        String token = authHeader.substring(7);
//        Claims claims = jwtUtil.parseJWT(token);
//        if (claims == null) {
//            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
//        }
//        if (!"admin".equals(claims.get("roles"))) {
//            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
//        }

        /**
         * 如果我们每个方法都去写上面一段代码，冗余度太高，不利于维护，那如何做使我们的代码
         * 看起来更清爽呢？我们可以将这段代码放入拦截器去实现。
         */
        Claims claims = (Claims) request.getAttribute("admin_claims");
        if (claims == null) {
            return new Result(true, StatusCode.ACCESSERROR, "权限不足");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendSms(@PathVariable String mobile) {
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    /**
     * 用户注册
     *
     * @param user 用户
     * @param code 短信验证码
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        userService.add(user, code);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(String mobile, String password) {
        User user = userService.findByMobileAndPassWord(mobile, password);
        if (user != null) {
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("name", user.getNickname());
            map.put("avatar", user.getAvatar());
            return new Result(true, StatusCode.OK, "登录成功", map);
        } else {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }
    }

    /**
     * 增加用户粉丝数
     *
     * @param userId
     * @param count
     * @return
     */
    @RequestMapping(value = "/incFans/{userId}/{count}", method = RequestMethod.POST)
    public void incFansCount(@PathVariable String userId, @PathVariable int count) {
        userService.incFansCount(userId, count);
    }

    /**
     * 增加用户关注数
     *
     * @param userId
     * @param count
     * @return
     */
    @RequestMapping(value = "/incFollow/{userId}/{count}", method = RequestMethod.POST)
    public void incFollowCount(@PathVariable String userId, @PathVariable int count) {
        userService.incFollowCount(userId, count);
    }
}
