package com.example.SmartContactManager.dao;

import com.example.SmartContactManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
