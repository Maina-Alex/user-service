package com.eclectics.io.esbusermodule.model;

import com.eclectics.io.esbusermodule.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_message_templates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTemplate extends BaseEntity{
    @Lob
    private String message;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @JsonIgnore
    private String baseParams;
    @Column(columnDefinition = " default 0",name = "active")
    private Boolean active = Boolean.TRUE;
    private boolean defaultTemplate=false;
}
