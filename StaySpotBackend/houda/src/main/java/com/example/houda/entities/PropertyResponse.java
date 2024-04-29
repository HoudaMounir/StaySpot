package com.example.houda.entities;

public class PropertyResponse {
    private int id;
    private String name;
    private String location;
    private Integer nbrBathrooms;
    private Integer nbrBedrooms;
    private Long price;
    private Long surface;
    private String description;
    private String imagesFileName;
    private byte[] imageData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNbrBathrooms() {
        return nbrBathrooms;
    }

    public void setNbrBathrooms(Integer nbrBathrooms) {
        this.nbrBathrooms = nbrBathrooms;
    }

    public Integer getNbrBedrooms() {
        return nbrBedrooms;
    }

    public void setNbrBedrooms(Integer nbrBedrooms) {
        this.nbrBedrooms = nbrBedrooms;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagesFileName() {
        return imagesFileName;
    }

    public void setImagesFileName(String imagesFileName) {
        this.imagesFileName = imagesFileName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
