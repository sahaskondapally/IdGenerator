package com.unique.idgenerator.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.unique.idgenerator.model.TrackingNumber;
import com.unique.idgenerator.repository.TrackingNumberRepository;
import com.unique.idgenerator.service.SequenceGenerator;
import com.unique.idgenerator.service.TrackingNumberService;

@Service
public class TrackingNumberServiceImpl implements TrackingNumberService {

	private final SequenceGenerator sequenceGenerator;
	private final TrackingNumberRepository trackingNumberRepository;
	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public TrackingNumberServiceImpl(SequenceGenerator sequenceGenerator,
			TrackingNumberRepository trackingNumberRepository, RedisTemplate<String, String> redisTemplate) {
		this.sequenceGenerator = sequenceGenerator;
		this.trackingNumberRepository = trackingNumberRepository;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String generateUniqueId() {
		String uniqueId = sequenceGenerator.nextId();

		// Persist to Cassandra
		TrackingNumber trackingNumber = new TrackingNumber(uniqueId);
		trackingNumberRepository.save(trackingNumber);

		// Persist to Redis with 5 days of expiry
		redisTemplate.opsForValue().set(uniqueId, uniqueId, 5, TimeUnit.DAYS);

		return uniqueId;
	}
}
