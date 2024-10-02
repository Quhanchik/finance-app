package kz.quhan.finance_app.mapper;

import kz.quhan.finance_app.dto.AppUserDTO;
import kz.quhan.finance_app.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    AppUserDTO toDTO(AppUser appUser);
}
