package kz.quhan.finance_app.util;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.entity.Role;

import java.util.List;

public class DataUtils {
    public static AppUser getJohnDoeTransient() {
        return new AppUser("johnDoe", "password", List.of(getRoleUserPersisted()));
    }

    public static AppUser getJohnDoePersistent() {
        return new AppUser(1, "johnDoe", "password", List.of(getRoleUserPersisted()));
    }

    public static AppUser getMikeSmithTransient() {
        return new AppUser("mikeSmith", "password", List.of(getRoleUserPersisted()));
    }

    public static AppUser getMikeSmithPersisted() {
        return new AppUser(2, "mikeSmith", "password", List.of(getRoleUserPersisted()));
    }

    public static Bill getBill1Transient(AppUser user) {
        return Bill.builder()
                .name("test1")
                .description("test")
                .financeUnits(null)
                .totalIncome(0)
                .totalExpenses(0)
                .totalMoney(0.0)
                .creator(user)
                .members(List.of(user))
                .build();
    }

    public static Bill getBill1Persistent(AppUser user) {
        return Bill.builder()
                .id(1)
                .name("test1")
                .description("test")
                .financeUnits(null)
                .totalIncome(0)
                .totalExpenses(0)
                .totalMoney(0.0)
                .creator(user)
                .members(List.of(user))
                .build();
    }

    public static Bill getBill2Transient(AppUser user) {
        return Bill.builder()
                .name("test2")
                .description("test")
                .financeUnits(null)
                .totalIncome(0)
                .totalExpenses(0)
                .totalMoney(0.0)
                .creator(user)
                .members(List.of(user))
                .build();
    }

    public static Bill getBill2Persistent(AppUser user) {
        return Bill.builder()
                .id(1)
                .name("test1")
                .description("test")
                .financeUnits(null)
                .totalIncome(0)
                .totalExpenses(0)
                .totalMoney(0.0)
                .creator(user)
                .members(List.of(user))
                .build();
    }

    public static Role getRoleUserPersisted() {
        return new Role(1, "ROLE_USER");
    }
}
