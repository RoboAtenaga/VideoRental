package com.rensource.videorental.controller;

import com.rensource.videorental.dto.LoginRequestDto;
import com.rensource.videorental.dto.RentalRequestDto;
import com.rensource.videorental.dto.RentalResponseDto;
import com.rensource.videorental.dto.ResponseDto;
import com.rensource.videorental.entity.User;
import com.rensource.videorental.entity.Video;
import com.rensource.videorental.service.RentalService;
import com.rensource.videorental.service.UserService;
import com.rensource.videorental.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author r.atenga
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/index")
    public String index() {
        return "Video Rental Service";
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto req){
        //@Todo: move loading dummy data
        if(userService.isEmpty()){
            userService.loadDummyData();
            videoService.loadDummyData();
        }
        ResponseDto responseDto = new ResponseDto();
        User user = userService.findUserByName(req.getUsername());
        if(user != null){
            if(passwordEncoder.matches(req.getPassword(), user.getPassword())){
                responseDto.setResponseCode(ResponseDto.Status.SUCCESS.code);
                responseDto.setResponseMessage("success");
                return ResponseEntity.ok(responseDto);
            }
        }
        responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
        responseDto.setResponseMessage("invalid credentials");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @GetMapping("/video/list")
    public ResponseEntity<ResponseDto> videoList(@RequestParam("page") Optional<Integer> page,
                                            @RequestParam("size") Optional<Integer> size) {
        ResponseDto responseDto = new ResponseDto();

        int nPage = page.orElse(0);
        int nSize = size.orElse(5);

        List<Video> videos = videoService.getAllVideos(nPage, nSize);
        if(videos != null && !videos.isEmpty()){
            responseDto.setResponseCode(ResponseDto.Status.SUCCESS.code);
            responseDto.setResponseMessage("success");
            responseDto.setData(videos);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }else {
            responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
            responseDto.setResponseMessage("No videos available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }

    @PostMapping("/rental/price")
    public ResponseEntity<ResponseDto> calculatePrice(@RequestBody RentalRequestDto req){
        ResponseDto responseDto = new ResponseDto();

        // check if user exists
        User user = userService.findUserByName(req.getUsername());
        if(user == null){
            responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
            responseDto.setResponseMessage("User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        // check if video exists
        Video video = videoService.getVideoByTitle(req.getVideoTitle());
        if(video == null){
            responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
            responseDto.setResponseMessage("Video does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        RentalResponseDto rental = rentalService.newRental(req, user, video);
        if(rental == null){
            responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
            responseDto.setResponseMessage("Server error, please try again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto.setResponseCode(ResponseDto.Status.SUCCESS.code);
        responseDto.setResponseMessage("success");
        responseDto.setData(rental);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rental/history")
    public ResponseEntity<ResponseDto> rentalHistory(@RequestParam("page") Optional<Integer> page,
                                                     @RequestParam("size") Optional<Integer> size,
                                                     @RequestParam("filter") Optional<String> filter) {
        ResponseDto responseDto = new ResponseDto();

        String nFilter = filter.orElse(null);

        int nPage = page.orElse(0);
        int nSize = size.orElse(5);

        List<RentalResponseDto> rentals = rentalService.rentalHistory(nFilter, nPage, nSize);
        if(rentals != null && !rentals.isEmpty()){
            responseDto.setResponseCode(ResponseDto.Status.SUCCESS.code);
            responseDto.setResponseMessage("success");
            responseDto.setData(rentals);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }else {
            responseDto.setResponseCode(ResponseDto.Status.FAILURE.code);
            responseDto.setResponseMessage("No rental history");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }
}
