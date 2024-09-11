package kz.quhan.finance_app.dto;

import kz.quhan.finance_app.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRes {
    private String accessToken;
    private AppUser user;
}
