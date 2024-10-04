package kz.quhan.finance_app.service;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.exception.BillException;
import kz.quhan.finance_app.repository.AppUserRepository;
import kz.quhan.finance_app.repository.BillRepository;
import kz.quhan.finance_app.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillServiceTests {
    @Mock
    private BillRepository billRepository;
    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private BillService billService;


//    @BeforeEach
//    public void setUp() {
//        Authentication authentication = new UsernamePasswordAuthenticationToken("johnDoe", "password");
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

    @Test
    @DisplayName("test bill save function")
    public void givenBillToSave_whenSave_thenReturnSavedBill() {
        //given
        AppUser user = DataUtils.getJohnDoePersistent();
        Bill billToSave = DataUtils.getBill1Transient(user);
        Bill savedBill = DataUtils.getBill1Persistent(user);
        UserDetails userDetails = new User(
                user.getLogin(),
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        BDDMockito.given(billRepository.getBillByName(anyString()))
                .willReturn(Optional.empty());
        BDDMockito.given(billRepository.save(any(Bill.class)))
                .willReturn(savedBill);
        //when
        Bill obtainedBill = billService.create(billToSave, userDetails);
        //then
        assertThat(obtainedBill).isNotNull();
        assertThat(obtainedBill).isEqualTo(savedBill);
    }

    @Test
    @DisplayName("test bill save function")
    public void givenDuplicatedBillToSave_whenSave_thenThrowError() {
        //given
        AppUser user = DataUtils.getJohnDoePersistent();
        Bill billToSave = DataUtils.getBill1Transient(user);
        Bill savedBill = DataUtils.getBill1Persistent(user);
        UserDetails userDetails = new User(
                user.getLogin(),
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        BDDMockito.given(billRepository.getBillByName(anyString()))
                .willReturn(Optional.of(savedBill));
        //when
        assertThrows(BillException.class, () -> billService.create(billToSave, userDetails));
        //then
    }
}
