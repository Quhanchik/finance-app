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
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {


    Page<Bill> getBillsByCreatorLogin(String userLogin, Pageable pageable);

    Optional<Bill> getBillByName(String name);
}