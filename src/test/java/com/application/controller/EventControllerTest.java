package com.application.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.application.entity.Event;
import com.application.repository.EventRepository;

public class EventControllerTest {

	@Mock
	private EventRepository eventRepository;

	private EventController eventController;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		eventController = new EventController(eventRepository);
	}

	@Test
	public void testGetAllEvents() {
		Mockito.when(eventRepository.findAll()).thenReturn(new ArrayList<>());
		List<Event> events = eventController.getAllEvents();
		assertNotNull(events);
		assertTrue(events.isEmpty());
	}
}
