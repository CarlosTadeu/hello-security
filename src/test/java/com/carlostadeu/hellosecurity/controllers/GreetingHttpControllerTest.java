package com.carlostadeu.hellosecurity.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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

    @Test
    void greetWithRequestPostProcessor() throws Exception {
        mvc.perform(get("/auth/greetings/C").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("spring")
    void greetWithAnnotation() throws Exception {
        mvc.perform(get("/auth/greetings/C"))
                .andExpect(status().isOk());
    }

    @Test
    void greetWithHttpBasic() throws Exception {
        mvc.perform(get("/auth/greetings/C").with(httpBasic("spring", "security")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void noAuthGreet() throws Exception {
        mvc.perform(get("/noauth/greetings/"))
                .andExpect(status().isOk());
    }
}
