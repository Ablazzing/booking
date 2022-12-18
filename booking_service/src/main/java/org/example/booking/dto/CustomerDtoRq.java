package org.example.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDtoRq {
    private String email;
    private String name;
}
