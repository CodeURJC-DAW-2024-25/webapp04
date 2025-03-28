package es.grupo04.backend.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Report;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface ReportMapper {

    @Mapping(source="user", target="reportCreator")
    ReportDTO toDTO(Report report);

    List<ReportDTO> toDTOs(Collection<Report> reports);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "user", ignore = true) 
    Report toEntity(ReportDTO reportDTO);
}

