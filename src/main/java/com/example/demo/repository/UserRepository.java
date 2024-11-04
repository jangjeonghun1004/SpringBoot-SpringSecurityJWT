package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 회원 정보를 가져옵니다.
     * @param uid 아이디
     * @return User 객체
     */
    User getByUid(String uid);
}
