package com.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.application.exception.FileProcessingException;
import com.application.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogFileProcessorServiceTest {

	@Mock
	private EventRepository eventRepository;
	
	private ObjectMapper mapper;
	
	private LogFileProcessorService logFileProcessorService;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		mapper = new ObjectMapper();
		logFileProcessorService = new LogFileProcessorService(eventRepository, mapper);
	}
	
	@Test
	public void testProcessLogEvents() throws FileProcessingException {
		String path = "src/test/resources/logfile.txt";
		Mockito.when(eventRepository.saveAll(Mockito.anyIterable())).thenReturn(new ArrayList<>());
		Exception ex = null;
		try {
			logFileProcessorService.processLogEvents(path);
		} catch (Exception e) {
			ex = e;
		}
		assertNull(ex);
	}
	
	@Test
	public void testProcessLogEventsWithInvalidPath() throws FileProcessingException {
		String path = "abc";
		Mockito.when(eventRepository.saveAll(Mockito.anyIterable())).thenReturn(new ArrayList<>());
		Exception ex = null;
		try {
			logFileProcessorService.processLogEvents(path);
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
	}
	
	@Test
	public void testProcessLogEventsWithFailureInDatabaseSave() throws FileProcessingException {
		String path = "src/test/resources/logfile.txt";
		Mockito.when(eventRepository.saveAll(Mockito.anyIterable())).thenThrow(IllegalArgumentException.class);
		Exception ex = null;
		try {
			logFileProcessorService.processLogEvents(path);
		} catch (Exception e) {
			ex = e;
		}
		assertNull(ex);
	}
	
	@Test
	public void testProcessLogEventsForInvalidFile() throws FileProcessingException {
		String path = "src/test/resources/invalid_logfile1.txt";
		Mockito.when(eventRepository.findAll()).thenReturn(new ArrayList<>());
		Exception ex = null;
		try {
			logFileProcessorService.processLogEvents(path);
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
	}
	
	@Test
	public void testProcessLogEventsForInvalidFileAndJsonParseError() throws FileProcessingException {
		String path = "src/test/resources/invalid_logfile2.txt";
		Mockito.when(eventRepository.findAll()).thenReturn(new ArrayList<>());
		Exception ex = null;
		try {
			logFileProcessorService.processLogEvents(path);
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
	}
}
