package com.rensource.videorental.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author r.atenga
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDate creationDate = LocalDate.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate lastModifiedDate = creationDate;

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public BaseAuditableModel setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public BaseAuditableModel setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
}
