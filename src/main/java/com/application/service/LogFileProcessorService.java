package com.application.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.application.converter.EventDataToEntityConverter;
import com.application.entity.Event;
import com.application.exception.FileProcessingException;
import com.application.repository.EventRepository;
import com.application.view.EventData;
import com.application.view.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogFileProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogFileProcessorService.class);

	private final EventRepository eventRepository;

	private final ObjectMapper objectMapper;

	public LogFileProcessorService(EventRepository eventRepository, ObjectMapper objectMapper) {
		this.eventRepository = eventRepository;
		this.objectMapper = objectMapper;
	}

	public void processLogEvents(String logFileLocation) throws FileProcessingException {
		//String fileLocation = getLogFileLocation(logFileLocation);
		List<String> linesOfFile = getLinesOfFile(logFileLocation);
		List<String> invalidLines = new ArrayList<>();
		List<EventData> events = linesOfFile.parallelStream().map(line -> {
			try {
				return createDTO(line);
			} catch (FileProcessingException e) {
				invalidLines.add(line);
				return null;
			}
		}).collect(Collectors.toList());
		validate(events, invalidLines);
		Map<String, List<EventData>> eventsDataMap = events.parallelStream().filter(Objects::nonNull)
				.collect(Collectors.groupingBy(EventData::getId));
		validate(eventsDataMap);
		Map<String, Long> eventDurationMap = eventsDataMap.entrySet().parallelStream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> calculateDuration(entry.getValue())));
		logLongEvents(eventDurationMap);
		saveEventsToDatabase(eventsDataMap, eventDurationMap);
		LOGGER.info("File Processed!!");
	}

	private void saveEventsToDatabase(Map<String, List<EventData>> eventsDataMap, Map<String, Long> eventDurationMap) {
		LOGGER.info("Saving to databse");
		List<Event> events = EventDataToEntityConverter.convert(eventsDataMap, eventDurationMap);
		try {
			eventRepository.saveAll(events);
			LOGGER.info("Events persisted to database");
			LOGGER.info("See details of saved events at: http://localhost:8085/events");
		} catch (Exception ex) {
			LOGGER.error("Error occured while saving events to database");
		}
	}

	private void validate(List<EventData> events, List<String> invalidLines) throws FileProcessingException {
		boolean hasInvalidEventData = false;
		if (!invalidLines.isEmpty()) {
			LOGGER.info("Invalid lines in file: " + invalidLines);
			hasInvalidEventData = true;
		} else {
			hasInvalidEventData = events.parallelStream().filter(Objects::nonNull).anyMatch(event -> Objects.isNull(event.getId())
					|| Objects.isNull(event.getState()) || Objects.isNull(event.getTimestamp()));
		}
		if (hasInvalidEventData) {
			throw new FileProcessingException("File is invalid. There are invalid events in file.");
		}
	}

	private void validate(Map<String, List<EventData>> eventsDataMap) throws FileProcessingException {
		List<String> invalidEvents = eventsDataMap.entrySet().parallelStream()
				.filter(entry -> (entry.getValue().size() != 2) || !(entry.getValue().parallelStream()
						.anyMatch(data -> State.STARTED.equals(data.getState()))
						&& entry.getValue().parallelStream().anyMatch(data -> State.FINISHED.equals(data.getState()))))
				.map(Map.Entry::getKey).collect(Collectors.toList());
		if (!invalidEvents.isEmpty()) {
			LOGGER.error("Invalid events:" + invalidEvents);
			throw new FileProcessingException("File is invalid. There are invalid events in file.");
		}
	}

	private void logLongEvents(Map<String, Long> eventDurationMap) {
		List<String> longEvents = eventDurationMap.entrySet().parallelStream().filter(entry -> entry.getValue() > 4)
				.map(Map.Entry::getKey).collect(Collectors.toList());
		LOGGER.info("Long events::" + longEvents);
	}

	private List<String> getLinesOfFile(String fileLocation) throws FileProcessingException {
		List<String> linesOfFile = null;
		try {
			Path path = Paths.get(fileLocation);
			linesOfFile = Files.readAllLines(path);
		} catch (Exception e) {
			throw new FileProcessingException("Cannot open file: " + fileLocation);
		}
		return linesOfFile;
	}

	/*private String getLogFileLocation(String logFileLocation) {
		if (Objects.isNull(logFileLocation)) {
			return "C:\\Users\\sohit\\Desktop\\CS\\logfile.txt";
			// return "classpath:logfile.txt";
		} else {
			return logFileLocation;
		}
	}*/

	private Long calculateDuration(List<EventData> eventData) {
		return eventData.stream().filter(event -> State.FINISHED.equals(event.getState())).findFirst().get()
				.getTimestamp()
				- eventData.stream().filter(event -> State.STARTED.equals(event.getState())).findFirst().get()
						.getTimestamp();
	}

	private EventData createDTO(String line) throws FileProcessingException {
		try {
			return objectMapper.readValue(line, EventData.class);
		} catch (JsonProcessingException e) {
			throw new FileProcessingException("Error occured in parsing line of file to Java object.");
		}
	}
}
