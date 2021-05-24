package com.rensource.videorental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

/**
 * @author r.atenga
 */
@Entity
@Table(name = "videos")
public class Video extends BaseAuditableModel{

    @NonNull
    @Column(name = "title", unique = true)
    private String title;

    @NonNull
    private String type;

    @NonNull
    private String genre;

    @NonNull
    @Column(name = "maximum_age")
    private int maximumAge = 0;

    @NonNull
    @Column(name = "year_released")
    private int yearReleased = 0;

    @JsonIgnore
    @OneToMany(mappedBy="video")
    private Set<Rental> rentals;

    public Video(String title, String type, String genre, Integer maximumAge, Integer yearReleased) {
        this.title = title;
        this.type = type;
        this.genre = genre;
        this.maximumAge = maximumAge;
        this.yearReleased = yearReleased;
    }

    public Video() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Video{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", maximumAge='").append(maximumAge).append('\'');
        sb.append(", yearReleased='").append(yearReleased).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
