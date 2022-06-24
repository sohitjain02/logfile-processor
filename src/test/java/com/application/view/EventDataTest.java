package com.application.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class EventDataTest {

	@Test
	public void testEventDataPOJO() {
		EventData eventData = new EventData();
		eventData.setId("1");
		eventData.setType(null);
		eventData.setHost(null);
		eventData.setState(State.FINISHED);
		eventData.setTimestamp(1491377495213L);

		assertNotNull(eventData.getId());
		assertNotNull(eventData.getState());
		assertNotNull(eventData.getTimestamp());
		assertNull(eventData.getType());
		assertNull(eventData.getHost());
	}
}
