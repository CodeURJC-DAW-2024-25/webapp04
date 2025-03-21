package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.Message;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface MessageMapper {

    MessageDTO toDTO(Message message);
    
}