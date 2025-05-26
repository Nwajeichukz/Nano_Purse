package com.NanoPurse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractAuditingEntity{
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
   @Column(length = 36, nullable = false, updatable = false)
   private String id;

   private String email;

   @JsonIgnore
   private String password;

   @ManyToOne(cascade = CascadeType.PERSIST)
   @JoinColumn(name = "role_id")
   private Role role;
}
