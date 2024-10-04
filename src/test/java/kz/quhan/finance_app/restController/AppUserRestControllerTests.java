package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.quhan.finance_app.config.JWTFilter;
import kz.quhan.finance_app.dto.AppUserDTO;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.mapper.AppUserMapper;
import kz.quhan.finance_app.service.AppUserService;
import kz.quhan.finance_app.util.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = AppUserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
//@ActiveProfiles(value = "test")
public class AppUserRestControllerTests {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AppUserService appUserService;

    @MockBean
    private AppUserMapper appUserMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletResponse httpServletResponse;


    @Test
    @DisplayName("test get me function")
    @WithMockUser(username = "johnDoe", roles = {"ROLE_USER"})
    public void givenSavedUser_whenGetMe_thenMyAppUserReturned() throws Exception {
        //given
        AppUser user = DataUtils.getJohnDoePersistent();
        BDDMockito.given(appUserService.getAppUserByLogin(anyString()))
                        .willReturn(user);
        BDDMockito.given(appUserMapper.toDTO(any(AppUser.class)))
                .willReturn(AppUserDTO.builder()
                        .login(user.getLogin())
                        .build());
        //when
        ResultActions result = mockMvc.perform(get("/api/appUsers/me"));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()));
    }

}
