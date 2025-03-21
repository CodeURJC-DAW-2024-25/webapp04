package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.Chat;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserBasicMapper.class})
public interface ChatMapper {

    ChatDTO toDTO(Chat chat);
    
}

