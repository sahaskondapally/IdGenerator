package com.unique.idgenerator.service;

import org.springframework.stereotype.Component;

import com.unique.idgenerator.util.IdFormatter;
import com.unique.idgenerator.util.NodeIdentifier;
import com.unique.idgenerator.util.TimeUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SequenceGenerator {

	private static final int SEQUENCE_BITS = 12;
	private static final int UNUSED_BITS = 22;

	private static final int maxSequence = (int) (Math.pow(2, SEQUENCE_BITS) - 1);

	private volatile long lastTimestamp = -1L;
	private volatile long sequence = 0L;

	private final NodeIdentifier nodeIdentifier;
	private final TimeUtil timeUtil;
	private final IdFormatter idFormatter;

	public SequenceGenerator(NodeIdentifier nodeIdentifier, TimeUtil timeUtil, IdFormatter idFormatter) {
		this.nodeIdentifier = nodeIdentifier;
		this.timeUtil = timeUtil;
		this.idFormatter = idFormatter;
	}

	public synchronized String nextId() {
		int nodeId = nodeIdentifier.generateNodeId();
		long currentTimestamp = timeUtil.getCurrentTimestamp();

		if (currentTimestamp < lastTimestamp) {
			throw new IllegalStateException("Invalid System Clock!");
		}

		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & maxSequence;
			if (sequence == 0) {
				currentTimestamp = timeUtil.waitForNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = currentTimestamp;

		long idHigh = currentTimestamp;
		long idLow = (nodeId << SEQUENCE_BITS) | sequence;

		return idFormatter.formatId(idLow, idHigh);
	}
}
