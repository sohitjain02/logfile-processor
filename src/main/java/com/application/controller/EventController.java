package com.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.entity.Event;
import com.application.repository.EventRepository;

@RestController
public class EventController {

	private final EventRepository eventRepository;

	public EventController(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@GetMapping("/events")
	public List<Event> getAllEvents() {
		List<Event> events = (List<Event>) eventRepository.findAll();
		return events;
	}

}
