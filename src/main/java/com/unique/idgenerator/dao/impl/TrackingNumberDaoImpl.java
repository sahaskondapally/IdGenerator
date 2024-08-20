package com.unique.idgenerator.dao.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.unique.idgenerator.dao.TrackingNumberDao;
import com.unique.idgenerator.model.TrackingNumber;
import com.unique.idgenerator.repository.TrackingNumberRepository;

@Repository
public class TrackingNumberDaoImpl implements TrackingNumberDao {

    private final TrackingNumberRepository trackingNumberRepository;
    private final RedisTemplate<String, TrackingNumber> redisTemplate;

    @Autowired
    public TrackingNumberDaoImpl(TrackingNumberRepository trackingNumberRepository, RedisTemplate<String, TrackingNumber> redisTemplate) {
        this.trackingNumberRepository = trackingNumberRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveToDatabase(TrackingNumber trackingNumber) {
        trackingNumberRepository.save(trackingNumber);
    }

    @Override
    public void saveToRedis(TrackingNumber trackingNumberEntity) {
        // Cache the tracking number in Redis with a 5-day expiration
        redisTemplate.opsForValue().set(trackingNumberEntity.getTrackingNumber(), trackingNumberEntity, 5, TimeUnit.DAYS);
    }
}
