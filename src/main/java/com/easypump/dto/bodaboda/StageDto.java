package com.easypump.dto.bodaboda;

import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.utility.Util;
import com.easypump.model.bodaboda.Stage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageDto {
    private Integer id;
    private String name;
    private String region;
    private String district;
    private String county;
    private String subCounty;
    private String village;
    private String managerName;
    private String managerPhoneNumber;
    private String managerEmail;

    public void isValid() {
        PowerValidator.notNull(name, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage name"));
        PowerValidator.notNull(region, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage Region"));
        PowerValidator.notNull(district, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage district"));
        PowerValidator.notNull(county, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage county"));
        PowerValidator.notNull(subCounty, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage sub-county"));
        PowerValidator.notNull(village, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage village"));
        PowerValidator.notNull(managerName, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage manager name"));
        PowerValidator.notNull(managerPhoneNumber, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage manager phone number"));
        PowerValidator.notNull(managerEmail, String.format(ErrorMessages.VALID_PARAMETER_REQUIRED, "Stage manager email"));
    }

    public Stage toStageEntity() {
        return Util.copyProperties(this, new Stage());
    }
}
