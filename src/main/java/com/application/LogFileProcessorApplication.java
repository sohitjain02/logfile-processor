package com.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.exception.FileProcessingException;
import com.application.service.LogFileProcessorService;

@SpringBootApplication
public class LogFileProcessorApplication implements CommandLineRunner {

	private final LogFileProcessorService logFileProcessorService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogFileProcessorApplication.class);

	public LogFileProcessorApplication(LogFileProcessorService logFileProcessorService) {
		this.logFileProcessorService = logFileProcessorService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LogFileProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String filePath = null;
		while (true) {
			System.out.print("Enter file path (Press enter to kill the process):: ");
			filePath = br.readLine();
			if (Objects.nonNull(filePath) && filePath.length() > 1) {
				try {
					logFileProcessorService.processLogEvents(filePath);
				} catch (FileProcessingException e) {
					LOGGER.error(e.getMessage());
				}
			} else {
				System.exit(0);
			}
		}
	}

}
