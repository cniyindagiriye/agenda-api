package com.cse.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cse.api.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	Contact findById(String contactId);

	@Query(value = "select u from Contact u where u.phoneNumber = ?1")
	Contact findByEmailAddress(String phoneNumber);
}
