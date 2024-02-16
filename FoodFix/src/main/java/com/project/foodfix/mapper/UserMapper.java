package com.project.foodfix.mapper;

import com.project.foodfix.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("SELECT * FROM User WHERE user_id = #{user_id}")
    User getUserById(@Param("id") String user_id);

    @Select("SELECT * FROM User WHERE user_phone = #{user_phone}")
    User getUserByPhone(@Param("phone") String user_phone);

    @Select("SELECT * FROM User")
    List<User> getAllUsers();

    @Select("SELECT user_id , user_pw FROM User")
    List<User> getUsers();

    @Insert("INSERT INTO User (user_id, user_name, user_phone, address, user_pw , nickname , male) " +
            "VALUES (#{user_id}, #{user_name}, #{user_phone}, #{address}, #{user_pw} , #{nickname} , #{male})")
    int insertUser(User user);

    @Update("UPDATE User SET user_name=#{user_name}, address=#{address}, user_pw=#{user_pw} , " +
            "nickname=#{nickname} , male=#{male} WHERE user_phone=#{user_phone}")
    int updateUser(User user);

    @Delete("DELETE FROM User WHERE user_phone=#{user_phone}")
    int deleteUser(@Param("phone") String user_phone);
}
