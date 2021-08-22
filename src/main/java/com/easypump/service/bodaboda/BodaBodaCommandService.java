package com.easypump.service.bodaboda;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.bodaboda.BodaBodaDto;
import com.easypump.dto.bodaboda.StageDto;
import com.easypump.infrastructure.AppContext;
import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.exception.BadRequestException;
import com.easypump.infrastructure.utility.Util;
import com.easypump.model.bodaboda.BodaBoda;
import com.easypump.model.bodaboda.Stage;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.common.IdTypeEnum;
import com.easypump.repository.recipient.BodaBodaRepository;
import com.easypump.service.bodaboda.handler.BodaBodaCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BodaBodaCommandService implements BodaBodaCommandHandler {
    private final BodaBodaRepository bodaBodaRepository;

    @Autowired
    public BodaBodaCommandService(BodaBodaRepository bodaBodaRepository) {
        this.bodaBodaRepository = bodaBodaRepository;
    }

    @Override
    @Transactional
    public ActionResponse createBodaBoda(BodaBodaDto bodaBodaDto) {
        bodaBodaDto.isValid();
        BodaBoda bodaBoda = bodaBodaDto.toRecipientEntity();
        AppContext.stamp(bodaBoda);
        StageDto stageDto = bodaBodaDto.getStage();
        stageDto.isValid();
        Stage stage = stageDto.toStageEntity();
        AppContext.stamp(stage);
        bodaBoda.setStage(stage);
        bodaBoda = bodaBodaRepository.save(bodaBoda);
        return new ActionResponse(bodaBoda.getId());
    }

    @Override
    @Transactional
    public ActionResponse updateBodaBoda(BodaBodaDto bodaBodaDto, Integer id) {
        BodaBoda bodaBoda = bodaBodaRepository.findById(id).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, "Recipient", "ID")));
        PowerValidator.isTrue(AuditEntity.RecordStatus.ACTIVE.equals(bodaBoda.getRecordStatus()), String.format(ErrorMessages.CANNOT_UPDATE_RECORD_IN_STATUS, "Recipient", bodaBoda.getRecordStatus()));
        bodaBodaDto.isValid();
        Util.copyProperties(bodaBodaDto, bodaBoda);
        bodaBoda.setIdType(IdTypeEnum.valueOf(bodaBodaDto.getIdType()));
        AppContext.stamp(bodaBoda);
        Stage stage = bodaBoda.getStage();
        StageDto stageDto = bodaBodaDto.getStage();
        stageDto.isValid();
        Util.copyProperties(stageDto, stage);
        AppContext.stamp(stage);
        bodaBoda.setStage(stage);
        bodaBoda = bodaBodaRepository.save(bodaBoda);
        return new ActionResponse(bodaBoda.getId());
    }


    @Override
    @Transactional
    public ActionResponse closeBodaBoda(Integer id) {
        BodaBoda bodaBoda = bodaBodaRepository.findById(id).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, "Boda boda", "ID")));
        bodaBoda.setRecordStatus(AuditEntity.RecordStatus.CLOSED);
        AppContext.stamp(bodaBoda);
        bodaBodaRepository.save(bodaBoda);
        return new ActionResponse(id);
    }
}
