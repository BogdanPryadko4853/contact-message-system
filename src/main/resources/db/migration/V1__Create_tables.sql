-- V1__Create_tables.sql
CREATE TABLE IF NOT EXISTS message_topic (
                                             id BIGINT PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS contact (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    full_name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS message (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       contact_id BIGINT NOT NULL,
                                       topic_id BIGINT NOT NULL,
                                       content TEXT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (contact_id) REFERENCES contact(id),
    FOREIGN KEY (topic_id) REFERENCES message_topic(id)
    );