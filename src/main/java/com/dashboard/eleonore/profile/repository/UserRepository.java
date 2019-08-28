package com.dashboard.eleonore.profile.repository;

import com.dashboard.eleonore.profile.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
