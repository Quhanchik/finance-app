package kz.quhan.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kz.quhan.finance_app.entity.Bill}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto implements Serializable {
    Integer id;
    String Name;
    String Description;
}