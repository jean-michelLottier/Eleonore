package com.dashboard.eleonore.profile.repository;

import com.dashboard.eleonore.profile.repository.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    @Modifying
    @Query("delete from AuthToken auth where auth.token = :token")
    void deleteByToken(@Param("token") String token);

    @Query("select auth from AuthToken auth where auth.token = :token")
    Optional<AuthToken> findByToken(@Param("token") String token);
}
