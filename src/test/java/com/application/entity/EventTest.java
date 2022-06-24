package com.application.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class EventTest {

	@Test
	public void testEntityPOJO() {
		Event event = Event.builder().id("1").alert(false).duration(5).host(null).type(null).build();
		assertNotNull(event.getId());
		assertNotNull(event.getDuration());
		assertFalse(event.isAlert());
		assertNull(event.getType());
		assertNull(event.getHost());

		Event event1 = new Event();
		assertNull(event1.getId());

		Event event2 = new Event(null, 0, null, null, false);
		assertNull(event2.getId());

	}
}
