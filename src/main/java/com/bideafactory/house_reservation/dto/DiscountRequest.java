package com.bideafactory.house_reservation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    private String userId;
    private String houseId;
    private String discountCode;
}
