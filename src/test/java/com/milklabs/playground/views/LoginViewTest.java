package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

class LoginViewTest {

	@BeforeEach
	void setupVaadin() {
		MockVaadin.setup();
	}

	@AfterEach
	void tearDownVaadin() {
		MockVaadin.tearDown();
	}

	@Test
	void shouldRenderHeadings_andLoginAction() {
		LoginView view = new LoginView();

		assertTrue(ViewTestUtils.findH1Containing(view, "Playground").isPresent(),
				"Expected an H1 containing 'Playground'");

		assertTrue(ViewTestUtils.findH2Containing(view, "Login using").isPresent(),
				"Expected an H2 containing 'Login using'");

		List<LoginForm> forms = ViewTestUtils.findComponents(view, LoginForm.class);
		assertEquals(1, forms.size(), "Expected exactly one LoginForm");
		LoginForm form = forms.get(0);

		assertEquals("login", form.getAction(), "LoginForm action must be 'login'");
		assertFalse(form.isError(), "LoginForm error should start as false");
	}

	@Test
	void beforeEnter_withErrorParam_shouldSetLoginErrorTrue() {
		LoginView view = new LoginView();

		LoginForm form = ViewTestUtils.findComponents(view, LoginForm.class).get(0);
		assertFalse(form.isError());

		// Mock chain:
		// event.getLocation().getQueryParameters().getParameters().containsKey("error")
		BeforeEnterEvent event = mock(BeforeEnterEvent.class);
		Location location = mock(Location.class);
		QueryParameters qp = mock(QueryParameters.class);

		when(event.getLocation()).thenReturn(location);
		when(location.getQueryParameters()).thenReturn(qp);
		when(qp.getParameters()).thenReturn(Map.of("error", List.of("1")));

		view.beforeEnter(event);

		assertTrue(form.isError(), "LoginForm error should become true when ?error is present");
	}

	@Test
	void beforeEnter_withoutErrorParam_shouldNotSetError() {
		LoginView view = new LoginView();

		LoginForm form = ViewTestUtils.findComponents(view, LoginForm.class).get(0);
		assertFalse(form.isError());

		BeforeEnterEvent event = mock(BeforeEnterEvent.class);
		Location location = mock(Location.class);
		QueryParameters qp = mock(QueryParameters.class);

		when(event.getLocation()).thenReturn(location);
		when(location.getQueryParameters()).thenReturn(qp);
		when(qp.getParameters()).thenReturn(Map.of()); // no "error"

		view.beforeEnter(event);

		assertFalse(form.isError(), "LoginForm error should remain false when ?error is absent");
	}
}
