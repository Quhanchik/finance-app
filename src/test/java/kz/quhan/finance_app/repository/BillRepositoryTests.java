package kz.quhan.finance_app.repository;


import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BillRepositoryTests {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void beforeEach() {
        billRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get pageable list of bills filtered by creators login")
    public void givenCreatorLogin_whenGetBillsByCreatorLogin_thenPageableBillsReturned() {
        //given
        AppUser user1 = DataUtils.getMikeSmithTransient();
        AppUser user2 = DataUtils.getJohnDoeTransient();
        appUserRepository.saveAll(List.of(user1, user2));

        Bill bill1 = DataUtils.getBill1Transient(user1);
        Bill bill2 = DataUtils.getBill2Transient(user2);
        billRepository.saveAll(List.of(bill2, bill1));
        //when
        Page<Bill> obtainedBills = billRepository.getBillsByCreatorLogin(bill1.getCreator().getLogin(), PageRequest.of(0, 5));
        //then
        assertThat(obtainedBills.getTotalElements()).isEqualTo(1);
        assertThat(CollectionUtils.isEmpty(obtainedBills.getContent())).isFalse();
    }

    @Test
    @DisplayName("Test get pageable list of bills filtered by creators login")
    public void givenIncorrectCreatorLogin_whenGetBillsByCreatorLogin_thenEmptyPageableReturned() {
        //given
        AppUser user1 = DataUtils.getMikeSmithTransient();
        AppUser user2 = DataUtils.getJohnDoeTransient();
        appUserRepository.saveAll(List.of(user1, user2));

        String login = "incorrectLogin";

        Bill bill1 = DataUtils.getBill1Transient(user1);
        Bill bill2 = DataUtils.getBill2Transient(user2);
        billRepository.saveAll(List.of(bill2, bill1));
        //when
        Page<Bill> obtainedBills = billRepository.getBillsByCreatorLogin(login, PageRequest.of(0, 5));
        //then
        assertThat(obtainedBills.getTotalElements()).isEqualTo(0);
        assertThat(CollectionUtils.isEmpty(obtainedBills.getContent())).isTrue();
    }
}
