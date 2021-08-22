package com.easypump.controller.role;


import com.easypump.controller.BaseController;
import com.easypump.dto.ActionResponse;
import com.easypump.dto.role.PermissionDto;
import com.easypump.dto.role.RoleDto;
import com.easypump.model.common.ActionTypeEnum;
import com.easypump.model.common.EntityTypeEnum;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.user.UserActivityLog;
import com.easypump.service.role.handler.RoleCommandHandler;
import com.easypump.service.role.handler.RoleQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/role")
public class RoleController extends BaseController {

    private final RoleCommandHandler roleCommandHandler;
    private final RoleQueryHandler roleQueryHandler;

    @Autowired
    public RoleController(RoleCommandHandler roleCommandHandler, RoleQueryHandler roleQueryHandler) {
        this.roleCommandHandler = roleCommandHandler;
        this.roleQueryHandler = roleQueryHandler;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ActionResponse createRole(@RequestBody RoleDto roleDto) {
        return executeAndLogUserActivity(EntityTypeEnum.ROLE, ActionTypeEnum.CREATE, (UserActivityLog log) -> {
            ActionResponse response = roleCommandHandler.createRole(roleDto);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.PUT)
    public ActionResponse updateRole(@RequestBody RoleDto roleDto, @PathVariable("roleId") Integer roleId) {
        return executeAndLogUserActivity(EntityTypeEnum.ROLE, ActionTypeEnum.CREATE, (UserActivityLog log) -> {
            ActionResponse response = roleCommandHandler.updateRole(roleDto, roleId);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.DELETE)
    public ActionResponse removeRole(@PathVariable("roleId") Integer roleId) {
        return executeAndLogUserActivity(EntityTypeEnum.ROLE, ActionTypeEnum.REMOVE, (UserActivityLog log) -> {
            ActionResponse response = roleCommandHandler.removeRole(roleId);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(method = RequestMethod.GET)
    public RecordHolder<RoleDto> getRoles(@RequestParam Map<String, String> queryParams) {
        return executeHttpGet(() -> roleQueryHandler.getRoles(queryParams));
    }

    @RequestMapping(path = "/permission", method = RequestMethod.GET)
    public RecordHolder<PermissionDto> getPermissions(@RequestParam Map<String, String> queryParams) {
        return executeHttpGet(() -> roleQueryHandler.getPermissions(queryParams));
    }
}
