package com.application.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.application.entity.Event;
import com.application.view.EventData;

public class EventDataToEntityConverter {

	private EventDataToEntityConverter() {
	}

	public static List<Event> convert(Map<String, List<EventData>> eventsDataMap, Map<String, Long> eventDurationMap) {
		List<Event> events = new ArrayList<>();
		eventsDataMap.entrySet().parallelStream()
				.forEach(entry -> events.add(getEvent(entry.getValue().get(0), eventDurationMap.get(entry.getKey()))));
		return events;
	}

	private static Event getEvent(EventData eventData, Long duration) {
		boolean alert = false;
		if (duration > 4) {
			alert = true;
		}
		Event event = Event.builder().id(eventData.getId()).duration(duration).host(eventData.getHost())
				.type(eventData.getType()).alert(alert).build();
		return event;
	}
}
