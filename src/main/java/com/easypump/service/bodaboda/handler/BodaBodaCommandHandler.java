package com.easypump.service.bodaboda.handler;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.bodaboda.BodaBodaDto;
import org.springframework.web.multipart.MultipartFile;

public interface BodaBodaCommandHandler {

    ActionResponse createBodaBoda(BodaBodaDto bodaBodaDto);

    ActionResponse updateBodaBoda(BodaBodaDto bodaBodaDto, Integer id);

    ActionResponse closeBodaBoda(Integer id);
}
