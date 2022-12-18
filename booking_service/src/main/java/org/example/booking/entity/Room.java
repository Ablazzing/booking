package org.example.booking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"bookings"})
@EqualsAndHashCode(exclude = {"bookings"})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    @NonNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private RoomLevel roomLevel;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;
}
