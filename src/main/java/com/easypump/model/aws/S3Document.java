package com.easypump.model.aws;

import com.easypump.model.common.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity(name = "s3_document")
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class S3Document extends AuditEntity {
    private String name;
    private String mimeType;
    private String path;
    private S3UploadTypeEnum uploadType;

    @Column(name = "name")
    public String getName() {
        return name;
    }


    @Column(name = "mime_type")
    public String getMimeType() {
        return mimeType;
    }

    @Column(name = "path")
    public String getPath() {
        return path;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_type_enum")
    public S3UploadTypeEnum getUploadType() {
        return uploadType;
    }
}
