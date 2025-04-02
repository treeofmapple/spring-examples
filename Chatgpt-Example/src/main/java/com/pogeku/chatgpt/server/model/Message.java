package com.pogeku.chatgpt.server.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Messages")
public class Message {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	
	private String messageContent;
	
	private LocalDate data;
	
	private LocalTime hora;
	
    @PrePersist
    public void prePersist() {
        this.data = LocalDate.now();
        this.hora = LocalTime.now();
    }
	
	/*
	
	Add later userAuthentication
	
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private User user;
	
	*/
}
