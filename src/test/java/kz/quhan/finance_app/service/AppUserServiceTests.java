package kz.quhan.finance_app.service;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.exception.AppUserException;
import kz.quhan.finance_app.repository.AppUserRepository;
import kz.quhan.finance_app.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTests {
    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    @DisplayName("Test getAppUserByLogin function")
    public void givenAppUserLogin_whenGetAppUserLoginByEmail_thenAppUserReturn() {
        //given
        AppUser appUser = DataUtils.getJohnDoePersistent();
        BDDMockito.given(appUserRepository.getAppUsersByLogin(anyString()))
                .willReturn(Optional.of(appUser));
        //when
        AppUser obtainedUser = appUserService.getAppUserByLogin("login");
        //then
        assertThat(obtainedUser).isNotNull();
    }

    @Test
    @DisplayName("Test getAppUserByLogin function")
    public void givenIncorrectAppUserLogin_whenGetAppUserLoginByEmail_thenExceptionThrown() {
        //given
        AppUser appUser = DataUtils.getJohnDoePersistent();
        BDDMockito.given(appUserRepository.getAppUsersByLogin(anyString()))
                .willThrow(AppUserException.class);
        //when
        assertThrows(AppUserException.class, () -> appUserService.getAppUserByLogin("login"));
        //then
    }
}
