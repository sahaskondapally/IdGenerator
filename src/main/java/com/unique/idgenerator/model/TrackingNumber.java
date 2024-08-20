package com.unique.idgenerator.model;

import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a tracking number entity in the Cassandra database.
 * This entity stores information about a tracking number, including its associated metadata.
 */
@Table("tracking_numbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingNumber {

    @PrimaryKey
    private String trackingNumber;
    private String originCountryId;
    private String destinationCountryId;
    private double weight;
    private String createdAt;
    private UUID customerId;
    private String customerName;
    private String customerSlug;

    // Additional methods (if any) can be added here
}
