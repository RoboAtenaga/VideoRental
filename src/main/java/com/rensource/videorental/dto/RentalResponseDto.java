package com.rensource.videorental.dto;

import java.time.LocalDate;

public class RentalResponseDto {
    private String user;
    private String video;
    private Integer daysRented;
    private Double price;
    private LocalDate createdDate;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getDaysRented() {
        return daysRented;
    }

    public void setDaysRented(Integer daysRented) {
        this.daysRented = daysRented;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public RentalResponseDto(String user, String video, Integer daysRented, Double price, LocalDate createdDate) {
        this.user = user;
        this.video = video;
        this.daysRented = daysRented;
        this.price = price;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RentalResponseDto{");
        sb.append("user='").append(user).append('\'');
        sb.append(", video='").append(video).append('\'');
        sb.append(", daysRented=").append(daysRented);
        sb.append(", price=").append(price);
        sb.append(", createdDate=").append(createdDate);
        sb.append('}');
        return sb.toString();
    }
}
