package com.poly.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private String hotelName;

    private String roomType;

    @Column(nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String guestEmail;

    private String guestPhone;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    private Integer nights;

    @Column(nullable = false)
    private Long totalPrice;

    @Builder.Default
    private Integer vPointsEarned = 0;

    private String paymentMethod;

    @Column(length = 500)
    private String specialRequests;

    @Column(length = 500)
    private String specialNotes;

    @Builder.Default
    private String paymentStatus = "PENDING";

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (bookingCode == null) {
            bookingCode = "VS" + System.currentTimeMillis();
        }
    }
}
