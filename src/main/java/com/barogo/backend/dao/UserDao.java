package com.barogo.backend.dao;

import com.barogo.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Boolean existsUserByUserId(String userId);
}
