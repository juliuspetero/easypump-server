package com.easypump.service.user;


import com.easypump.dto.user.UserDto;
import com.easypump.dto.user.UserSqlResultSet;
import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.security.UserDetails;
import com.easypump.infrastructure.utility.DateUtil;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.user.User;
import com.easypump.repository.user.AppUserRepository;
import com.easypump.service.user.handler.UserQueryHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserQueryService implements UserDetailsService, UserQueryHandler {
    private final AppUserRepository appUserRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserQueryService(AppUserRepository appUserRepository, NamedParameterJdbcTemplate jdbcTemplate) {
        this.appUserRepository = appUserRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @SneakyThrows(ParseException.class)
    public RecordHolder<UserDto> getUsersByQueryParams(Map<String, String> queryParams) {
        Arrays.asList(UserQueryParams.values()).forEach(param -> queryParams.putIfAbsent(param.getValue(), null));
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(queryParams);
        String appUserQuery = getUserQuery();
        if (queryParams.get(UserQueryParams.DATE_OF_BIRTH.getValue()) != null) {
            Date dateOfBirth = DateUtil.DefaultDateFormat().parse(queryParams.get(UserQueryParams.DATE_OF_BIRTH.getValue()));
            mapSqlParameterSource.addValue(UserQueryParams.DATE_OF_BIRTH.getValue(), dateOfBirth, Types.DATE);
        }
        Integer limit = queryParams.get(UserQueryParams.LIMIT.getValue()) != null ? Integer.parseInt(queryParams.get(UserQueryParams.LIMIT.getValue())) : Integer.MAX_VALUE;
        mapSqlParameterSource.addValue(UserQueryParams.LIMIT.getValue(), limit, Types.INTEGER);
        Integer offset = queryParams.get(UserQueryParams.OFFSET.getValue()) != null ? Integer.parseInt(queryParams.get(UserQueryParams.OFFSET.getValue())) : 0;
        mapSqlParameterSource.addValue(UserQueryParams.OFFSET.getValue(), offset, Types.INTEGER);
        List<UserSqlResultSet> sqlResultSets = jdbcTemplate.query(appUserQuery, mapSqlParameterSource, new AppUserMapper());
        List<UserDto> userDtos = sqlResultSets.stream().map(this::fromSqlResultSet).collect(Collectors.toList());
        return new RecordHolder<>(userDtos.size(), userDtos);
    }

    private UserDto fromSqlResultSet(UserSqlResultSet resultSet) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(resultSet, userDto);
        return userDto;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = appUserRepository.findOneByEmailAndRecordStatus(s, AuditEntity.RecordStatus.ACTIVE);
        PowerValidator.notNull(user, ErrorMessages.INVALID_SECURITY_CREDENTIAL);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null) {
            user.getRole().getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getName())).forEach(authorities::add);
        }
        return new UserDetails(user, authorities);
    }

    private static final class AppUserMapper implements RowMapper<UserSqlResultSet> {

        @Override
        public UserSqlResultSet mapRow(ResultSet resultSet, int i) throws SQLException {
            UserSqlResultSet userSqlResultSet = new UserSqlResultSet();
            userSqlResultSet.setId(resultSet.getInt(UserQueryParams.ID.getValue()));
            userSqlResultSet.setLastName(resultSet.getString(UserQueryParams.LAST_NAME.getValue()));
            userSqlResultSet.setFirstName(resultSet.getString(UserQueryParams.FIRST_NAME.getValue()));
            userSqlResultSet.setAddress(resultSet.getString(UserQueryParams.ADDRESS.getValue()));
            userSqlResultSet.setEmail(resultSet.getString(UserQueryParams.EMAIL.getValue()));
            userSqlResultSet.setDateOfBirth(resultSet.getDate(UserQueryParams.DATE_OF_BIRTH.getValue()));
            userSqlResultSet.setIdNumber(resultSet.getString(UserQueryParams.ID_NUMBER.getValue()));
            userSqlResultSet.setUserStatus(resultSet.getString(UserQueryParams.USER_STATUS.getValue()));
            userSqlResultSet.setRoleId(resultSet.getInt(UserQueryParams.ROLE_ID.getValue()));
            userSqlResultSet.setRoleName(resultSet.getString(UserQueryParams.ROLE_NAME.getValue()));
            userSqlResultSet.setRoleDescription(resultSet.getString(UserQueryParams.ROLE_DESCRIPTION.getValue()));
            userSqlResultSet.setRoleStatus(resultSet.getString(UserQueryParams.ROLE_STATUS.getValue()));
            userSqlResultSet.setPhoneNumber(resultSet.getString(UserQueryParams.PHONE_NUMBER.getValue()));
            return userSqlResultSet;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum UserQueryParams {
        LIMIT("limit"),
        OFFSET("offset"),
        ID("id"),
        FULL_NAME("fullName"),
        ADDRESS("address"),
        EMAIL("email"),
        USER_STATUS("userStatus"),
        DATE_OF_BIRTH("dateOfBirth"),
        ID_NUMBER("idNumber"),
        PHONE_NUMBER("phoneNumber"),
        ROLE_ID("roleId"),
        ROLE_NAME("roleName"),
        ROLE_DESCRIPTION("roleDescription"),
        ROLE_STATUS("roleStatus"),
        CREATED_BY_FULL_NAME("createdByFullName"),
        MODIFIED_BY_FULL_NAME("modifiedByFullName"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName");
        private final String value;
    }

    private String getUserQuery() {
        return "SELECT usr.id            AS id,\n" +
                "       usr.first_name    AS firstName,\n" +
                "       usr.last_name     AS lastName,\n" +
                "       usr.address       AS address,\n" +
                "       usr.email         AS email,\n" +
                "       usr.record_status AS userStatus,\n" +
                "       usr.date_of_birth AS dateOfBirth,\n" +
                "       usr.id_number     AS idNumber,\n" +
                "       usr.phone_number  AS phoneNumber,\n" +
                "       usr.is_verified   AS verified,\n" +
                "       com.id            AS companyId,\n" +
                "       com.name          AS companyName,\n" +
                "       com.company_type  AS companyType,\n" +
                "       com.record_status AS companyStatus,\n" +
                "       acc.id            AS accountId,\n" +
                "       acc.country_code  AS countryCode,\n" +
                "       acc.account_type  AS accountType,\n" +
                "       br.id             AS branchId,\n" +
                "       br.name           AS branchName,\n" +
                "       br.created_on     AS branchOpeningDate,\n" +
                "       br.record_status  AS branchStatus,\n" +
                "       rl.id             AS roleId,\n" +
                "       rl.name           AS roleName,\n" +
                "       rl.description    AS roleDescription,\n" +
                "       rl.record_status  AS roleStatus\n" +
                "FROM app_user usr\n" +
                "         INNER JOIN account acc\n" +
                "                    ON acc.id = usr.account_id\n" +
                "         LEFT OUTER JOIN role rl\n" +
                "                         ON rl.id = usr.role_id\n" +
                "         LEFT OUTER JOIN company com\n" +
                "                         ON com.id = usr.company_id\n" +
                "         LEFT OUTER JOIN branch br\n" +
                "                         ON br.id = usr.branch_id\n" +
                "         LEFT OUTER JOIN app_user createdBy\n" +
                "                         ON createdBy.id = usr.id\n" +
                "         LEFT OUTER JOIN app_user modifiedBy\n" +
                "                         ON modifiedBy.id = usr.id\n" +
                "WHERE (usr.id = :id OR :id IS NULL)\n" +
                "  AND (CONCAT(usr.first_name, '', usr.last_name) LIKE\n" +
                "       CONCAT('%', REPLACE(:fullName, ' ', ''), '%') OR\n" +
                "       :fullName IS NULL)\n" +
                "  AND (usr.address LIKE CONCAT('%', :address, '%') OR :address IS NULL)\n" +
                "  AND (usr.email LIKE CONCAT('%', :email, '%') OR :email IS NULL)\n" +
                "  AND (usr.record_status = :userStatus OR :userStatus IS NULL)\n" +
                "  AND (usr.date_of_birth = DATE(:dateOfBirth) OR :dateOfBirth IS NULL)\n" +
                "  AND (usr.id_number LIKE CONCAT('%', :idNumber, '%') OR :idNumber IS NULL)\n" +
                "  AND (usr.phone_number LIKE CONCAT('%', :phoneNumber, '%') OR :phoneNumber IS NULL)\n" +
                "  AND (usr.is_verified = :verified OR :verified IS NULL)\n" +
                "  AND (CONCAT(createdBy.first_name, '', createdBy.last_name) LIKE\n" +
                "       CONCAT('%', REPLACE(:createdByFullName, ' ', ''), '%') OR\n" +
                "       :createdByFullName IS NULL)\n" +
                "  AND (CONCAT(modifiedBy.first_name, '', modifiedBy.last_name) LIKE\n" +
                "       CONCAT('%', REPLACE(:modifiedByFullName, ' ', ''), '%') OR\n" +
                "       :modifiedByFullName IS NULL)\n" +
                "  AND (usr.account_id = :accountId OR :accountId IS NULL)\n" +
                "  AND (com.name LIKE CONCAT('%', :companyName, '%') OR :companyName IS NULL)\n" +
                "  AND (br.name LIKE CONCAT('%', :branchName, '%') OR :branchName IS NULL)\n" +
                "  AND (usr.role_id = :roleId OR :roleId IS NULL)\n" +
                "  AND (rl.name LIKE CONCAT('%', :roleName, '%') OR :roleName IS NULL)\n" +
                "LIMIT :limit OFFSET :offset";
    }
}
