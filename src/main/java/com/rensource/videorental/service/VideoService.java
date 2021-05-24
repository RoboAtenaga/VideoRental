package com.rensource.videorental.service;

import com.rensource.videorental.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author r.atenga
 */
public interface VideoService {
    void loadDummyData();
    List<Video> getAllVideos(int pageNo, int pageSize);
    Video getVideoByTitle(String title);
}
