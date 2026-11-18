package com.poly.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(nullable = false)
    private String city;

    private String address;

    @Column(nullable = false)
    private Long price;

    private Long oldPrice;

    private Integer discountPercent;

    private Integer stars;

    private Double rating;

    private String ratingText;

    private Integer reviewsCount;

    @Column(length = 500)
    private String image;

    private String badge;

    private String badgeSub;

    private String roomType;

    private String urgency;

    private String distance;

    private String taxNote;

    private Integer galleryCount;

    @Column(length = 2000)
    private String description;

    private Boolean featured;

    @Column(length = 1000)
    private String inclusions;

    @Column(length = 1000)
    private String amenities;

    @Column(length = 500)
    private String tags;

    @Column(length = 2000)
    private String gallery;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper methods to get lists from comma-separated strings
    public List<String> getInclusionsList() {
        if (inclusions == null || inclusions.isEmpty()) return List.of();
        return List.of(inclusions.split("\\|"));
    }

    public void setInclusionsList(List<String> list) {
        this.inclusions = list == null ? "" : String.join("|", list);
    }

    public List<String> getAmenitiesList() {
        if (amenities == null || amenities.isEmpty()) return List.of();
        return List.of(amenities.split("\\|"));
    }

    public void setAmenitiesList(List<String> list) {
        this.amenities = list == null ? "" : String.join("|", list);
    }

    public List<String> getTagsList() {
        if (tags == null || tags.isEmpty()) return List.of();
        return List.of(tags.split("\\|"));
    }

    public List<String> getGalleryList() {
        if (gallery == null || gallery.isEmpty()) return List.of();
        return List.of(gallery.split("\\|"));
    }

    public void setGalleryList(List<String> list) {
        this.gallery = list == null ? "" : String.join("|", list);
    }
}
