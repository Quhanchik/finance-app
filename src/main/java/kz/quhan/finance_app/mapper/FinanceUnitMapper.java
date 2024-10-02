package kz.quhan.finance_app.mapper;

import kz.quhan.finance_app.dto.FullFinanceUnitDTO;
import kz.quhan.finance_app.entity.FinanceUnit;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FinanceUnitMapper {
    FullFinanceUnitDTO toFullFinanceUnitDTO(FinanceUnit financeUnit);

    FinanceUnit toEntity(FullFinanceUnitDTO fullFinanceUnitDTO);
}
