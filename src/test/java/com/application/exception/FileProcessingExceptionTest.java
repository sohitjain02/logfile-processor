package com.application.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class FileProcessingExceptionTest {

	@Test
	public void testFileProcessingException() {
		FileProcessingException ex = new FileProcessingException("test exception message");
		assertNotNull(ex);
	}
}
