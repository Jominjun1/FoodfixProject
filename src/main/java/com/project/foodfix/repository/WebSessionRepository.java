package com.project.foodfix.repository;

import com.project.foodfix.model.WebSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebSessionRepository extends JpaRepository<WebSession, String>{
    @Transactional
    @Modifying
    @Query("SELECT ws.session_id FROM WebSession ws WHERE ws.store_id = :store_id")
    Optional<String> findByStoreId(@Param("store_id") Long store_id);

    @Transactional
    @Modifying
    @Query("SELECT ws.session_id FROM WebSession ws WHERE ws.user_id = :user_id")
    Optional<String> findByUserId(@Param("user_id") String user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM WebSession ws WHERE ws.session_id = :session_id")
    void deleteBySession(@Param("session_id")String session_id);
}
