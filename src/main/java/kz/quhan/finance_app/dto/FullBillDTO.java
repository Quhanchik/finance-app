package kz.quhan.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link kz.quhan.finance_app.entity.Bill}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FullBillDTO implements Serializable {
    Integer id;
    String name;
    String description;
    Double totalMoney;
    Double totalIncome;
    Double totalExpenses;
    AppUserDTO creator;
    List<AppUserDTO> members;
}