package com.unique.idgenerator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unique.idgenerator.service.TrackingNumberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IdController {

	private final TrackingNumberService trackingNumberService;

	@GetMapping("/generate-id")
	public ResponseEntity<String> generateId() {
		String uniqueId = trackingNumberService.generateUniqueId();
		return new ResponseEntity<>(uniqueId, HttpStatus.OK);
	}
}
