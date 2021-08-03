package com.bcg.ecommerce.service;

import com.bcg.ecommerce.dto.Watch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class EcommerceServiceTest {

    @InjectMocks
    private EcommerceService ecommerceService;

    private Watch watchData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        watchData = new Watch("001","Rolex",100.0, 200.0, 3L);
    }

    @Test
    public void testGetTotalPriceOfItemsInCartSuccess() {
        List<String> listOfIds = Arrays.asList("001", "001", "001", "001", "001", "003", "003", "003", "003", "003", "003");

        ResponseEntity response = ecommerceService.getTotalPriceOfItemsInCart(listOfIds);
        assertTrue(response.getStatusCode().toString().equals("200 OK"));
        assertTrue(response.getBody().toString().contains("{price=700.0}"));
    }
}
