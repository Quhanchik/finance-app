package kz.quhan.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for {@link kz.quhan.finance_app.entity.FinanceUnit}
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class FullFinanceUnitDTO {
    Integer id;
    Boolean isProfit;
    Double money;
    String description;
    Date timestamp;
    CategoryDTO category;
    AppUserDTO creator;
    BillDto bill;
}