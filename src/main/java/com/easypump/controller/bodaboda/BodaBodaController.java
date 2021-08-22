package com.easypump.controller.recipient;

import com.easypump.controller.BaseController;
import com.easypump.dto.ActionResponse;
import com.easypump.dto.bodaboda.BodaBodaDto;
import com.easypump.model.common.ActionTypeEnum;
import com.easypump.model.common.EntityTypeEnum;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.user.UserActivityLog;
import com.easypump.service.bodaboda.BodaBodaQueryService;
import com.easypump.service.bodaboda.handler.BodaBodaCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/bodaBoda")
public class BodaBodaController extends BaseController {
    private final BodaBodaCommandHandler bodaBodaCommandHandler;
    private final BodaBodaQueryService bodaBodaQueryService;

    @Autowired
    public BodaBodaController(BodaBodaCommandHandler bodaBodaCommandHandler, BodaBodaQueryService bodaBodaQueryService) {
        this.bodaBodaCommandHandler = bodaBodaCommandHandler;
        this.bodaBodaQueryService = bodaBodaQueryService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ActionResponse createBodaBoda(@RequestBody BodaBodaDto bodaBodaDto) {
        return executeAndLogUserActivity(EntityTypeEnum.BODA_BODA, ActionTypeEnum.CREATE, (UserActivityLog log) -> {
            ActionResponse response = bodaBodaCommandHandler.createBodaBoda(bodaBodaDto);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ActionResponse updateBodaBoda(@RequestBody BodaBodaDto bodaBodaDto, @PathVariable("id") Integer id) {
        return executeAndLogUserActivity(EntityTypeEnum.BODA_BODA, ActionTypeEnum.UPDATE, (UserActivityLog log) -> {
            ActionResponse response = bodaBodaCommandHandler.updateBodaBoda(bodaBodaDto, id);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ActionResponse closedRecipient(@PathVariable("id") Integer id) {
        return executeAndLogUserActivity(EntityTypeEnum.BODA_BODA, ActionTypeEnum.CLOSE, (UserActivityLog log) -> {
            ActionResponse response = bodaBodaCommandHandler.closeBodaBoda(id);
            log.setEntityId(response.getResourceId());
            return response;
        });
    }

    @RequestMapping(method = RequestMethod.GET)
    public RecordHolder<BodaBodaDto> getBodaBodas(@RequestParam Map<String, String> queryParams) {
        return executeHttpGet(() -> bodaBodaQueryService.getBodaBodas(queryParams));
    }

}
