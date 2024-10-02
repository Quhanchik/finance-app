package kz.quhan.finance_app.service;

import kz.quhan.finance_app.dto.LoginRes;
import kz.quhan.finance_app.dto.RegistrationRes;
import kz.quhan.finance_app.dto.UserReq;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.exception.AppUserException;
import kz.quhan.finance_app.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserService appUserService;
    private final PasswordEncoder encoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager manager;

    public LoginRes login(UserReq userReq) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                userReq.getLogin(),
                userReq.getPassword()
        ));

        Optional<AppUser> userRes = appUserService.findUserByLogin(userReq.getLogin());

        String accessToken = jwtUtils.generateAccessToken(userReq.getLogin());

        return new LoginRes(accessToken, userRes.get());
    }

    public RegistrationRes registration(UserReq userReq) {
        Optional<AppUser> userOptional = appUserService.findUserByLogin(userReq.getLogin());

        if(userOptional.isPresent()) {
            throw new AppUserException("user with this id is already exist");
        }

        AppUser createdUser = appUserService.create(new AppUser(userReq.getLogin(), encoder.encode(userReq.getPassword())));

        String accessToken = jwtUtils.generateAccessToken(userReq.getLogin());

        return new RegistrationRes(accessToken, createdUser);
    }
}
