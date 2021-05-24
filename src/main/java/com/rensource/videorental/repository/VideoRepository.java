package com.rensource.videorental.repository;

import com.rensource.videorental.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author r.atenga
 */
@Repository
public interface VideoRepository extends PagingAndSortingRepository<Video, Long> {
    Video findVideoByTitleEquals(String title);
}
