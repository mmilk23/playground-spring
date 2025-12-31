package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.milklabs.playground.entity.Category;
import com.milklabs.playground.entity.Product;
import com.vaadin.flow.component.textfield.IntegerField;

class ProductFormTest {

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    private static Category cat(Integer id, String name) {
        Category c = new Category();
        c.setCategoryId(id);
        c.setCategoryName(name);
        return c;
    }

    @Test
    void setProduct_shouldToggleIdReadOnly_basedOnIdPresence() {
        MockVaadin.setup();

        ProductForm form = new ProductForm(List.of(), List.of(cat(1, "Electronics")));

        IntegerField id = ViewTestUtils.findIntegerFieldByLabel(form, "#ID").orElseThrow();

        Product p1 = new Product();
        p1.setProductId(null);
        p1.setProductName("X");
        p1.setProductPrice(1.0);
        p1.setProductCategory(cat(1, "Electronics"));
        form.setProduct(p1);
        assertFalse(id.isReadOnly(), "Null id should keep #ID editable");

        Product p2 = new Product();
        p2.setProductId(10);
        p2.setProductName("Y");
        p2.setProductPrice(2.0);
        p2.setProductCategory(cat(1, "Electronics"));
        form.setProduct(p2);
        assertTrue(id.isReadOnly(), "Non-null id should make #ID read-only");
    }

    @Test
    void saveClick_shouldFireSaveEvent() {
        MockVaadin.setup();

        ProductForm form = new ProductForm(List.of(), List.of(cat(1, "Electronics")));
        Product bean = new Product();
        bean.setProductName("Test Product");
        bean.setProductPrice(9.9);
        bean.setProductCategory(cat(1, "Electronics"));
        form.setProduct(bean);

        AtomicReference<Product> saved = new AtomicReference<>();
        form.addSaveListener(e -> saved.set(e.getProduct()));

        ViewTestUtils.findButtonByText(form, "Save").orElseThrow().click();

        assertNotNull(saved.get());
        assertEquals("Test Product", saved.get().getProductName());
    }

    @Test
    void deleteClick_shouldFireDeleteEvent() {
        MockVaadin.setup();

        ProductForm form = new ProductForm(List.of(), List.of(cat(1, "Electronics")));
        Product bean = new Product();
        bean.setProductId(99);
        bean.setProductName("ToDelete");
        bean.setProductPrice(1.0);
        bean.setProductCategory(cat(1, "Electronics"));
        form.setProduct(bean);

        AtomicReference<Product> deleted = new AtomicReference<>();
        form.addDeleteListener(e -> deleted.set(e.getProduct()));

        ViewTestUtils.findButtonByText(form, "Delete").orElseThrow().click();

        assertNotNull(deleted.get());
        assertEquals(99, deleted.get().getProductId());
    }

    @Test
    void closeClick_shouldFireCloseEvent() {
        MockVaadin.setup();

        ProductForm form = new ProductForm(List.of(), List.of(cat(1, "Electronics")));

        AtomicReference<Boolean> closed = new AtomicReference<>(false);
        form.addCloseListener(e -> closed.set(true));

        ViewTestUtils.findButtonByText(form, "Cancel").orElseThrow().click();

        assertTrue(closed.get());
    }
}
