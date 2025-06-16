package com.softrast.contactmessagesystem.repository;

import com.softrast.contactmessagesystem.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmailAndPhone(String email, String phone);
}