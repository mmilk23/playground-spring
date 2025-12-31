package com.milklabs.playground.views;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.milklabs.playground.entity.Product;
import com.milklabs.playground.service.CategoryService;
import com.milklabs.playground.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@Route(value = "list-product", layout = MainLayout.class)
@PageTitle("List of Products")
@RolesAllowed("ADMIN")
public class ListProduct extends VerticalLayout {

	private static final long serialVersionUID = -2399853765849233724L;

	private static final Logger log = LoggerFactory.getLogger(ListProduct.class);

	Grid<Product> grid = new Grid<>();
	TextField filterText = new TextField();
	ProductForm form;
	ProductService service;
	CategoryService categoryService;


	public ListProduct(ProductService service, CategoryService categoryService) {
		log.info("[ListProduct] ProductService {}", service);
		this.service = service;
		this.categoryService = categoryService;
		
		addClassName("list-view");
		setSizeFull();
		configureGrid();
		configureForm();
		add(getToolbar(), getContent());
		updateList();
		closeEditor();
	}
	
	private void configureForm() {
		log.info("[configureForm] ProductService {} CategoryService {} ", service, categoryService);
		
		form = new ProductForm(service.findAll(), categoryService.findAll());
		form.setWidth("25em");
		form.addSaveListener(this::saveProduct); 
	    form.addDeleteListener(this::deleteProduct); 
	    form.addCloseListener(e -> closeEditor());
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(grid, form);
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, form);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(p -> p.getProductId()).setHeader("#ID").setSortable(true);
		grid.addColumn(p -> p.getProductName()).setHeader("Name").setSortable(true);
		grid.addColumn(p -> p.getProductCategory().getCategoryName()).setHeader("Category").setSortable(true);
		grid.addColumn(p -> p.getProductPrice()).setHeader("Price").setSortable(true);
		grid.addComponentColumn(p -> createStatusIcon(p.getProductAvailable())).setHeader("Available?").setSortable(true);
		grid.addColumn(p -> p.getProductDescription()).setHeader("Description").setSortable(false);		
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> editProduct(event.getValue()));
	}
	
	private Icon createStatusIcon(Boolean isAvailable) {
        Icon icon;
        if (Boolean.TRUE.equals(isAvailable)) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
	
	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		Button addProductBtn = new Button("Add Product");
		addProductBtn.addClickListener(click -> addProduct());

		var toolbar = new HorizontalLayout(filterText, addProductBtn);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void updateList() {
		grid.setItems(service.findAllProduct(filterText.getValue()));
	}

	private void addProduct() {
		grid.asSingleSelect().clear();
		editProduct(new Product());
	}

	public void editProduct(Product product) {
		if (product == null) {
			closeEditor();
		} else {
			form.setProduct(product);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setProduct(null);
		form.setVisible(false);
		removeClassName("editing");
	}
	
	private void saveProduct(ProductForm.SaveEvent event) {
	    service.saveProduct(event.getProduct());
	    updateList();
	    closeEditor();
	}

	private void deleteProduct(ProductForm.DeleteEvent event) {
	    service.deleteProduct(event.getProduct());
	    updateList();
	    closeEditor();
	}

}
