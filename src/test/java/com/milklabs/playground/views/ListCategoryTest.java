package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.milklabs.playground.entity.Category;
import com.milklabs.playground.service.CategoryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;

class ListCategoryTest {

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    private static Category cat(Integer id, String name, Category parent) {
        Category c = new Category();
        c.setCategoryId(id);
        c.setCategoryName(name);
        c.setCategoryParent(parent);
        return c;
    }

    @Test
    void viewShouldLoadGridItems_fromService() {
        MockVaadin.setup();

        CategoryService svc = mock(CategoryService.class);

        Category electronics = cat(1, "Electronics", null);
        Category phones = cat(4, "Mobile Phones", electronics);

        when(svc.findAllCategory(anyString())).thenReturn(List.of(electronics, phones));
        when(svc.findAll()).thenReturn(List.of(electronics, phones));
        when(svc.findAllPrecedingCategories(eq(1))).thenReturn(List.of(electronics));
        when(svc.findAllPrecedingCategories(eq(4))).thenReturn(List.of(electronics, phones));

        ListCategory view = new ListCategory(svc);

        Grid<?> grid = ViewTestUtils.findComponents(view, Grid.class).stream()
                .findFirst().orElseThrow();

        assertEquals(2, ViewTestUtils.gridItemCount(grid));
    }

    @Test
    void addCategory_thenSave_shouldCallServiceSave() {
        MockVaadin.setup();

        CategoryService svc = mock(CategoryService.class);

        Category electronics = cat(1, "Electronics", null);
        when(svc.findAllCategory(anyString())).thenReturn(List.of(electronics));
        when(svc.findAll()).thenReturn(List.of(electronics));
        when(svc.findAllPrecedingCategories(anyInt())).thenReturn(List.of(electronics));

        ListCategory view = new ListCategory(svc);

        Button add = ViewTestUtils.findButtonByText(view, "Add Category").orElseThrow();
        add.click();

        // save with the default (empty) bean - still should trigger
        ViewTestUtils.findButtonByText(view, "Save").orElseThrow().click();

        verify(svc, atLeastOnce()).saveCategory(any(Category.class));
    }

    @Test
    void delete_shouldCallServiceDelete() {
        MockVaadin.setup();

        CategoryService svc = mock(CategoryService.class);
        Category electronics = cat(1, "Electronics", null);

        when(svc.findAllCategory(anyString())).thenReturn(List.of(electronics));
        when(svc.findAll()).thenReturn(List.of(electronics));
        when(svc.findAllPrecedingCategories(anyInt())).thenReturn(List.of(electronics));

        ListCategory view = new ListCategory(svc);

        // Select an item by calling the edit method directly (simpler than simulating grid selection)
        view.editCategory(electronics);

        ViewTestUtils.findButtonByText(view, "Delete").orElseThrow().click();

        verify(svc, atLeastOnce()).deleteCategory(any(Category.class));
    }
}
