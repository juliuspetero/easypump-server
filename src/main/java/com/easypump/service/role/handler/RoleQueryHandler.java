package com.easypump.service.role.handler;

import com.easypump.dto.role.PermissionDto;
import com.easypump.dto.role.RoleDto;
import com.easypump.model.common.RecordHolder;

import java.util.Map;

public interface RoleQueryHandler {

    RecordHolder<RoleDto> getRoles(Map<String, String> queryParams);

    RecordHolder<PermissionDto> getPermissions(Map<String, String> queryParams);
}
