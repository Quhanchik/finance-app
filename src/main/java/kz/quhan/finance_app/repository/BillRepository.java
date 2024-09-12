package kz.quhan.finance_app.repository;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "SELECT b.* FROM finance_app.t_bill b JOIN finance_app.t_user_bill AS tub ON b.id = tub.bill_id where user_id = :userId",
            countQuery = "SELECT count(b.*) FROM finance_app.t_bill b JOIN finance_app.t_user_bill AS tub ON b.id = tub.bill_id where user_id = :userId",
            nativeQuery = true)
    Page<Bill> gettingBillsByUserId(@Param("userId") Integer userId, Pageable pageable);
}