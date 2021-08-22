package com.easypump.service.user.handler;

import com.easypump.dto.user.UserDto;
import com.easypump.model.common.RecordHolder;

import java.util.Map;

public interface UserQueryHandler {

    RecordHolder<UserDto> getUsersByQueryParams(Map<String, String> queryParams);
}
