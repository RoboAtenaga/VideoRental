package com.rensource.videorental.service.impl;

import com.rensource.videorental.dto.RentalRequestDto;
import com.rensource.videorental.dto.RentalResponseDto;
import com.rensource.videorental.entity.Rental;
import com.rensource.videorental.entity.User;
import com.rensource.videorental.entity.Video;
import com.rensource.videorental.repository.RentalRepository;
import com.rensource.videorental.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author r.atenga
 */
@Service
public class RentalServiceImpl implements RentalService {

    private static final Logger LOG = LoggerFactory.getLogger(RentalServiceImpl.class);

    @Autowired
    private RentalRepository repository;

    @Override
    public RentalResponseDto newRental(RentalRequestDto requestDto, User user, Video video) {
        try{
            if(requestDto == null || user == null || video == null){
                return null;
            }

            double price = 0;
            // check video type to calculate price
            switch (video.getType()){
                case "Regular":
                    price = 10 * requestDto.getNumberOfDays();
                    break;
                case "Children's movie":
                    price = (8 * requestDto.getNumberOfDays()) + video.getMaximumAge()/2;
                    break;
                case "New Release":
                    int yearDifference = LocalDateTime.now().getYear() - video.getYearReleased();
                    price = (15 * requestDto.getNumberOfDays()) - yearDifference;
                    break;
                default:
                    return null;
            }

            Rental rental = new Rental();
            rental.setDaysRented(requestDto.getNumberOfDays());
            rental.setPrice(price);
            rental.setUser(user);
            rental.setVideo(video);
            repository.save(rental);

            RentalResponseDto rentalResponse = new RentalResponseDto(user.getUsername(), video.getTitle(),
                    requestDto.getNumberOfDays(), price, LocalDate.now());
            return rentalResponse;
        }catch (Exception e){
            LOG.error("Calculate Price error: " + e);
            return null;
        }
    }

    @Override
    public List<RentalResponseDto> rentalHistory(String filter, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            String username = null, createdDate = null;
            if(filter != null && filter.trim() != null){
                LOG.info("filter " + filter);
                // check if filter contains both username and date
                if(filter.contains("username") && filter.contains("date")){
                    String[] filterSplit = filter.split(",");
                    username = filterSplit[0].split(":")[1];
                    createdDate = filterSplit[1].split(":")[1];
                }else if(filter.contains("username")) {
                    String[] filterSplit = filter.split(":");
                    username = filterSplit[1];
                    createdDate = null;
                }else if(filter.contains("date")){
                    String[] filterSplit = filter.split(":");
                    username = null;
                    createdDate = filterSplit[1];
                }
            }

            List<Rental> rentals = null;
            List<RentalResponseDto> rentalResponseDtos;
            if(username != null && createdDate != null){
                //convert createdDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate ld = LocalDate.parse(createdDate, formatter);
                rentals = repository.findAllWithCreatedDateAndUsername(ld, username, pageable);
            }else if(username != null){
                rentals = repository.findAllWithUsername(username, pageable);
            }else if(createdDate != null){
                //convert createdDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate ld = LocalDate.parse(createdDate, formatter);
                rentals = repository.findAllWithCreatedDate(ld, pageable);
            }else {
                rentals = repository.findAllOrderByUsername(pageable);
            }

            if(rentals != null && !rentals.isEmpty()){
                rentalResponseDtos = new ArrayList<>();
                for (Rental rental : rentals) {
                    RentalResponseDto rentalResponseDto = new RentalResponseDto(rental.getUser().getUsername(),
                            rental.getVideo().getTitle(), rental.getDaysRented(), rental.getPrice(),
                            rental.getCreationDate());
                    rentalResponseDtos.add(rentalResponseDto);
                }
                return rentalResponseDtos;
            }
            return null;
        }catch (Exception e){
            LOG.error("rentalHistory error: " + e);
            return null;
        }
    }
}
