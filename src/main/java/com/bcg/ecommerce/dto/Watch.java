package com.bcg.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Watch {

    @JsonProperty("watch_id")
    private String watchId;

    @JsonProperty("watch_name")
    private String watchName;

    @JsonProperty("unit_price")
    private Double unitPrice;

    @JsonProperty("discount_price")
    private Double discountPrice;

    @JsonProperty("min_discount_quantity")
    private Long minDiscountQuantity;
}
