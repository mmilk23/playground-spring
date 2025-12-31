package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.github.mvysny.kaributesting.v10.MockVaadin;

class WelcomeViewTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        MockVaadin.tearDown();
    }

    @Test
    void shouldShowWelcomeText_withCurrentUsername() {
        MockVaadin.setup();

        var principal = User.withUsername("admin").password("x").roles("ADMIN").build();
        var auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        WelcomeView view = new WelcomeView();

        // WelcomeView only renders a single H2 with the current username.
        assertTrue(ViewTestUtils.findH2Containing(view, "Welecome, admin").isPresent());
    }
}
