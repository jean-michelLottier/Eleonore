package com.dashboard.eleonore.profile.repository;

import com.dashboard.eleonore.profile.repository.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    @Modifying
    @Query("delete from #{#entityName} auth where auth.token = :token")
    void deleteByToken(@Param("token") String token);
}
