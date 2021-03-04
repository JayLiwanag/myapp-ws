package org.myapp.dev.core.user.dao;

import org.myapp.dev.core.user.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUserId(String userId);

    @Query(value = "Select * from Users",
            countQuery = "Select count(*) from Users",
            nativeQuery = true)
    Page<UserEntity> findAllUsersOnDB(Pageable pageable);

}
