package com.example.KBapexbackend_java.Repository;

import com.example.KBapexbackend_java.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
