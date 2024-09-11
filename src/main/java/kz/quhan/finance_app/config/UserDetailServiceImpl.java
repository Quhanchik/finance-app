package kz.quhan.finance_app.config;

import jakarta.transaction.Transactional;
import kz.quhan.finance_app.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import kz.quhan.finance_app.service.AppUserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final AppUserService appUserService;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUserOptional = appUserService.findUserByLogin(username);
        if(appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(appUser.getLogin())
                    .password(appUser.getPassword())
                    .authorities(appUser.getRoles().stream().map(role -> {
                        return new SimpleGrantedAuthority(role.getName());
                    }).toList())
                    .build();
        } else {
            throw new UsernameNotFoundException("user with this login not found");
        }
    }
}
