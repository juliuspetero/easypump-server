package com.easypump.dto.user;

import com.easypump.infrastructure.utility.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class UserSqlResultSet {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String userStatus;
    @JsonFormat(pattern = DateUtil.DATE_FORMAT_BY_SLASH)
    private Date dateOfBirth;
    private String idNumber;
    private String phoneNumber;
    private boolean verified;
    private Integer companyId;
    private String companyName;
    private String companyType;
    private String companyStatus;
    private Integer accountId;
    private String countryCode;
    private String accountType;
    private Integer branchId;
    private String branchName;
    @JsonFormat(pattern = DateUtil.DATE_FORMAT_BY_SLASH)
    private Date branchOpeningDate;
    private String branchStatus;
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private String roleStatus;
}
