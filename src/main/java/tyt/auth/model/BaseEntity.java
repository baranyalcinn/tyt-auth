package tyt.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity is an abstract class that provides common fields for all entities.
 * It implements Serializable interface for possible serialization.
 * It is annotated with @Data from Lombok to auto-generate getters, setters, equals, hashCode and toString methods.
 * It is annotated with @MappedSuperclass to allow JPA to include fields of this class as if they were declared by the child entity class.
 * It is annotated with @EntityListeners(AuditingEntityListener.class) to automatically populate createdBy, createdDate, lastModifiedBy, lastModifiedDate fields.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    /**
     * Unique identifier for the entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * The date and time when the entity was created.
     * It is set automatically on persist.
     * It is not updatable.
     */
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    /**
     * The username of the user who created the entity.
     * It is set automatically on persist.
     */
    @CreatedBy
    protected String createdBy;

    /**
     * The date and time when the entity was last updated.
     * It is set automatically on update.
     * It is not insertable.
     */
    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(insertable = false)
    protected LocalDateTime updatedAt;

    /**
     * The username of the user who last updated the entity.
     * It is set automatically on update.
     * It is not insertable.
     */
    @LastModifiedBy
    @Column(insertable = false)
    protected String updatedBy;

}