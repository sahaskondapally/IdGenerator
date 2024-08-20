package com.unique.idgenerator.service.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unique.idgenerator.dao.TrackingNumberDao;
import com.unique.idgenerator.model.TrackingNumber;
import com.unique.idgenerator.service.SequenceGenerator;
import com.unique.idgenerator.service.TrackingNumberService;

/**
 * Service class responsible for generating unique tracking numbers.
 * This class handles the business logic of generating a unique tracking number
 * based on input parameters and saving it to both Cassandra and Redis.
 */
@Service
public class TrackingNumberServiceImpl implements TrackingNumberService{

    private final SequenceGenerator sequenceGenerator;
    private final TrackingNumberDao trackingNumberDao;

    /**
     * Constructor to autowire necessary dependencies.
     *
     * @param sequenceGenerator The service responsible for generating sequence numbers.
     * @param trackingNumberDao The DAO layer for accessing Cassandra and Redis.
     */
    @Autowired
    public TrackingNumberServiceImpl(SequenceGenerator sequenceGenerator, TrackingNumberDao trackingNumberDao) {
        this.sequenceGenerator = sequenceGenerator;
        this.trackingNumberDao = trackingNumberDao;
    }

    /**
     * Generates a unique tracking number based on the provided input parameters.
     * The generated tracking number is saved to both Cassandra and Redis.
     *
     * @param originCountryId     The origin country code in ISO 3166-1 alpha-2 format.
     * @param destinationCountryId The destination country code in ISO 3166-1 alpha-2 format.
     * @param weight              The order's weight in kilograms.
     * @param createdAt           The order's creation timestamp in RFC 3339 format.
     * @param customerId          The customer's UUID.
     * @param customerName        The customer's name.
     * @param customerSlug        The customer's slug
     * @return The generated unique tracking number.
     */
    public String generateTrackingNumber(String originCountryId, String destinationCountryId, double weight, String createdAt, UUID customerId, String customerName, String customerSlug) {
        // Generate a unique tracking number based on input parameters.
        String trackingNumber = sequenceGenerator.nextId();

     // Create a TrackingNumber entity using the builder pattern.
        TrackingNumber trackingNumberEntity = TrackingNumber.builder()
            .trackingNumber(trackingNumber)
            .originCountryId(originCountryId)
            .destinationCountryId(destinationCountryId)
            .weight(weight)
            .createdAt(createdAt)
            .customerId(customerId)
            .customerName(customerName)
            .customerSlug(customerSlug)
            .build();

        // Persist the tracking number to Cassandra.
        trackingNumberDao.saveToDatabase(trackingNumberEntity);

        // Cache the tracking number in Redis with an expiration time.
        trackingNumberDao.saveToRedis(trackingNumberEntity);
        return trackingNumber;
    }
}
