package com.project.foodfix.repository;

import com.project.foodfix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
