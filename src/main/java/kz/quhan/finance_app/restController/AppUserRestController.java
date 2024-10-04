package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.quhan.finance_app.dto.AppUserDTO;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.mapper.AppUserMapper;
import kz.quhan.finance_app.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import kz.quhan.finance_app.service.AppUserService;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appUsers")
@RequiredArgsConstructor
public class AppUserRestController {

    private final AppUserService appUserService;

    private final AppUserMapper appUserMapper;

    @GetMapping("/me")
    public ResponseEntity<AppUserDTO> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = appUserService.getAppUserByLogin(userDetails.getUsername());

        AppUserDTO appUserDTO = appUserMapper.toDTO(appUser);

        return ResponseEntity.ok(appUserDTO);
    }
}
