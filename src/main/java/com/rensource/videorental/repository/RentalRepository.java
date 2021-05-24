package com.rensource.videorental.repository;

import com.rensource.videorental.entity.Rental;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author r.atenga
 */
@Repository
public interface RentalRepository extends PagingAndSortingRepository<Rental, Long> {

//    @Query("SELECT r FROM Rental r where r.creationDate = ?1 and r.user.username = ?2 order by r.user.username")
//    List<Rental> findAllByCreationDateAndUserOrderByUser(LocalDateTime createdDate, String userName, Pageable pageable);
//    @Query("FROM Rental AS r LEFT JOIN r.user as u where r.creationDate = ?1 and u.username = ?2 order by u.username")
//    @Query("SELECT r.id, r.daysRented, r.price, r.user, r.video, r.creationDate, r.lastModifiedDate FROM Rental AS r INNER JOIN User AS u on r.user = u where r.creationDate = ?1 and u.username = ?2 order by u.username")
    @Query("SELECT r FROM Rental r where r.creationDate = ?1 and r.user.username = ?2 order by r.user.username")
    List<Rental> findAllWithCreatedDateAndUsername(LocalDate createdDate, String userName, Pageable pageable);

    @Query("SELECT r FROM Rental r where r.user.username = ?1 order by r.user.username")
    List<Rental> findAllWithUsername(String userName, Pageable pageable);

    @Query("SELECT r FROM Rental r where r.creationDate = ?1 order by r.user.username")
    List<Rental> findAllWithCreatedDate(LocalDate createdDate, Pageable pageable);

    @Query("SELECT r FROM Rental r order by r.user.username")
    List<Rental> findAllOrderByUsername(Pageable pageable);
}
