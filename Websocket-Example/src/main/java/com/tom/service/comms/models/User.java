package com.tom.service.comms.models;

import com.tom.service.comms.models.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User {

	@Id
	private Integer id;

	@Column(name = "nick_name", unique = true, updatable = true, nullable = false)
	private String nickName;
	
	@Column(name = "full_name", unique = false, updatable = true, nullable = true)
	private String fullName;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status", unique = false, updatable = true, nullable = false)
	private Status status;
	
}
