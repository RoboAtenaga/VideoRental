package com.rensource.videorental.controllers;

import com.google.gson.reflect.TypeToken;
import com.rensource.videorental.IntegrationTest;
import com.rensource.videorental.dto.LoginRequestDto;
import com.rensource.videorental.dto.RentalRequestDto;
import com.rensource.videorental.dto.RentalResponseDto;
import com.rensource.videorental.dto.ResponseDto;
import com.rensource.videorental.entity.Rental;
import com.rensource.videorental.entity.User;
import com.rensource.videorental.entity.Video;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiControllerTest extends IntegrationTest {

    @Test
    void testIfApplicationIsRunning() throws Exception {
        MockHttpServletRequestBuilder get = get("/api/index");
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            assertNotNull(response);
            assertEquals("Video Rental Service", response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testLogin() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername(user.getUsername());
        loginRequestDto.setPassword("password");

        MockHttpServletRequestBuilder post = post("/api/login")
                .content(gson.toJson(loginRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isOk()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());
            assertNotNull(responseDto);
            assertEquals("00", responseDto.getResponseCode());
            assertEquals("success", responseDto.getResponseMessage());
        });

    }

    @WithMockUser(username="user")
    @Test
    void testLoginWithInvalidCredentials() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername(user.getUsername());
        loginRequestDto.setPassword("something");

        MockHttpServletRequestBuilder post = post("/api/login")
                .content(gson.toJson(loginRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isBadRequest()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());
            assertNotNull(responseDto);
            assertEquals("99", responseDto.getResponseCode());
            assertEquals("invalid credentials", responseDto.getResponseMessage());
        });

    }

    @WithMockUser(username="user")
    @Test
    void testGetAllVideosReturns5Items() throws Exception {

       modelFactory.pipe(Video.class).create(20);

        MockHttpServletRequestBuilder get = get("/api/video/list");
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            List<Video> videos = (List<Video>) responseDto.getData();

            assertEquals("success", responseDto.getResponseMessage());
            assertEquals("00", responseDto.getResponseCode());
            assertEquals(5, videos.size());
            assertNotNull(response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testCalculatePrice() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("New Release");
            return video;
        }).create();

        RentalRequestDto rentalRequestDto = new RentalRequestDto();
        rentalRequestDto.setUsername(user.getUsername());
        rentalRequestDto.setVideoTitle(v.getTitle());
        rentalRequestDto.setNumberOfDays(5);

        MockHttpServletRequestBuilder post = post("/api/rental/price")
                .content(gson.toJson(rentalRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isOk()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());
            assertNotNull(responseDto);
            assertEquals("00", responseDto.getResponseCode());
        });

    }

    @WithMockUser(username="user")
    @Test
    void testCalculatePriceWithUserNull() throws Exception {

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("New Release");
            return video;
        }).create();

        RentalRequestDto rentalRequestDto = new RentalRequestDto();
        rentalRequestDto.setUsername("Robo");
        rentalRequestDto.setVideoTitle(v.getTitle());
        rentalRequestDto.setNumberOfDays(5);

        MockHttpServletRequestBuilder post = post("/api/rental/price")
                .content(gson.toJson(rentalRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isNotFound()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            assertEquals("99", responseDto.getResponseCode());
            assertEquals("User does not exist", responseDto.getResponseMessage());
        });

    }

    @WithMockUser(username="user")
    @Test
    void testCalculatePriceWithoutVideo() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();


        RentalRequestDto rentalRequestDto = new RentalRequestDto();
        rentalRequestDto.setUsername(user.getUsername());
        rentalRequestDto.setVideoTitle("Jumanji");
        rentalRequestDto.setNumberOfDays(5);

        MockHttpServletRequestBuilder post = post("/api/rental/price")
                .content(gson.toJson(rentalRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isNotFound()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            assertEquals("99", responseDto.getResponseCode());
            assertEquals("Video does not exist", responseDto.getResponseMessage());
        });

    }

    @WithMockUser(username="user")
    @Test
    void testCalculatePriceWrongVideoType() throws Exception {
        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Any type");
            return video;
        }).create();

        RentalRequestDto rentalRequestDto = new RentalRequestDto();
        rentalRequestDto.setUsername(user.getUsername());
        rentalRequestDto.setVideoTitle(v.getTitle());
        rentalRequestDto.setNumberOfDays(5);

        MockHttpServletRequestBuilder post = post("/api/rental/price")
                .content(gson.toJson(rentalRequestDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post)
                .andExpect(status().isInternalServerError()).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            assertEquals("99", responseDto.getResponseCode());
            assertEquals("Server error, please try again", responseDto.getResponseMessage());
        });
    }

    @WithMockUser(username="user")
    @Test
    void testGetRentalHistoryNoFilter() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Regular");
            return video;
        }).create();

        Rental r = modelFactory.pipe(Rental.class).then(rental -> {
            rental.setDaysRented(1);
            rental.setVideo(v);
            rental.setUser(user);
            rental.setPrice(10.0);
            return rental;
        }).create();

        MockHttpServletRequestBuilder get = get("/api/rental/history");
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            List<RentalResponseDto> rentals = (List<RentalResponseDto>) responseDto.getData();

            assertEquals("success", responseDto.getResponseMessage());
            assertEquals("00", responseDto.getResponseCode());
            assertEquals(false, rentals.isEmpty());
            assertNotNull(response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testGetRentalHistoryFilterByUsername() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Regular");
            return video;
        }).create();

        Rental r = modelFactory.pipe(Rental.class).then(rental -> {
            rental.setDaysRented(1);
            rental.setVideo(v);
            rental.setUser(user);
            rental.setPrice(10.0);
            return rental;
        }).create();

        String filter = "username:" + user.getUsername();
        MockHttpServletRequestBuilder get = get("/api/rental/history?filter="+filter);
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            List<RentalResponseDto> rentals = (List<RentalResponseDto>) responseDto.getData();

            assertEquals("success", responseDto.getResponseMessage());
            assertEquals("00", responseDto.getResponseCode());
            assertEquals(false, rentals.isEmpty());

            assertNotNull(response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testGetRentalHistoryFilterByDate() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Regular");
            return video;
        }).create();

        Rental r = modelFactory.pipe(Rental.class).then(rental -> {
            rental.setDaysRented(1);
            rental.setVideo(v);
            rental.setUser(user);
            rental.setPrice(10.0);
            return rental;
        }).create();

        String filter = "date:" + LocalDate.now();
        MockHttpServletRequestBuilder get = get("/api/rental/history?filter="+filter);
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            List<RentalResponseDto> rentals = (List<RentalResponseDto>) responseDto.getData();

            assertEquals("success", responseDto.getResponseMessage());
            assertEquals("00", responseDto.getResponseCode());
            assertEquals(false, rentals.isEmpty());

            assertNotNull(response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testGetRentalHistoryFilterByUsernameAndDate() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Regular");
            return video;
        }).create();

        Rental r = modelFactory.pipe(Rental.class).then(rental -> {
            rental.setDaysRented(1);
            rental.setVideo(v);
            rental.setUser(user);
            rental.setPrice(10.0);
            return rental;
        }).create();

        String filter = "username:" + user.getUsername() + ",date:" + LocalDate.now();
        MockHttpServletRequestBuilder get = get("/api/rental/history?filter="+filter);
        mockMvc.perform(get)
                .andExpect(status().isOk()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            List<RentalResponseDto> rentals = (List<RentalResponseDto>) responseDto.getData();

            assertEquals("success", responseDto.getResponseMessage());
            assertEquals("00", responseDto.getResponseCode());
            assertEquals(false, rentals.isEmpty());

            assertNotNull(response);
        });
    }

    @WithMockUser(username="user")
    @Test
    void testGetRentalHistoryInvalidFilters() throws Exception {

        User user = modelFactory.pipe(User.class).then(u -> {
            u.setPassword(passwordEncoder.encode("password"));
            return u;
        }).create();

        Video v = modelFactory.pipe(Video.class).then(video -> {
            video.setType("Regular");
            return video;
        }).create();

        Rental r = modelFactory.pipe(Rental.class).then(rental -> {
            rental.setDaysRented(1);
            rental.setVideo(v);
            rental.setUser(user);
            rental.setPrice(10.0);
            return rental;
        }).create();

        String filter = "username:somethingRandom,date:2021-02-22";
        MockHttpServletRequestBuilder get = get("/api/rental/history?filter="+filter);
        mockMvc.perform(get)
                .andExpect(status().isNotFound()).andExpect(result -> {

            String response = result.getResponse().getContentAsString();

            ResponseDto responseDto = gson.fromJson(response, new TypeToken<ResponseDto>() {
            }.getType());

            assertEquals("No rental history", responseDto.getResponseMessage());
            assertEquals("99", responseDto.getResponseCode());
            assertEquals(true, responseDto.getData() == null);
            assertNotNull(response);
        });
    }

}
