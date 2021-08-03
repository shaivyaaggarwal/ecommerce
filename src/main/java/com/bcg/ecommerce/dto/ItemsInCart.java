package com.bcg.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemsInCart {

    @JsonProperty("watches")
    List<Watch> watches;
}
