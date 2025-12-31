package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.milklabs.playground.entity.Category;
import com.vaadin.flow.component.textfield.TextField;

class CategoryFormTest {

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void setCategory_shouldToggleIdReadOnly_basedOnIdPresence() {
        MockVaadin.setup();

        CategoryForm form = new CategoryForm(List.of(), List.of());

        TextField id = ViewTestUtils.findTextFieldByLabel(form, "#ID").orElseThrow();
        assertFalse(id.isReadOnly(), "When no bean is set, field starts editable");

        Category c1 = new Category();
        c1.setCategoryId(null);
        c1.setCategoryName("Electronics");
        form.setCategory(c1);
        assertFalse(id.isReadOnly(), "Null id should keep #ID editable");

        Category c2 = new Category();
        c2.setCategoryId(1);
        c2.setCategoryName("Books");
        form.setCategory(c2);
        assertTrue(id.isReadOnly(), "Non-null id should make #ID read-only");
    }

    @Test
    void saveClick_shouldFireSaveEvent() {
        MockVaadin.setup();

        CategoryForm form = new CategoryForm(List.of(), List.of());
        Category bean = new Category();
        bean.setCategoryName("Test");
        form.setCategory(bean);

        AtomicReference<Category> saved = new AtomicReference<>();
        form.addSaveListener(e -> saved.set(e.getCategory()));

        ViewTestUtils.findButtonByText(form, "Save").orElseThrow().click();

        assertNotNull(saved.get(), "Save event should fire");
        assertEquals("Test", saved.get().getCategoryName());
    }

    @Test
    void deleteClick_shouldFireDeleteEvent() {
        MockVaadin.setup();

        CategoryForm form = new CategoryForm(List.of(), List.of());
        Category bean = new Category();
        bean.setCategoryId(99);
        bean.setCategoryName("ToDelete");
        form.setCategory(bean);

        AtomicReference<Category> deleted = new AtomicReference<>();
        form.addDeleteListener(e -> deleted.set(e.getCategory()));

        ViewTestUtils.findButtonByText(form, "Delete").orElseThrow().click();

        assertNotNull(deleted.get(), "Delete event should fire");
        assertEquals(99, deleted.get().getCategoryId());
    }

    @Test
    void closeClick_shouldFireCloseEvent() {
        MockVaadin.setup();

        CategoryForm form = new CategoryForm(List.of(), List.of());

        AtomicReference<Boolean> closed = new AtomicReference<>(false);
        form.addCloseListener(e -> closed.set(true));

        ViewTestUtils.findButtonByText(form, "Cancel").orElseThrow().click();

        assertTrue(closed.get(), "Close event should fire");
    }
}
