package com.barogo.backend.dao.user;

import com.barogo.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Boolean existsUserByUserId(String userId);
}
