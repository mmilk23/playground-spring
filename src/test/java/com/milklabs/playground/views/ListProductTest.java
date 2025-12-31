package com.milklabs.playground.views;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.milklabs.playground.entity.Category;
import com.milklabs.playground.entity.Product;
import com.milklabs.playground.service.CategoryService;
import com.milklabs.playground.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;

class ListProductTest {

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

    private static Product prod(Integer id, String name, Category cat) {
        Product p = new Product();
        p.setProductId(id);
        p.setProductName(name);
        p.setProductPrice(9.9);
        p.setProductAvailable(true);
        p.setProductCategory(cat);
        return p;
    }

    @Test
    void viewShouldLoadGridItems_fromService() {
        MockVaadin.setup();

        ProductService prodSvc = mock(ProductService.class);
        CategoryService catSvc = mock(CategoryService.class);

        Category electronics = cat(1, "Electronics");
        Product kindle = prod(1, "Amazon Kindle", electronics);

        when(prodSvc.findAllProduct(anyString())).thenReturn(List.of(kindle));
        when(prodSvc.findAll()).thenReturn(List.of(kindle));
        when(catSvc.findAll()).thenReturn(List.of(electronics));

        ListProduct view = new ListProduct(prodSvc, catSvc);

        Grid<?> grid = ViewTestUtils.findComponents(view, Grid.class).stream()
                .findFirst().orElseThrow();

        assertEquals(1, ViewTestUtils.gridItemCount(grid));
    }

    @Test
    void addProduct_thenSave_shouldCallServiceSave() {
        MockVaadin.setup();

        ProductService prodSvc = mock(ProductService.class);
        CategoryService catSvc = mock(CategoryService.class);

        Category electronics = cat(1, "Electronics");
        when(prodSvc.findAllProduct(anyString())).thenReturn(List.of());
        when(prodSvc.findAll()).thenReturn(List.of());
        when(catSvc.findAll()).thenReturn(List.of(electronics));

        ListProduct view = new ListProduct(prodSvc, catSvc);

        Button add = ViewTestUtils.findButtonByText(view, "Add Product").orElseThrow();
        add.click();

        ViewTestUtils.findButtonByText(view, "Save").orElseThrow().click();

        verify(prodSvc, atLeastOnce()).saveProduct(any(Product.class));
    }

    @Test
    void delete_shouldCallServiceDelete() {
        MockVaadin.setup();

        ProductService prodSvc = mock(ProductService.class);
        CategoryService catSvc = mock(CategoryService.class);

        Category electronics = cat(1, "Electronics");
        Product kindle = prod(1, "Amazon Kindle", electronics);

        when(prodSvc.findAllProduct(anyString())).thenReturn(List.of(kindle));
        when(prodSvc.findAll()).thenReturn(List.of(kindle));
        when(catSvc.findAll()).thenReturn(List.of(electronics));

        ListProduct view = new ListProduct(prodSvc, catSvc);

        view.editProduct(kindle);

        ViewTestUtils.findButtonByText(view, "Delete").orElseThrow().click();

        verify(prodSvc, atLeastOnce()).deleteProduct(any(Product.class));
    }
}
