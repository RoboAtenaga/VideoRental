package com.rensource.videorental.service;

import com.rensource.videorental.dto.RentalRequestDto;
import com.rensource.videorental.dto.RentalResponseDto;
import com.rensource.videorental.entity.User;
import com.rensource.videorental.entity.Video;

import java.util.List;

/**
 * @author r.atenga
 */
public interface RentalService {
    RentalResponseDto newRental(RentalRequestDto requestDto, User user, Video video);
    List<RentalResponseDto> rentalHistory(String filter, int page, int size);
}
