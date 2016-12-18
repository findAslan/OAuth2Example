package com.hascode.tutorial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(@Param("username") String username);

	Account findByToken(@Param("token") String token);
}
