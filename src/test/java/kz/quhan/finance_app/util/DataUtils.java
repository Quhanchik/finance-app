package kz.quhan.finance_app.util;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Role;

import java.util.Collections;
import java.util.List;

public class DataUtils {
    public static AppUser getJohnDoeTransient() {
        return new AppUser("john doe", "19283746511", List.of(getRoleUserPersisted()));
    }

    public static Role getRoleUserPersisted() {
        return new Role(1, "ROLE_USER");
    }
}
