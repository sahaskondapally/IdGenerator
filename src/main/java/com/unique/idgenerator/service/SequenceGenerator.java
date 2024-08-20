package com.unique.idgenerator.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.unique.idgenerator.util.IdFormatter;
import com.unique.idgenerator.util.NodeIdentifier;
import com.unique.idgenerator.util.TimeUtil;

import lombok.extern.log4j.Log4j2;

/**
 * The {@code SequenceGenerator} class generates unique identifiers based on
 * the current timestamp, a node ID, and a sequence number.
 * <p>
 * This class uses a simple algorithm that combines the current timestamp, a
 * node identifier, and a sequence number to ensure that generated IDs are
 * unique and sequential within the same millisecond.
 * </p>
 * <p>
 * The class is thread-safe, ensuring that IDs generated concurrently will be
 * unique.
 * </p>
 */
@Component
@Log4j2
public class SequenceGenerator {

    private final TimeUtil timeUtil;
    private final IdFormatter idFormatter;
    private final AtomicLong sequence = new AtomicLong(0);
    private final AtomicLong lastTimestamp = new AtomicLong(-1);

    private final int nodeId;
    private static final int SEQUENCE_BITS = 12; // Number of bits allocated for the sequence
    private static final long maxSequence = (1L << SEQUENCE_BITS) - 1; // Maximum value for the sequence

    /**
     * Constructs a new {@code SequenceGenerator} with the specified
     * {@link TimeUtil} and {@link IdFormatter}.
     *
     * @param timeUtil     the utility to retrieve the current timestamp
     * @param idFormatter  the utility to format the generated ID
     */
    public SequenceGenerator(TimeUtil timeUtil, IdFormatter idFormatter) {
        this.timeUtil = timeUtil;
        this.idFormatter = idFormatter;
        this.nodeId = NodeIdentifier.generateNodeId(); // Generates a unique node ID

        log.info("SequenceGenerator initialized with nodeId: {}", nodeId);
    }

    /**
     * Generates the next unique ID.
     * <p>
     * This method generates an ID based on the current timestamp. If the
     * method is called multiple times within the same millisecond, the
     * sequence number is incremented to ensure uniqueness. If the sequence
     * number reaches its maximum value, the method will wait for the next
     * millisecond to continue generating IDs.
     * </p>
     *
     * @return a unique ID as a {@code String}
     */
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

    /**
     * Generates a formatted ID based on the provided timestamp and sequence number.
     *
     * @param timestamp the timestamp part of the ID
     * @param sequence  the sequence number part of the ID
     * @return a formatted ID as a {@code String}
     */
    private String generateId(long timestamp, long sequence) {
        long idHigh = timestamp;
        long idLow = (nodeId << SEQUENCE_BITS) | sequence;
        String formattedId = idFormatter.formatId(idLow, idHigh);
        log.debug("Formatted ID: {}", formattedId);
        return formattedId;
    }
}
