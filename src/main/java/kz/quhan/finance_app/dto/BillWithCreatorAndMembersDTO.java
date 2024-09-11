package kz.quhan.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link kz.quhan.finance_app.entity.Bill}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillWithCreatorAndMembersDTO implements Serializable {
    Integer id;
    String Name;
    String Description;
    Double totalMoney;
    Double totalIncome;
    Double totalExpenses;
    AppUserDTO creator;
    List<AppUserDTO> members;
}