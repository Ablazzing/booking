package org.example.booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"room", "customer"})
@EqualsAndHashCode(exclude = {"room", "customer"})
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    @NonNull
    private String number;

    @Column(nullable = false)
    @NonNull
    private LocalDate startDate;

    @Column(nullable = false)
    @NonNull
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NonNull
    @JsonIgnore
    private Room room;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NonNull
    @JsonIgnore
    private Customer customer;
}
