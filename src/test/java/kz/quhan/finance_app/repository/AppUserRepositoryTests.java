package kz.quhan.finance_app.repository;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppUserRepositoryTests {
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get user by login functionality")
    public void givenEmail_whenGetAppUserByLogin_thenAppUserIsReturned() {
        //given
        AppUser user = DataUtils.getJohnDoeTransient();
        appUserRepository.save(user);
        //when
        AppUser obtainedUser = appUserRepository.getAppUsersByLogin(user.getLogin())
                .orElse(null);
        //then
        assertThat(obtainedUser).isNotNull();
        assertThat(obtainedUser.getLogin()).isEqualTo(user.getLogin());
    }

    @Test
    @DisplayName("Test get user by login return null functionality")
    public void givenIncorrectEmail_whenGetAppUserByLogin_thenNullIsReturned() {
        //given
        String email = "test@mail.com";
        //when
        AppUser obtainedUser = appUserRepository.getAppUsersByLogin(email)
                .orElse(null);
        //then
        assertThat(obtainedUser).isNull();
    }
}
