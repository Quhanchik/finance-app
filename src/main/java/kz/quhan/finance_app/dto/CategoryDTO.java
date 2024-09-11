package kz.quhan.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kz.quhan.finance_app.entity.Category}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO implements Serializable {
    Integer id;
    String name;
}