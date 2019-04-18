package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新用户粉丝数
     *
     * @param userId 用户ID
     * @param count 粉丝数
     */
    @Modifying
    @Query("update User u set u.fanscount = u.fanscount+?2 where u.id = ?1")
    void incFansCount(String userId, int count);

    /**
     * 更新用户关注数
     * @param userId
     * @param count
     */
    @Modifying
    @Query("update User u set u.followcount = u.followcount+?2 where u.id=?1")
    void incFollowCount(String userId,int count);
}
