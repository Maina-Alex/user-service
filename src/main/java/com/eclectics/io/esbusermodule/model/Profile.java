package com.eclectics.io.esbusermodule.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_profiles")
@Builder
@SQLDelete(sql = "update tb_profiles set soft_delete=true")
@Where(clause = "soft_delete = false")
public class Profile extends BaseEntity {
    private String name;
    private String remarks;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;

}
