package com.bideafactory.house_reservation.service;

import com.bideafactory.house_reservation.dto.DiscountRequest;
import com.bideafactory.house_reservation.dto.DiscountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Service
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Value("${discount.api.url}")
    private String discountApiUrl;

    @Autowired(required = true)
    private final RestTemplate restTemplate;

    public DiscountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public DiscountResponse validateDiscount(DiscountRequest discountRequest) throws URISyntaxException {
        URI uri = UriComponentsBuilder.fromHttpUrl(discountApiUrl)
                .build()
                .toUri();

        long startTime = System.nanoTime();
        try {
            logger.info("Calling discount API for userId: {}, houseId: {}, discountCode: {}",
                    discountRequest.getUserId(), discountRequest.getHouseId(), discountRequest.getDiscountCode());
            DiscountResponse response = restTemplate.postForObject(uri, discountRequest, DiscountResponse.class);
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            logger.info("Discount API response received. Time taken: {} ms", duration);
            return response;
        } catch (Exception e) {
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            logger.error("Error calling discount API. Time taken: {} ms", duration, e);

            if (duration > 5000) {
                throw new RuntimeException("TimeoutException");
            } else {
                throw e;
            }
        }
    }
}