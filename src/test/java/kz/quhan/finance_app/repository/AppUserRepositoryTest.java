package kz.quhan.finance_app.repository;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.util.DataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;
    @Test
    public void givenAppUserIsSaved_whenGetAppUserFromDB_thenAppUserIsObtainable() {
        //given
        AppUser userToSave = DataUtils.getJohnDoeTransient();
        AppUser savedUser = appUserRepository.save(userToSave);
        //when
        AppUser userFromDB = appUserRepository.findById(savedUser.getId()).orElse(null);
        //then
        assertThat(userFromDB).isNotNull();
    }
}
