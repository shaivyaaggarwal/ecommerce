package com.bcg.ecommerce.service;

import com.bcg.ecommerce.dto.Response;
import com.bcg.ecommerce.dto.Watch;
import com.bcg.ecommerce.dto.ItemsInCart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class EcommerceService {

    Logger logger = LoggerFactory.getLogger(EcommerceService.class);
    private List<Watch> watchesData = new ArrayList<>();

    public EcommerceService() {
        initListOfWatches();
    }

    public ResponseEntity getTotalPriceOfItemsInCart(List<String> idsOfWatchesInTheCart) {
        if (idsOfWatchesInTheCart.isEmpty()) {
            return new ResponseEntity<>(createResponse(Collections.emptyMap(), "There are no items in the cart"), HttpStatus.BAD_REQUEST);
        } else {
            try {
                return new ResponseEntity<>(createResponse(Map.of("price",calculateFinalPriceForWatches(idsOfWatchesInTheCart)), "SUCCESS"), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(createResponse(Collections.emptyMap(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private Response createResponse(Map body, String message){
        Response response = new Response();
        response.setBody(body);
        response.setMessage(message);
        return response;
    }

    private Double calculateFinalPriceForWatches(List<String> idsOfWatches) throws Exception {
        Double finalPrice = 0.0;
        Map<String, Integer> itemsToBuy = new HashMap<>();

        for (String id : idsOfWatches) {
            itemsToBuy.put(id, itemsToBuy.getOrDefault(id, 0) + 1);
        }

        Iterator iterator = itemsToBuy.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            finalPrice += getPriceAfterDiscount((String) pair.getKey(), (Integer) pair.getValue());
        }

        return finalPrice;
    }

    private Double getPriceAfterDiscount(String id, Integer count) throws Exception {
        Double priceAfterDiscount = 0.0;
        Watch watchData = watchesData.stream().filter(watch -> watch.getWatchId().equals(id)).findFirst().orElse(null);

        try {
            if (watchData.getMinDiscountQuantity() != null && watchData.getDiscountPrice() != null) {
                Long discountedWatches = (count >= watchData.getMinDiscountQuantity()) ? count / watchData.getMinDiscountQuantity() : 0;
                Long nonDiscountedWatches = count % watchData.getMinDiscountQuantity();
                priceAfterDiscount += watchData.getDiscountPrice() * discountedWatches;
                priceAfterDiscount += watchData.getUnitPrice() * nonDiscountedWatches;
            } else {
                priceAfterDiscount += watchData.getUnitPrice() * count;
            }
        } catch (NullPointerException e) {
            throw new Exception("No watch found in the database with the id: " + id);
        }

        return priceAfterDiscount;
    }

    private void initListOfWatches() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemsInCart watches = mapper.readValue(FileUtils.readFileToString("com.bcg.ecommerce/inventory/watchData.json"), ItemsInCart.class);
            for (Watch watch : watches.getWatches()) {
                watchesData.add(watch);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error while processing JSON: " + e.getMessage());
        }
    }
}
