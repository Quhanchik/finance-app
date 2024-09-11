package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.quhan.finance_app.dto.AppUserDTO;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import kz.quhan.finance_app.service.AppUserService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appUsers")
@RequiredArgsConstructor
public class AppUserRestController {

    private final AppUserService appUserService;

    private final AppUserRepository appUserRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public Page<AppUser> getList(Pageable pageable) {
        return appUserService.getList(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        Optional<AppUser> appUserOptional = appUserService.getOne(id);

        if(appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();

            AppUserDTO appUserDTO = objectMapper.convertValue(appUser, AppUserDTO.class);

            return ResponseEntity.ok().body(appUserDTO);
        } else {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST, "user by this id doesn't found");

            return ResponseEntity.badRequest().body(problemDetail);
        }
    }

    @GetMapping("/by-ids")
    public List<AppUser> getMany(@RequestParam List<Integer> ids) {
        return appUserService.getMany(ids);
    }

    @PostMapping
    public AppUser create(@RequestBody AppUser appUser) {
        return appUserService.create(appUser);
    }

    @PatchMapping("/{id}")
    public AppUser patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(appUser).readValue(patchNode);

        return appUserService.patch(appUser);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<AppUser> appUsers = appUserRepository.findAllById(ids);

        for (AppUser appUser : appUsers) {
            objectMapper.readerForUpdating(appUser).readValue(patchNode);
        }

        List<AppUser> resultAppUsers = appUserService.patchMany(appUsers);
        List<Integer> ids1 = resultAppUsers.stream()
                .map(AppUser::getId)
                .toList();
        return ids1;
    }

    @DeleteMapping("/{id}")
    public AppUser delete(@PathVariable Integer id) {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if (appUser != null) {
            appUserService.delete(appUser);
        }
        return appUser;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        appUserService.deleteMany(ids);
    }
}
