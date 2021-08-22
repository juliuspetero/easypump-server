package com.easypump.service.user.handler;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.user.UserDto;

public interface UserCommandHandler {

    ActionResponse createUser(UserDto userDto);

    ActionResponse updateUser(UserDto userDto, Integer id);

    ActionResponse closeUser(Integer userId);
}
