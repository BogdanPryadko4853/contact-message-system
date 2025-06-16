package com.softrast.contactmessagesystem.repository;

import com.softrast.contactmessagesystem.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
