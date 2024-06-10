package com.milklabs.playground.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.milklabs.playground.entity.Category;
import com.milklabs.playground.service.CategoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@Route(value = "list-category", layout = MainLayout.class)
@PageTitle("List of Category")
@RolesAllowed("ADMIN")
public class ListCategory extends VerticalLayout {

	private static final long serialVersionUID = -8065717201068451481L;

	private static final Logger log = LoggerFactory.getLogger(ListCategory.class);

	Grid<Category> grid = new Grid<>();
	TextField filterText = new TextField();
	CategoryForm form;
	CategoryService service;

	public ListCategory(CategoryService service) {
		log.info("[ListCategory] CategoryService {}", service);
		this.service = service;

		addClassName("list-view");
		setSizeFull();
		configureGrid();
		configureForm();
		add(getToolbar(), getContent());
		updateList();
		closeEditor();
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
		grid.addColumn(c -> c.getCategoryId()).setHeader("#ID").setSortable(true);
		grid.addColumn(c -> c.getCategoryName()).setHeader("Category Name").setSortable(true);
		grid.addColumn(c -> {
			Category categoryParent = c.getCategoryParent();
			return categoryParent != null ? categoryParent.getCategoryName() : "-";
		}).setHeader("Category Parent").setSortable(true);
		
		grid.addColumn(c -> getAllPrecedingCategories(c)).setHeader("Category Path").setSortable(false);

		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> editCategory(event.getValue()));
	}
	
	private String getAllPrecedingCategories(Category c) {
		StringBuilder str = new StringBuilder("");
		
		for (Category catPre: service.findAllPrecedingCategories(c.getCategoryId())) {
			str.append(" >> ");
			str.append(catPre.getCategoryName());
		}
		
		return str.toString().substring(4);
		
	}

	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		Button addCategoryBtn = new Button("Add Category");
		addCategoryBtn.addClickListener(click -> addCategory());

		var toolbar = new HorizontalLayout(filterText, addCategoryBtn);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void configureForm() {
		form = new CategoryForm(service.findAllCategory(""), service.findAllCategory(""));
		form.setWidth("25em");
		form.addSaveListener(this::saveCategory); 
	    form.addDeleteListener(this::deleteCategory); 
	    form.addCloseListener(e -> closeEditor());
	}

	private void updateList() {
		grid.setItems(service.findAllCategory(filterText.getValue()));
	}

	private void addCategory() {
		grid.asSingleSelect().clear();
		editCategory(new Category());
	}

	public void editCategory(Category category) {
		if (category == null) {
			closeEditor();
		} else {
			form.setCategory(category);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setCategory(null);
		form.setVisible(false);
		removeClassName("editing");
	}
	
	private void saveCategory(CategoryForm.SaveEvent event) {
	    service.saveCategory(event.getCategory());
	    updateList();
	    closeEditor();
	}

	private void deleteCategory(CategoryForm.DeleteEvent event) {
	    service.deleteCategory(event.getCategory());
	    updateList();
	    closeEditor();
	}

}
