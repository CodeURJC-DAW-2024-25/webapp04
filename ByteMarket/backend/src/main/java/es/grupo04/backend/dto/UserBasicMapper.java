package es.grupo04.backend.dto;

import es.grupo04.backend.model.User;

public class UserBasicMapper {

    public UserBasicDTO toDTO(User user) {
        return new UserBasicDTO(user.getId(), user.getName());
    }
    
}
