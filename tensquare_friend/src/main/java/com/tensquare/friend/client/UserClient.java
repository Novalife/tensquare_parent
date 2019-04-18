package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户客户端
 * 注意,@PathVariable注解一定要指定参数名称，否则出错
 */
@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 增加用户粉丝数
     * @param userId
     * @param count
     * @return
     */
    @RequestMapping(value = "/user/incFans/{userId}/{count}", method = RequestMethod.POST)
    void incFansCount(@PathVariable("userId") String userId, @PathVariable("count") int count);

    /**
     * 增加用户关注数
     * @param userId
     * @param count
     * @return
     */
    @RequestMapping(value = "/user/incFollow/{userId}/{count}", method = RequestMethod.POST)
    void incFollowCount(@PathVariable("userId") String userId, @PathVariable("count") int count);
}