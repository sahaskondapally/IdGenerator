package com.unique.idgenerator.service;

import java.util.UUID;

public interface TrackingNumberService {
	public String generateTrackingNumber(String originCountryId, String destinationCountryId, double weight,
			String createdAt, UUID customerId, String customerName, String customerSlug);
}
