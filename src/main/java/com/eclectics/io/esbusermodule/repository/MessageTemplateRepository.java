package com.eclectics.io.esbusermodule.repository;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.eclectics.io.esbusermodule.model.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate,Long> {
    Optional<MessageTemplate> findByMessageTypeAndActiveTrueAndSoftDeleteFalse(MessageType messageType);
    MessageTemplate findByMessageTypeAndDefaultTemplateTrueAndSoftDeleteFalse(MessageType messageType);
}
