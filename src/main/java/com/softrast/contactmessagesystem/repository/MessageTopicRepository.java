package com.softrast.contactmessagesystem.repository;

import com.softrast.contactmessagesystem.model.entity.MessageTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTopicRepository extends JpaRepository<MessageTopic, Long> {
}
