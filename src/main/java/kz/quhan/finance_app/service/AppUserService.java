package kz.quhan.finance_app.service;

import kz.quhan.finance_app.entity.AppUser;
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

    public Optional<AppUser> getOne(Integer id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> getAppUserByLogin(String login) {
        return appUserRepository.getAppUsersByLogin(login);
    }

    public List<AppUser> getMany(Collection<Integer> ids) {
        return appUserRepository.findAllById(ids);
    }

    public AppUser create(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUser patch(AppUser id) {
        return appUserRepository.save(id);
    }

    public List<AppUser> patchMany(Collection<AppUser> ids) {
        return appUserRepository.saveAll(ids);
    }

    public void delete(AppUser id) {
        appUserRepository.delete(id);
    }

    public void deleteMany(Collection<Integer> ids) {
        appUserRepository.deleteAllById(ids);
    }
}
