package com.unique.idgenerator.dao;

import com.unique.idgenerator.model.TrackingNumber;

public interface TrackingNumberDao {
    /**
     * Saves the tracking number to the database.
     *
     * @param trackingNumber the tracking number to be saved
     */
    void saveToDatabase(TrackingNumber trackingNumber);

    /**
     * Saves the tracking number to Redis with an expiry time.
     *
     * @param key the key to be saved in Redis
     * @param value the value to be saved in Redis
     * @param duration the duration in days for which the key should persist
     */

	void saveToRedis(TrackingNumber trackingNumberEntity);
}
