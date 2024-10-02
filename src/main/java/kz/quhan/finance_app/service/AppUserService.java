package kz.quhan.finance_app.service;

import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.exception.AppUserException;
import kz.quhan.finance_app.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public Optional<AppUser> findUserByLogin(String login) {
        return appUserRepository.getAppUsersByLogin(login);
    }

    public Page<AppUser> getList(Pageable pageable) {
        return appUserRepository.findAll(pageable);
    }

    public AppUser getOne(Integer id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserException("user with this id does not found"));
    }

    public AppUser getAppUserByLogin(String login) {
        return appUserRepository.getAppUsersByLogin(login)
                .orElseThrow(() -> new AppUserException("user with this login doesn't exist"));
    }

    public List<AppUser> getMany(Collection<Integer> ids) {
        return appUserRepository.findAllById(ids);
    }

    public AppUser create(AppUser appUser) {
        Optional<AppUser> appUserFromDB = appUserRepository.getAppUsersByLogin(appUser.getLogin());

        if(appUserFromDB.isPresent()) {
            return appUserRepository.save(appUser);
        } else {
            throw new AppUserException("user with this id is already exist");
        }

    }

    public AppUser patch(AppUser id) {
        return appUserRepository.save(id);
    }

    public List<AppUser> patchMany(Collection<AppUser> ids) {
        return appUserRepository.saveAll(ids);
    }

    public void delete(Integer id) {
        Optional<AppUser> appUser = appUserRepository.findById(id);

        if(appUser.isEmpty()) {
            throw new AppUserException("user with this id doesn't exist");
        }

        appUserRepository.delete(
                AppUser.builder()
                .id(id)
                .build()
        );
    }

    public void deleteMany(Collection<Integer> ids) {
        appUserRepository.deleteAllById(ids);
    }
}
