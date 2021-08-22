package com.easypump.dto.bodaboda;

import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.utility.DateUtil;
import com.easypump.infrastructure.utility.Util;
import com.easypump.model.bodaboda.BodaBoda;
import com.easypump.model.common.IdTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class BodaBodaDto {
    private Integer id;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = DateUtil.DATE_FORMAT_BY_SLASH)
    private Date dateOfBirth;
    private String idNumber;
    private String idType;
    private String email;
    private String phoneNumber;
    private String region;
    private String district;
    private String county;
    private String subCounty;
    private String village;
    private StageDto stage;

    @SneakyThrows
    public void isValid() {
        PowerValidator.validStringLength(firstName, 5, 255, ErrorMessages.INVALID_FIRST_NAME);
        PowerValidator.notNull(stage, ErrorMessages.STAGE_DETAIL_REQUIRED);
        PowerValidator.validStringLength(lastName, 5, 255, ErrorMessages.INVALID_LAST_NAME);
        PowerValidator.validStringLength(idNumber, 5, 255, ErrorMessages.INVALID_ID_NUMBER_PROVIDED);
        PowerValidator.ValidEnum(IdTypeEnum.class, idType, ErrorMessages.INVALID_ID_TYPE);
        PowerValidator.notNull(dateOfBirth, ErrorMessages.DATE_OF_BIRTH_REQUIRED);
        PowerValidator.validEmail(email, ErrorMessages.INVALID_EMAIL_ADDRESS);
        PowerValidator.validPhoneNumber(phoneNumber, ErrorMessages.INVALID_PHONE_NUMBER);
        PowerValidator.notNull(region, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage Region"));
        PowerValidator.notNull(district, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage district"));
        PowerValidator.notNull(county, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage county"));
        PowerValidator.notNull(subCounty, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage sub-county"));
        PowerValidator.notNull(village, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage village"));
    }

    public BodaBoda toRecipientEntity() {
        BodaBoda bodaBoda = Util.copyProperties(this, new BodaBoda());
        bodaBoda.setIdType(IdTypeEnum.valueOf(idType));
        return bodaBoda;
    }
}
