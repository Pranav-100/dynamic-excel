package com.data.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "B_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String country;

	private String code;

	private String city;
	private String state;

	private String activate;

	private String createdBy;

	private LocalDate createdOn;
}

