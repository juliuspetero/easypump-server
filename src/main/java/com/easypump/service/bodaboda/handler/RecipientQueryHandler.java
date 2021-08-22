package com.easypump.service.bodaboda.handler;

import com.easypump.dto.bodaboda.BodaBodaDto;
import com.easypump.model.common.RecordHolder;

import java.util.Map;

public interface RecipientQueryHandler {
    RecordHolder<BodaBodaDto> getBodaBodas(Map<String, String> queryParams);
}
