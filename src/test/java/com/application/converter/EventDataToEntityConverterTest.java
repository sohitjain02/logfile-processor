package com.application.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.application.entity.Event;
import com.application.view.EventData;
import com.application.view.State;

public class EventDataToEntityConverterTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<EventDataToEntityConverter> constructor = EventDataToEntityConverter.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testConvert() {
		List<EventData> eventData1 = new ArrayList<>();
		EventData data1 = new EventData();
		data1.setId("1");
		data1.setState(State.STARTED);
		data1.setTimestamp(1491377495212L);
		EventData data2 = new EventData();
		data2.setId("1");
		data2.setState(State.FINISHED);
		data2.setTimestamp(1491377495217L);
		eventData1.add(data1);
		eventData1.add(data2);

		List<EventData> eventData2 = new ArrayList<>();
		EventData data3 = new EventData();
		data3.setId("2");
		data3.setState(State.STARTED);
		data3.setTimestamp(1491377495212L);
		EventData data4 = new EventData();
		data4.setId("2");
		data4.setState(State.FINISHED);
		data4.setTimestamp(1491377495215L);
		eventData2.add(data3);
		eventData2.add(data4);

		Map<String, List<EventData>> eventsDataMap = new HashMap<>();
		eventsDataMap.put("1", eventData1);
		eventsDataMap.put("2", eventData2);
		Map<String, Long> eventDurationMap = new HashMap<>();
		eventDurationMap.put("1", 5L);
		eventDurationMap.put("2", 3L);
		List<Event> events = EventDataToEntityConverter.convert(eventsDataMap, eventDurationMap);
		assertFalse(events.isEmpty());
	}
}
