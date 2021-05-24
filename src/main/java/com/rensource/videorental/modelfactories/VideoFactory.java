package com.rensource.videorental.modelfactories;

import com.github.heywhy.springentityfactory.contracts.FactoryHelper;
import com.github.heywhy.springentityfactory.contracts.ModelFactory;
import com.github.javafaker.Faker;
import com.rensource.videorental.entity.Video;

import java.util.UUID;


public class VideoFactory implements FactoryHelper<Video> {
    @Override
    public Class<Video> getEntity() {
        return Video.class;
    }

    @Override
    public Video apply(Faker faker, ModelFactory factory) {
        Video video = new Video();
        video.setTitle(UUID.randomUUID().toString());
        video.setGenre(UUID.randomUUID().toString());
        video.setType(UUID.randomUUID().toString());
        video.setMaximumAge(20);
        video.setYearReleased(2020);
        return video;
    }
}
