package com.bcg.ecommerce.controller;

import com.bcg.ecommerce.service.EcommerceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ecommerce")
public class EcommerceController {

    private final EcommerceService ecommerceService;

    @PostMapping(path = "/checkout", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getTotalPriceOfItemsInCart(@NonNull @RequestBody List<String> idsOfWatches) {
        return ecommerceService.getTotalPriceOfItemsInCart(idsOfWatches);
    }
}
