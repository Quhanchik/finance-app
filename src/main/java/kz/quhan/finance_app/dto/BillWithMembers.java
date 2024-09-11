package kz.quhan.finance_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kz.quhan.finance_app.entity.AppUser;
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
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BillWithMembers implements Serializable {
    Integer id;
    String Name;
    String Description;
    AppUser creator;
    List<AppUser> members;
}