package kz.quhan.finance_app.mapper;

import kz.quhan.finance_app.dto.BillDto;
import kz.quhan.finance_app.dto.FullBillDTO;
import kz.quhan.finance_app.entity.Bill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {
    FullBillDTO toFullBillDTO(Bill bill);

    BillDto toBillDTO(Bill bill);

    List<FullBillDTO> toFullBillDTOs(List<Bill> bills);
}
