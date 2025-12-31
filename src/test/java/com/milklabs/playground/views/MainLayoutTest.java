package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;

class MainLayoutTest {

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void shouldCreateDrawerLinks_whenRoutesRegistered() {
        MockVaadin.setup();

        // RouterLink requires routes to be registered (MockVaadin doesn't scan @Route automatically)
        RouteConfiguration.forSessionScope().setRoute("", WelcomeView.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("list-category", ListCategory.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("list-product", ListProduct.class, MainLayout.class);

        MainLayout layout = new MainLayout();

        List<String> linkTexts = ViewTestUtils.findComponents(layout, RouterLink.class).stream()
                .map(RouterLink::getText)
                .collect(Collectors.toList());

        // MainLayout currently exposes only these two drawer links.
        assertEquals(List.of("List Categories", "List Products"), linkTexts);
    }
}
