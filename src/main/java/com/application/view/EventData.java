package com.application.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventData {

	private String id;

	private State state;

	private String type;

	private String host;

	private long timestamp;
}
