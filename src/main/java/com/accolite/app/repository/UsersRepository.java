package com.accolite.app.repository;

import com.accolite.app.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UsersRepository extends JpaRepository<UsersEntity,Long> {
    @Query(value = """
      select t from UsersEntity t where t.email=:email
      """)
    Optional<UsersEntity> findByEmail(String email);
}
