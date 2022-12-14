package org.example.booking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"bookings"})
@EqualsAndHashCode(exclude = {"bookings"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = false, nullable = false)
    @NonNull
    private String name;

    @Column(unique = true, nullable = false)
    @NonNull
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;
}
