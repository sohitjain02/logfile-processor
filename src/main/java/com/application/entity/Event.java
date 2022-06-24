package com.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "duration")
	private long duration;

	@Column(name = "type")
	private String type;

	@Column(name = "host")
	private String host;

	@Column(name = "alert")
	private boolean alert;
}
