package org.example.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class BookingDtoRs {
    private String number;
    private String roomName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
}
