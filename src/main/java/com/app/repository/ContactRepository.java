package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
