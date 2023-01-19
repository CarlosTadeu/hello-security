package com.carlostadeu.hellosecurity.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingHttpControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // Test Specific Authentications
    @Test
    void greetWithHttpBasic() throws Exception {
        mvc.perform(get("/auth/greetings/Carlos").with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void greetWithWrongHttpBasic() throws Exception {
        mvc.perform(get("/auth/greetings/Carlos").with(httpBasic("user", "wrongpass")))
                .andExpect(status().isUnauthorized());
    }

    // Test Authentication Mocks
    @Test
    void greetWithRequestPostProcessor() throws Exception {
        mvc
                .perform(get("/auth/greetings/Carlos")
                        .with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails()
    void greetWithHttpBasicAndUserDetails() throws Exception {
        mvc.perform(get("/auth/greetings/Carlos").with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void greetWithAnnotation() throws Exception {
        mvc.perform(get("/auth/greetings/C"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails()
    void greetWithUserDetails() throws Exception {
        mvc.perform(get("/auth/greetings/C"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void greetWithAnonymousUser() throws Exception {
        mvc.perform(get("/auth/greetings/C"))
                .andExpect(status().isUnauthorized());
    }

    // Test No Auth
    @Test
    void noAuthGreet() throws Exception {
        mvc.perform(get("/noauth/greetings"))
                .andExpect(status().isOk());
    }
}
