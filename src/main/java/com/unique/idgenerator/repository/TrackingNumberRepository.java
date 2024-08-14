package com.unique.idgenerator.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.unique.idgenerator.model.TrackingNumber;

@Repository
public interface TrackingNumberRepository extends CassandraRepository<TrackingNumber, String> {
}
