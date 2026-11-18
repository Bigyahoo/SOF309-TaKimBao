package com.vstay.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(nullable = false)
    private Long price;

    private Long oldPrice;

    private Integer capacity;

    private String bedType;

    private Double area;

    @Column(length = 500)
    private String image;

    @Column(length = 1000)
    private String amenities;

    @Builder.Default
    private Boolean available = true;
}
