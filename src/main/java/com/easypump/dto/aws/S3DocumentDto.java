package com.easypump.dto.aws;

import com.easypump.dto.user.UserDto;
import com.easypump.model.aws.S3Document;
import com.easypump.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class S3DocumentDto {
    private Integer id;
    private String name;
    private String mimeType;
    private String path;
    private String uploadType;
    private UserDto createdBy;

    public S3DocumentDto(S3Document s3Document) {
        this.id = s3Document.getId();
        this.name = s3Document.getName();
        this.mimeType = s3Document.getMimeType();
        this.path = s3Document.getPath();
        this.uploadType = s3Document.getUploadType().name();
        User user = s3Document.getCreatedBy();
        this.createdBy = UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build();
    }
}
