package com.example.houda.entities;

import com.example.houda.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String location;
    private Integer nbrBathrooms;
    private Integer nbrBedrooms;
    private Long price;
    private Long surface;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String imagesFileName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Property() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNbrBathrooms(Integer nbrBathrooms) {
        this.nbrBathrooms = nbrBathrooms;
    }

    public void setNbrBedrooms(Integer nbrBedrooms) {
        this.nbrBedrooms = nbrBedrooms;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagesFileName(String imagesFileName) {
        this.imagesFileName = imagesFileName;
    }
}
