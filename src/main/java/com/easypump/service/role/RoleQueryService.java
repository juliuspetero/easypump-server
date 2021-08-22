package com.easypump.service.role;

import com.easypump.dto.role.PermissionDto;
import com.easypump.dto.role.PermissionSqlResultSet;
import com.easypump.dto.role.RoleDto;
import com.easypump.infrastructure.AppContext;
import com.easypump.infrastructure.utility.Util;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.role.Role;
import com.easypump.model.user.User;
import com.easypump.repository.role.RoleRepository;
import com.easypump.service.role.handler.RoleQueryHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RoleQueryService implements RoleQueryHandler {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleQueryService(NamedParameterJdbcTemplate jdbcTemplate, RoleRepository roleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleRepository = roleRepository;
    }


    @Override
    public RecordHolder<RoleDto> getRoles(Map<String, String> queryParams) {
        Integer id = queryParams.get(RoleQueryParams.ID.getValue()) != null ?
                Integer.parseInt(queryParams.get(RoleQueryParams.ID.getValue())) : null;
        String name = queryParams.get(RoleQueryParams.NAME.getValue());
        String description = queryParams.get(RoleQueryParams.DESCRIPTION.getValue());
        List<Role> roles = roleRepository.findByQueryParams(id, name, description);
        List<RoleDto> roleDtos = roles.stream().map(this::toRoleDto).collect(Collectors.toList());
        return new RecordHolder<>(roleDtos.size(), roleDtos);
    }

    private RoleDto toRoleDto(Role role) {
        RoleDto roleDto = Util.copyProperties(role, new RoleDto());
        roleDto.setStatus(role.getStatus().name());
        List<PermissionDto> permissionDtos = role.getPermissions().stream().map(p -> PermissionDto.builder().id(p.getId()).actionType(p.getActionType().name()).entityType(p.getEntityType().name()).build()).collect(Collectors.toList());
        roleDto.setPermissions(permissionDtos);
        return roleDto;
    }

    private static final class PermissionMapper implements RowMapper<PermissionSqlResultSet> {
        @Override
        public PermissionSqlResultSet mapRow(ResultSet resultSet, int i) throws SQLException {
            PermissionSqlResultSet permissionSqlResultSet = new PermissionSqlResultSet();
            permissionSqlResultSet.setId(resultSet.getInt(PermissionQueryParams.ID.getValue()));
            permissionSqlResultSet.setName(resultSet.getString(PermissionQueryParams.NAME.getValue()));
            permissionSqlResultSet.setActionType(resultSet.getString(PermissionQueryParams.ACTION_TYPE.getValue()));
            permissionSqlResultSet.setEntityType(resultSet.getString(PermissionQueryParams.ENTITY_TYPE.getValue()));
            return permissionSqlResultSet;
        }
    }

    @Override
    public RecordHolder<PermissionDto> getPermissions(Map<String, String> queryParams) {
        User loggedInUser = AppContext.getLoggedInUser();
        Arrays.asList(PermissionQueryParams.values()).forEach(param -> queryParams.putIfAbsent(param.getValue(), null));
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(queryParams);
        Integer limit = queryParams.get(PermissionQueryParams.LIMIT.getValue()) != null ? Integer.parseInt(queryParams.get(PermissionQueryParams.LIMIT.getValue())) : Integer.MAX_VALUE;
        mapSqlParameterSource.addValue(PermissionQueryParams.LIMIT.getValue(), limit, Types.INTEGER);
        Integer offset = queryParams.get(PermissionQueryParams.OFFSET.getValue()) != null ? Integer.parseInt(queryParams.get(PermissionQueryParams.OFFSET.getValue())) : 0;
        mapSqlParameterSource.addValue(PermissionQueryParams.OFFSET.getValue(), offset, Types.INTEGER);
        List<PermissionSqlResultSet> sqlResultSets = jdbcTemplate.query(permissionQuery(), mapSqlParameterSource, new PermissionMapper());
        List<PermissionDto> permissionDtos = sqlResultSets.stream().map(rs -> PermissionDto.builder().id(rs.getId()).actionType(rs.getActionType()).entityType(rs.getEntityType()).build()).collect(Collectors.toList());
        return new RecordHolder<>(permissionDtos.size(), permissionDtos);
    }

    private String permissionQuery() {
        return "" +
                "SELECT pm.id          AS id,\n" +
                "       pm.name        AS name,\n" +
                "       pm.action_type AS actionType,\n" +
                "       pm.entity_type AS entityType\n" +
                "FROM permission pm\n" +
                "WHERE (pm.id = :id OR :id IS NULL)\n" +
                "  AND (pm.name LIKE CONCAT('%', :name, '%') OR :name IS NULL)\n" +
                "  AND (pm.action_type LIKE CONCAT('%', :actionType, '%') OR :actionType IS NULL)\n" +
                "  AND (pm.entity_type LIKE CONCAT('%', :entityType, '%') OR :entityType IS NULL)\n" +
                "LIMIT :limit OFFSET :offset";
    }

    @AllArgsConstructor
    @Getter
    public enum PermissionQueryParams {
        LIMIT("limit"),
        OFFSET("offset"),
        ID("id"),
        NAME("name"),
        ENTITY_TYPE("entityType"),
        ACTION_TYPE("actionType");
        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum RoleQueryParams {
        LIMIT("limit"),
        OFFSET("offset"),
        ID("id"),
        ACCOUNT_ID("category"),
        NAME("name"),
        DESCRIPTION("entityType");
        private final String value;
    }
}
