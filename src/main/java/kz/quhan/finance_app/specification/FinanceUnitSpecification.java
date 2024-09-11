package kz.quhan.finance_app.specification;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Category;
import kz.quhan.finance_app.entity.FinanceUnit;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Date;

public class FinanceUnitSpecification {
    public static Specification<FinanceUnit> hasCategory(Integer categoryId) {
        return ((root, query, criteriaBuilder) -> categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId));
    }

    public static Specification<FinanceUnit> hasTimestamp(String timestamp) {
        return ((root, query, criteriaBuilder) -> timestamp == null ? null : criteriaBuilder.equal(root.get("timestamp"), parseStringToDate(timestamp)));
    }

    public static Specification<FinanceUnit> moreThanTimestamp(String timestampStart) {
        return ((root, query, criteriaBuilder) -> timestampStart == null ? null : criteriaBuilder.greaterThan(root.get("timestamp"), parseStringToDate(timestampStart)));
    }

    public static Specification<FinanceUnit> lessThanTimestamp(String timestampEnd) {
        return ((root, query, criteriaBuilder) -> timestampEnd == null ? null : criteriaBuilder.lessThan(root.get("timestamp"), parseStringToDate(timestampEnd)));
    }


    private static Date parseStringToDate(String timestamp) {
        long millis = Long.parseLong(timestamp);
        Instant instant = Instant.ofEpochMilli(millis);
        Date parsedDate = Date.from(instant);
        return parsedDate;
    }

    public static Specification<FinanceUnit> hasCreator(Integer creatorId) {
        return ((root, query, criteriaBuilder) -> creatorId == null ? null : criteriaBuilder.equal(root.get("creator").get("id"), creatorId));
    }

    public static Specification<FinanceUnit> hasBill(Integer billId) {
        return ((root, query, criteriaBuilder) -> billId == null ? null : criteriaBuilder.equal(root.get("bill").get("id"), billId));
    }
}
