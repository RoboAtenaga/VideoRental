package com.rensource.videorental.service.impl;

import com.rensource.videorental.constants.VideoTypeEnum;
import com.rensource.videorental.entity.Video;
import com.rensource.videorental.repository.VideoRepository;
import com.rensource.videorental.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author r.atenga
 */
@Service
public class VideoServiceImpl implements VideoService {

    private static final Logger LOG = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    private VideoRepository repository;

    @Override
    public void loadDummyData() {
        List<Video> videos = new ArrayList<>();
        Video video = new Video("Moana", VideoTypeEnum.CHILDREN.getType(), "Comedy", 12, 0);
        videos.add(video);
        video = new Video("Smurfs", VideoTypeEnum.CHILDREN.getType(), "Drama", 15, 0);
        videos.add(video);
        video = new Video("Titanic", VideoTypeEnum.REGULAR.getType(), "Romance", 0, 0);
        videos.add(video);
        video = new Video("Wrong Turn", VideoTypeEnum.REGULAR.getType(), "Horror", 0, 0);
        videos.add(video);
        video = new Video("Face-off", VideoTypeEnum.NEW.getType(), "Action", 0, 2020);
        videos.add(video);
        video = new Video("Baby Driver", VideoTypeEnum.NEW.getType(), "Action", 0, 2021);
        videos.add(video);

        try {
            // save to db
            repository.saveAll(videos);
        }catch (Exception e){
            LOG.error("loadDummyData error: " + e);
        }
    }

    @Override
    public List<Video> getAllVideos(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Video> videos = repository.findAll(pageable);
        if(videos!= null && videos.hasContent()){
            return videos.getContent();
        }
        return null;
    }

    @Override
    public Video getVideoByTitle(String title) {
        return repository.findVideoByTitleEquals(title);
    }
}
