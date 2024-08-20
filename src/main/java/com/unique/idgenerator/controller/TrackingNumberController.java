package com.unique.idgenerator.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unique.idgenerator.service.TrackingNumberService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller to handle API requests for generating tracking numbers.
 * Provides an endpoint to generate and return a unique tracking number.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrackingNumberController {

    private final TrackingNumberService trackingNumberService;

    /**
     * API endpoint to generate a unique tracking number.
     * Accepts several query parameters and returns a JSON response containing the generated tracking number.
     *
     * @param originCountryId     The origin country code in ISO 3166-1 alpha-2 format.
     * @param destinationCountryId The destination country code in ISO 3166-1 alpha-2 format.
     * @param weight              The order's weight in kilograms.
     * @param createdAt           The order's creation timestamp in RFC 3339 format.
     * @param customerId          The customer's UUID.
     * @param customerName        The customer's name.
     * @param customerSlug        The customer's slug 
     * @return A ResponseEntity containing a map with the tracking number and creation timestamp.
     */
    @GetMapping("/next-tracking-number")
    public ResponseEntity<Map<String, String>> generateTrackingNumber(
            @RequestParam String originCountryId,
            @RequestParam String destinationCountryId,
            @RequestParam double weight,
            @RequestParam String createdAt,
            @RequestParam UUID customerId,
            @RequestParam String customerName,
            @RequestParam String customerSlug) {

        // Call the service to generate a unique tracking number.
        String trackingNumber = trackingNumberService.generateTrackingNumber(originCountryId, destinationCountryId, weight, createdAt, customerId, customerName, customerSlug);

        // Prepare the response map.
        Map<String, String> response = new HashMap<>();
        response.put("tracking_number", trackingNumber);
        response.put("created_at", createdAt);

        // Return the response entity with HTTP status 200.
        return ResponseEntity.ok(response);
    }
}
