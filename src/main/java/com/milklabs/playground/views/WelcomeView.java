package com.milklabs.playground.views;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Welcome")
@RolesAllowed({"ADMIN","USER"})
public class WelcomeView extends VerticalLayout{
	
	private static final long serialVersionUID = -8370078847865381654L;

	public WelcomeView() {
		addClassName("welcome-view");
		setSizeFull();
		add(new H2("Welecome, " + getCurrentUsername()));
	}
	
	private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return null;
        }
    }

}
