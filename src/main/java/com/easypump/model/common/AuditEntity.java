package com.easypump.model.common;

import com.easypump.model.user.User;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@MappedSuperclass
public class AuditEntity extends BaseEntity {
    private Date createdOn;
    private Date modifiedOn;
    private User createdBy;
    private User modifiedBy;
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedOn() {
        return createdOn;
    }

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    public User getCreatedBy() {
        return createdBy;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modified_by")
    public User getModifiedBy() {
        return modifiedBy;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    public enum RecordStatus {
        ACTIVE,
        REJECTED,
        APPROVED,
        INACTIVE,
        DELETED,
        CLOSED,
        PENDING_PASSWORD_RESET
    }
}
