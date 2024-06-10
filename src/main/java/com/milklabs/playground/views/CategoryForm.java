package com.milklabs.playground.views;

import java.util.List;

import com.milklabs.playground.entity.Category;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

public class CategoryForm extends FormLayout {

	private static final long serialVersionUID = -8824346316821442099L;

	BeanValidationBinder<Category> binder = new BeanValidationBinder<>(Category.class);

	TextField categoryId = new TextField("#ID");
	TextField categoryName = new TextField("Category Name");
	ComboBox<Category> category = new ComboBox<>("Category");
	ComboBox<Category> categoryParent = new ComboBox<>("Parent Category");

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");

	public CategoryForm(List<Category> categories, List<Category> categoryParents) {
		addClassName("category-form");
		binder.bindInstanceFields(this);
		category.setItems(categories);
		category.setItemLabelGenerator(Category::getCategoryName);
		categoryParent.setItems(categoryParents);
		categoryParent.setItemLabelGenerator(Category::getCategoryName);
		categoryId.addValueChangeListener(event -> {
			if (event.getValue() != null && !event.getValue().isEmpty()) {
				categoryId.setReadOnly(true);
			} else {
				categoryId.setReadOnly(false);
			}
		});

		add(categoryId, categoryName, categoryParent, createButtonsLayout());

	};

	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}
	}

	public void setCategory(Category category) {
		binder.setBean(category);
	}

	public abstract static class CategoryFormEvent extends ComponentEvent<CategoryForm> {

		private static final long serialVersionUID = -6622013694139631111L;

		@Getter
		private Category category;

		protected CategoryFormEvent(CategoryForm source, Category contact) {
			super(source, false);
			this.category = contact;
		}

	}

	public static class SaveEvent extends CategoryFormEvent {
		private static final long serialVersionUID = 7748233493854443715L;

		SaveEvent(CategoryForm source, Category contact) {
			super(source, contact);
		}
	}

	public static class DeleteEvent extends CategoryFormEvent {
		private static final long serialVersionUID = -770535573947329612L;

		DeleteEvent(CategoryForm source, Category contact) {
			super(source, contact);
		}
	}

	public static class CloseEvent extends CategoryFormEvent {
		private static final long serialVersionUID = 6693293712499169018L;

		CloseEvent(CategoryForm source) {
			super(source, null);
		}
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
		return addListener(CloseEvent.class, listener);
	}

}
