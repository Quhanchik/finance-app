package kz.quhan.finance_app.repository;

import kz.quhan.finance_app.entity.FinanceUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceUnitRepository extends JpaRepository<FinanceUnit, Integer>, JpaSpecificationExecutor<FinanceUnit> {
//    @Query(value = "SELECT * FROM t_finance_unit where bill_id =:billId",
//            countQuery = "SELECT count(*) FROM t_finance_unit where bill_id=:billId",
//            nativeQuery = true)
    Page<FinanceUnit> getFinanceUnitsByBill_Id(@Param("billId") Integer billId,
                                               Pageable pageable,
                                               Specification<FinanceUnit> spec);
}