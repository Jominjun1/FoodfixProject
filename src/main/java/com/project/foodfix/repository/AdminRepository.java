package com.project.foodfix.repository;

import com.project.foodfix.model.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Admin a where a.admin_id = :admin_id")
    void deleteByAdmin(@Param("admin_id") String admin_id);
}