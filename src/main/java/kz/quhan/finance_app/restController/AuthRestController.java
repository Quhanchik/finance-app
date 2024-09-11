package kz.quhan.finance_app.restController;

import kz.quhan.finance_app.dto.LoginRes;
import kz.quhan.finance_app.dto.RegistrationRes;
import kz.quhan.finance_app.dto.UserReq;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.service.AppUserService;
import kz.quhan.finance_app.utils.JWTUtils;
import kz.quhan.finance_app.utils.UserLoginExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AppUserService appUserService;
    private final PasswordEncoder encoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager manager;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserReq user) throws UserLoginExistException {
        Optional<AppUser> userOptional = appUserService.findUserByLogin(user.getLogin());

        if(userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("login is already exist");
        }

        AppUser userCreated = appUserService.create(new AppUser(user.getLogin(), encoder.encode(user.getPassword())));

        String accessToken = jwtUtils.generateAccessToken(user.getLogin());

        RegistrationRes res = new RegistrationRes(accessToken, userCreated);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserReq user) {
        System.out.println(user);
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getLogin(),
                user.getPassword()
        ));

        System.out.println("test");

        Optional<AppUser> userRes = appUserService.findUserByLogin(user.getLogin());

        String accessToken = jwtUtils.generateAccessToken(user.getLogin());

        LoginRes loginRes = new LoginRes(accessToken, userRes.get());

        return ResponseEntity.ok(loginRes);
    }
}
