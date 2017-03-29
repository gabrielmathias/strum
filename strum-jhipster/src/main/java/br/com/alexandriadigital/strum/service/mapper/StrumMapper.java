package br.com.alexandriadigital.strum.service.mapper;

import br.com.alexandriadigital.strum.domain.*;
import br.com.alexandriadigital.strum.service.dto.StrumDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Strum and its DTO StrumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface StrumMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    StrumDTO strumToStrumDTO(Strum strum);

    List<StrumDTO> strumsToStrumDTOs(List<Strum> strums);

    @Mapping(source = "ownerId", target = "owner")
    Strum strumDTOToStrum(StrumDTO strumDTO);

    List<Strum> strumDTOsToStrums(List<StrumDTO> strumDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Strum strumFromId(Long id) {
        if (id == null) {
            return null;
        }
        Strum strum = new Strum();
        strum.setId(id);
        return strum;
    }
    

}
