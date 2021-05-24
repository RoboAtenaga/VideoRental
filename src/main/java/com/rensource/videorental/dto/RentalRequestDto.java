package com.rensource.videorental.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RentalRequestDto {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Video title is required")
    private String videoTitle;
    @NotNull(message = "Number of days is required")
    private Integer numberOfDays;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RentalRequestDto{");
        sb.append("username='").append(username).append('\'');
        sb.append(", videoTitle='").append(videoTitle).append('\'');
        sb.append(", numberOfDays=").append(numberOfDays);
        sb.append('}');
        return sb.toString();
    }
}
