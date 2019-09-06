package com.dashboard.eleonore.profile.repository;

import com.dashboard.eleonore.profile.repository.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    @Query("select case when count(a) > 0 then true else false end from Authentication a where a.login = :login and a.password = :password")
    boolean exists(@Param("login") String login, @Param("password") String password);

    @Query("select a from Authentication a where a.login = :login and a.password = :password")
    Optional<Authentication> findByLoginPassword(@Param("login") String login, @Param("password") String password);

    @Query("select a from Authentication a where a.id = (select t.authenticationId from AuthToken t where t.token = :token)")
    Optional<Authentication> findByToken(@Param("token") String token);
}
