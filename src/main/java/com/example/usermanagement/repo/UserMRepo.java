package com.example.usermanagement.repo;

import com.example.usermanagement.entity.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMRepo extends JpaRepository<UserM, Long> {
    UserM findByEmail(String email);

    UserM findByUserName(String userName);
}
