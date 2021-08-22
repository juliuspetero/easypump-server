package com.easypump.service.role.handler;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.role.RoleDto;

public interface RoleCommandHandler {

    ActionResponse createRole(RoleDto roleDto);

    ActionResponse updateRole(RoleDto roleDto, Integer roleId);

    ActionResponse removeRole(Integer roleId);

}
