package com.unique.idgenerator.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeUtil {

	private static final long CUSTOM_TIME_IN_MILLI = 1704067200000L; // January 1, 2024

	public long getCurrentTimestamp() {
		return Instant.now().toEpochMilli() - CUSTOM_TIME_IN_MILLI;
	}

	public long waitForNextMillis(long lastTimestamp) {
		long currentTimestamp = getCurrentTimestamp();
		while (currentTimestamp == lastTimestamp) {
			currentTimestamp = getCurrentTimestamp();
		}
		return currentTimestamp;
	}
}
