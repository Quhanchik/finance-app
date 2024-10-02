package kz.quhan.finance_app.restController;

import kz.quhan.finance_app.dto.LoginRes;
import kz.quhan.finance_app.dto.RegistrationRes;
import kz.quhan.finance_app.dto.UserReq;
import kz.quhan.finance_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationRes> registration(@RequestBody UserReq userReq)  {
        RegistrationRes res = authService.registration(userReq);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserReq user) {
        LoginRes res = authService.login(user);

        return ResponseEntity.ok(res);
    }
}
