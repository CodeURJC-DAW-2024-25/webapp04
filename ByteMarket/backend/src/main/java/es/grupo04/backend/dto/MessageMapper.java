package es.grupo04.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", source = "message.sender.name")
    MessageDTO toDTO(Message message);
    
}