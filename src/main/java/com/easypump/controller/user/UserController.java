package com.easypump.controller.user;

import com.easypump.controller.BaseController;
import com.easypump.dto.ActionResponse;
import com.easypump.dto.user.UserDto;
import com.easypump.model.common.ActionTypeEnum;
import com.easypump.model.common.EntityTypeEnum;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.user.UserActivityLog;
import com.easypump.service.user.handler.UserCommandHandler;
import com.easypump.service.user.handler.UserQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    private final UserCommandHandler userCommandHandler;
    private final UserQueryHandler userQueryHandler;

    @Autowired
    public UserController(UserCommandHandler userCommandHandler, UserQueryHandler userQueryHandler) {
        this.userCommandHandler = userCommandHandler;
        this.userQueryHandler = userQueryHandler;
    }

    @GetMapping
    public RecordHolder<UserDto> getAppUsers(@RequestParam Map<String, String> queryParams) {
        return executeHttpGet(() -> userQueryHandler.getUsersByQueryParams(queryParams));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ActionResponse createUser(@RequestBody UserDto userDto) {
        return executeAndLogUserActivity(EntityTypeEnum.USER, ActionTypeEnum.CREATE, (UserActivityLog log) -> {
            ActionResponse response = userCommandHandler.createUser(userDto);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ActionResponse updateUser(@RequestBody UserDto userDto, @PathVariable Integer id) {
        return executeAndLogUserActivity(EntityTypeEnum.USER, ActionTypeEnum.UPDATE, (UserActivityLog log) -> {
            ActionResponse response = userCommandHandler.updateUser(userDto, id);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ActionResponse removeUser(@PathVariable Integer id) {
        return executeAndLogUserActivity(EntityTypeEnum.USER, ActionTypeEnum.UPDATE, (UserActivityLog log) -> {
            ActionResponse response = userCommandHandler.closeUser(id);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

}
