package com.project.foodfix.mapper;

import com.project.foodfix.model.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdminMapper {
    @Select("SELECT * FROM Admin WHERE phone = #{phone}")
    Admin getAdminByPhone(@Param("phone") String phone);

    @Select("SELECT id , pw FROM Admin")
    List<Admin> getAdmins();

    @Insert("INSERT INTO Admin (id, name, phone, address, pw) " +
            "VALUES (#{id}, #{name}, #{phone}, #{address}, #{pw})")
    int insertAdmin(Admin admin);

    @Update("UPDATE Admin SET name=#{name}, address=#{address}, pw=#{pw} WHERE phone=#{phone}")
    int updateAdmin(Admin admin);

    @Delete("DELETE FROM Admin WHERE phone=#{phone}")
    int deleteAdmin(@Param("phone") String phone);
}
