package com.easypump.service.bodaboda;

import com.easypump.dto.bodaboda.BodaBodaDto;
import com.easypump.dto.bodaboda.BodaBodaSqlResultSet;
import com.easypump.infrastructure.utility.DateUtil;
import com.easypump.model.common.RecordHolder;
import com.easypump.service.bodaboda.handler.RecipientQueryHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BodaBodaQueryService implements RecipientQueryHandler {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BodaBodaQueryService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @SneakyThrows(ParseException.class)
    public RecordHolder<BodaBodaDto> getBodaBodas(Map<String, String> queryParams) {
        Arrays.asList(BodaBodaQueryParams.values()).forEach(param -> queryParams.putIfAbsent(param.getValue(), null));
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(queryParams);
        String recipientQuery = getRecipientQuery();
        if (queryParams.get(BodaBodaQueryParams.DATE_OF_BIRTH.getValue()) != null) {
            Date dateOfBirth = DateUtil.DefaultDateFormat().parse(queryParams.get(BodaBodaQueryParams.DATE_OF_BIRTH.getValue()));
            mapSqlParameterSource.addValue(BodaBodaQueryParams.DATE_OF_BIRTH.getValue(), dateOfBirth, Types.DATE);
        }
        if (queryParams.get(BodaBodaQueryParams.REGISTRATION_DATE.getValue()) != null) {
            Date registrationDate = DateUtil.DefaultDateFormat().parse(queryParams.get(BodaBodaQueryParams.REGISTRATION_DATE.getValue()));
            mapSqlParameterSource.addValue(BodaBodaQueryParams.REGISTRATION_DATE.getValue(), registrationDate, Types.DATE);
        }
        Integer limit = queryParams.get(BodaBodaQueryParams.LIMIT.getValue()) != null ? Integer.parseInt(queryParams.get(BodaBodaQueryParams.LIMIT.getValue())) : Integer.MAX_VALUE;
        mapSqlParameterSource.addValue(BodaBodaQueryParams.LIMIT.getValue(), limit, Types.INTEGER);
        Integer offset = queryParams.get(BodaBodaQueryParams.OFFSET.getValue()) != null ? Integer.parseInt(queryParams.get(BodaBodaQueryParams.OFFSET.getValue())) : 0;
        mapSqlParameterSource.addValue(BodaBodaQueryParams.OFFSET.getValue(), offset, Types.INTEGER);
        List<BodaBodaSqlResultSet> sqlResultSets = jdbcTemplate.query(recipientQuery, mapSqlParameterSource, new RecipientMapper());
        List<BodaBodaDto> bodaBodaDtos = sqlResultSets.stream().map(this::fromSqlResultSet).collect(Collectors.toList());
        return new RecordHolder<>(bodaBodaDtos.size(), bodaBodaDtos);
    }

    private BodaBodaDto fromSqlResultSet(BodaBodaSqlResultSet resultSet) {
        BodaBodaDto bodaBodaDto = new BodaBodaDto();
        BeanUtils.copyProperties(resultSet, bodaBodaDto);
        return bodaBodaDto;
    }

    private static final class RecipientMapper implements RowMapper<BodaBodaSqlResultSet> {

        @Override
        public BodaBodaSqlResultSet mapRow(ResultSet resultSet, int i) throws SQLException {
            BodaBodaSqlResultSet bodaBodaSqlResultSet = new BodaBodaSqlResultSet();
            bodaBodaSqlResultSet.setId(resultSet.getInt(BodaBodaQueryParams.ID.getValue()));
            bodaBodaSqlResultSet.setLastName(resultSet.getString(BodaBodaQueryParams.LAST_NAME.getValue()));
            bodaBodaSqlResultSet.setFirstName(resultSet.getString(BodaBodaQueryParams.FIRST_NAME.getValue()));
            bodaBodaSqlResultSet.setDateOfBirth(resultSet.getDate(BodaBodaQueryParams.DATE_OF_BIRTH.getValue()));
            bodaBodaSqlResultSet.setDateOfBirth(resultSet.getDate(BodaBodaQueryParams.REGISTRATION_DATE.getValue()));
            bodaBodaSqlResultSet.setIdTypeEnum(resultSet.getString(BodaBodaQueryParams.ID_TYPE_ENUM.getValue()));
            bodaBodaSqlResultSet.setIdNumber(resultSet.getString(BodaBodaQueryParams.ID_NUMBER.getValue()));
            bodaBodaSqlResultSet.setAddress(resultSet.getString(BodaBodaQueryParams.ADDRESS.getValue()));
            bodaBodaSqlResultSet.setEmail(resultSet.getString(BodaBodaQueryParams.EMAIL.getValue()));
            bodaBodaSqlResultSet.setStatus(resultSet.getString(BodaBodaQueryParams.STATUS.getValue()));
            bodaBodaSqlResultSet.setPhoneNumber(resultSet.getString(BodaBodaQueryParams.PHONE_NUMBER.getValue()));
            return bodaBodaSqlResultSet;
        }
    }

    private final String BASE_RECIPIENT_QUERY = "" +
            "SELECT rcp.id               AS id,\n" +
            "       rcp.last_name        AS lastName,\n" +
            "       rcp.first_name       AS firstName,\n" +
            "       rcp.date_of_birth    AS dateOfBirth,\n" +
            "       rcp.id_number        AS idNumber,\n" +
            "       rcp.id_type_enum     AS idTypeEnum,\n" +
            "       rcp.address          AS address,\n" +
            "       rcp.email            AS email,\n" +
            "       rcp.phone_number     AS phoneNumber,\n" +
            "       rcp.created_on       AS registrationDate,\n" +
            "       rcp.record_status    AS status,\n" +
            "       createdBy.id         AS createdByUserId,\n" +
            "       createdBy.first_name AS createdByUserFirstName,\n" +
            "       createdBy.last_name  AS createdByUserLastName,\n" +
            "       acc.id               AS accountId,\n" +
            "       acc.account_type     AS accountType,\n" +
            "       acc.country_code     AS countryCode,\n" +
            "       acc.record_status    AS accountStatus,\n" +
            "       com.id               AS companyId,\n" +
            "       com.name             AS companyName,\n" +
            "       com.created_on       AS companyOpeningDate,\n" +
            "       com.company_type     AS companyType,\n" +
            "       com.record_status    AS companyStatus,\n" +
            "       br.id                AS branchId,\n" +
            "       br.name              AS branchName,\n" +
            "       br.created_on        AS branchOpeningDate,\n" +
            "       br.record_status     AS branchStatus,\n" +
            "       rbd.id               AS bankDetailId,\n" +
            "       rbd.bank_name        AS bankName,\n" +
            "       rbd.account_name     AS bankAccountName,\n" +
            "       rbd.account_name     AS bankAccountNumber,\n" +
            "       rbd.branch_name      AS bankBranchName,\n" +
            "       rbd.swift_code       AS bankSwiftCode\n" +
            "FROM bodaboda rcp\n" +
            "         INNER JOIN recipient_bank_detail rbd\n" +
            "                    ON rbd.id = rcp.recipient_bank_detail_id\n" +
            "         INNER JOIN account acc\n" +
            "                    ON acc.id = rcp.account_id\n" +
            "         INNER JOIN app_user createdBy\n" +
            "                    ON createdBy.id = rcp.created_by\n" +
            "         INNER JOIN app_user modifiedBy\n" +
            "                    ON modifiedBy.id = rcp.modified_by\n" +
            "         LEFT OUTER JOIN company com\n" +
            "                         ON com.id = rcp.company_id\n" +
            "         LEFT OUTER JOIN branch br\n" +
            "                         ON br.id = rcp.branch_id\n" +
            "WHERE (rcp.id = :id OR :id IS NULL)\n" +
            "  AND (CONCAT(rcp.first_name, '', rcp.last_name) LIKE\n" +
            "       CONCAT('%', REPLACE(:fullName, ' ', ''), '%') OR\n" +
            "       :fullName IS NULL)\n" +
            "  AND (rcp.date_of_birth = DATE(:dateOfBirth) OR :dateOfBirth IS NULL)\n" +
            "  AND (rcp.id_number LIKE CONCAT('%', :idNumber, '%') OR :idNumber IS NULL)\n" +
            "  AND (rcp.id_type_enum LIKE CONCAT('%', :idTypeEnum, '%') OR :idTypeEnum IS NULL)\n" +
            "  AND (rcp.address LIKE CONCAT('%', :address, '%') OR :address IS NULL)\n" +
            "  AND (rcp.email LIKE CONCAT('%', :email, '%') OR :email IS NULL)\n" +
            "  AND (rcp.phone_number LIKE CONCAT('%', :phoneNumber, '%') OR :phoneNumber IS NULL)\n" +
            "  AND (rcp.created_on = DATE(:registrationDate) OR :registrationDate IS NULL)\n" +
            "  AND (rcp.record_status = :status OR :status IS NULL)\n" +
            "  AND (CONCAT(createdBy.first_name, '', createdBy.last_name) LIKE\n" +
            "       CONCAT('%', REPLACE(:createdByFullName, ' ', ''), '%') OR\n" +
            "       :createdByFullName IS NULL)\n" +
            "  AND (CONCAT(modifiedBy.first_name, '', modifiedBy.last_name) LIKE\n" +
            "       CONCAT('%', REPLACE(:modifiedByFullName, ' ', ''), '%') OR\n" +
            "       :modifiedByFullName IS NULL)\n" +
            "  AND (rcp.account_id IN (:accountIds))\n" +
            "  AND (com.name LIKE CONCAT('%', :companyName, '%') OR :companyName IS NULL)\n" +
            "  AND (br.name LIKE CONCAT('%', :branchName, '%') OR :branchName IS NULL)";

    private String getRecipientQuery() {
        return BASE_RECIPIENT_QUERY + "" +
                "LIMIT :limit OFFSET :offset";
    }

    @AllArgsConstructor
    @Getter
    public enum BodaBodaQueryParams {
        LIMIT("limit"),
        OFFSET("offset"),
        ID("id"),
        FULL_NAME("fullName"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        ADDRESS("address"),
        EMAIL("email"),
        STATUS("status"),
        DATE_OF_BIRTH("dateOfBirth"),
        REGISTRATION_DATE("registrationDate"),
        ID_NUMBER("idNumber"),
        ID_TYPE_ENUM("idTypeEnum"),
        PHONE_NUMBER("phoneNumber"),
        CREATED_BY_FULL_NAME("createdByFullName"),
        MODIFIED_BY_FULL_NAME("modifiedByFullName");
        private final String value;
    }
}
