package com.easypump.dto.user;

import com.easypump.dto.role.RoleDto;
import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.utility.DateUtil;
import com.easypump.infrastructure.utility.Util;
import com.easypump.model.common.IdTypeEnum;
import com.easypump.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String authentication;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = DateUtil.DATE_FORMAT_BY_SLASH)
    private Date dateOfBirth;
    private String idNumber;
    private String idType;
    private String address;
    private String email;
    private String phoneNumber;
    private String status;
    private String password;
    private RoleDto role;
    private UserDto createdBy;
    private UserDto modifiedBy;

    public User toUserEntity() {
        User user = Util.copyProperties(this, new User());
        user.setIdType(IdTypeEnum.valueOf(idType));
        return user;
    }

    @SneakyThrows
    public void isValid() {
        PowerValidator.validStringLength(firstName, 5, 255, ErrorMessages.INVALID_FIRST_NAME);
        PowerValidator.validStringLength(lastName, 5, 255, ErrorMessages.INVALID_LAST_NAME);
        PowerValidator.validStringLength(idNumber, 5, 255, ErrorMessages.INVALID_ID_NUMBER_PROVIDED);
        PowerValidator.ValidEnum(IdTypeEnum.class, idType, ErrorMessages.INVALID_ID_TYPE);
        PowerValidator.notNull(dateOfBirth, ErrorMessages.DATE_OF_BIRTH_REQUIRED);
        PowerValidator.validStringLength(address, 10, 255, ErrorMessages.INVALID_LOCATION_ADDRESS);
        PowerValidator.validEmail(email, ErrorMessages.INVALID_EMAIL_ADDRESS);
        PowerValidator.validPhoneNumber(phoneNumber, ErrorMessages.INVALID_PHONE_NUMBER);
    }
}
