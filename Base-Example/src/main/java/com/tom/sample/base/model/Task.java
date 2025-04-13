package com.tom.sample.base.model;

import java.time.LocalDate;

import com.tom.sample.base.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tasks")
public class Task extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(name = "title", nullable = false, updatable = true, unique = false)
    private String title;
	
    @Column(name = "description", nullable = false, updatable = true, unique = false)
	private String description;
	
	@Column(name = "due_date", nullable = false, updatable = true, unique = false)
	private LocalDate dueDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, unique = false)
	private Status status;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	private User user;
}
