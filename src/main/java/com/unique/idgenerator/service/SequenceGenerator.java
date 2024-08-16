package com.unique.idgenerator.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.unique.idgenerator.util.IdFormatter;
import com.unique.idgenerator.util.NodeIdentifier;
import com.unique.idgenerator.util.TimeUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SequenceGenerator {
	private final TimeUtil timeUtil;
	private final IdFormatter idFormatter;
	private final AtomicLong sequence = new AtomicLong(0);
	private final AtomicLong lastTimestamp = new AtomicLong(-1);

	private final int nodeId; 
	private static final int SEQUENCE_BITS = 12; // Example value, adjust as necessary
	private static final long maxSequence = (1L << SEQUENCE_BITS) - 1;

	public SequenceGenerator(TimeUtil timeUtil, IdFormatter idFormatter) {
		this.timeUtil = timeUtil;
		this.idFormatter = idFormatter;
		this.nodeId = NodeIdentifier.generateNodeId(); 

		log.info("SequenceGenerator initialized with nodeId: {}", nodeId);
	}

	public String nextId() {
		long currentTimestamp = timeUtil.getCurrentTimestamp();
		long lastTime = lastTimestamp.get();

		log.debug("Generating next ID. Current timestamp: {}, Last timestamp: {}", currentTimestamp, lastTime);

		// If the current timestamp is the same as the last one, increment the sequence
		if (currentTimestamp == lastTime) {
			long currentSequence = sequence.incrementAndGet() & maxSequence;

			log.debug("Same timestamp detected. Current sequence: {}", currentSequence);

			// If the sequence reaches its maximum, wait for the next millisecond
			if (currentSequence == 0) {
				log.info("Sequence exhausted, waiting for next millisecond...");
				currentTimestamp = timeUtil.waitForNextMillis(lastTime);
				log.info("Wait complete. New timestamp: {}", currentTimestamp);
			}

			lastTimestamp.set(currentTimestamp);
			String id = generateId(currentTimestamp, currentSequence);
			log.info("Generated ID: {}", id);
			return id;
		}

		// If the current timestamp is different from the last one, reset the sequence
		log.debug("New timestamp detected. Resetting sequence.");
		sequence.set(0);
		lastTimestamp.set(currentTimestamp);
		return generateId(currentTimestamp, 0);

	}

	private String generateId(long timestamp, long sequence) {
		long idHigh = timestamp;
		long idLow = (nodeId << SEQUENCE_BITS) | sequence;
		String formattedId = idFormatter.formatId(idLow, idHigh);
		log.debug("Formatted ID: {}", formattedId);
		return formattedId;

	}
}
