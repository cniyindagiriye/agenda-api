package com.cse.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cse.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findById(String userId);

	@Query(value = "select u from User u where u.email = ?1")
	User findByEmailAddress(String email);

	@Query(value = "select u from User u where u.email = ?1 and u.password = ?2")
	User login(String email, String password);
}
