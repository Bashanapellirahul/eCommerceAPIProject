package com.ECommerceAPI.ECommerceAPI.user.repository;

import com.ECommerceAPI.ECommerceAPI.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUserName(String userName);

    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

}
