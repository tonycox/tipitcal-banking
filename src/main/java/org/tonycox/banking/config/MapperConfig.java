package org.tonycox.banking.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tonycox.banking.account.api.dto.AccountEventDto;
import org.tonycox.banking.account.api.request.AccountEventRequest;
import org.tonycox.banking.account.model.AccountEventDao;
import org.tonycox.banking.account.service.dto.AccountEvent;
import org.tonycox.banking.account.service.request.AccountEventServiceRequest;
import org.tonycox.banking.auth.api.request.SignUpRequest;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        signupRequestToServiceRequest(modelMapper);
        eventRequestToServiceRequest(modelMapper);
        eventDaoToEvent(modelMapper);
        eventToEventDto(modelMapper);
        return modelMapper;
    }

    private void signupRequestToServiceRequest(ModelMapper modelMapper) {
        TypeMap<SignUpRequest, SignUpServiceRequest> typeMap = modelMapper
                .typeMap(SignUpRequest.class, SignUpServiceRequest.class);
        typeMap.addMapping(SignUpRequest::getEmail, SignUpServiceRequest::setEmail);
        typeMap.addMapping(SignUpRequest::getPassword, SignUpServiceRequest::setPassword);
    }

    private void eventRequestToServiceRequest(ModelMapper modelMapper) {
        TypeMap<AccountEventRequest, AccountEventServiceRequest> typeMap = modelMapper
                .typeMap(AccountEventRequest.class, AccountEventServiceRequest.class);
        typeMap.addMapping(AccountEventRequest::getAmount, AccountEventServiceRequest::setAmount);
        typeMap.addMapping(AccountEventRequest::getEventType, AccountEventServiceRequest::setEventType);
        typeMap.addMapping(AccountEventRequest::getUserId, AccountEventServiceRequest::setUserId);
    }

    private void eventDaoToEvent(ModelMapper modelMapper) {
        TypeMap<AccountEventDao, AccountEvent> typeMap = modelMapper
                .typeMap(AccountEventDao.class, AccountEvent.class);
        typeMap.addMapping(AccountEventDao::getAmount, AccountEvent::setAmount);
        typeMap.addMapping(AccountEventDao::getEventType, AccountEvent::setEventType);
        typeMap.addMapping(AccountEventDao::getUserId, AccountEvent::setUserId);
        typeMap.addMapping(AccountEventDao::getId, AccountEvent::setId);
        typeMap.addMapping(AccountEventDao::getCreatedAt, AccountEvent::setCreatedAt);
    }

    private void eventToEventDto(ModelMapper modelMapper) {
        TypeMap<AccountEvent, AccountEventDto> typeMap = modelMapper
                .typeMap(AccountEvent.class, AccountEventDto.class);
        typeMap.addMapping(AccountEvent::getAmount, AccountEventDto::setAmount);
        typeMap.addMapping(AccountEvent::getEventType, AccountEventDto::setEventType);
        typeMap.addMapping(AccountEvent::getUserId, AccountEventDto::setUserId);
        typeMap.addMapping(AccountEvent::getId, AccountEventDto::setEventId);
        typeMap.addMapping(AccountEvent::getCreatedAt, AccountEventDto::setCreatedAt);
    }
}
