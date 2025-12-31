package com.milklabs.playground.views;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.milklabs.playground.entity.Category;
import com.milklabs.playground.entity.Product;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

public class ProductForm extends FormLayout {

	private static final long serialVersionUID = -8824346316821442099L;
	
	private static final Logger log = LoggerFactory.getLogger(ProductForm.class);
	
	BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

	IntegerField  productId = new IntegerField ("#ID");
	TextField productName = new TextField("Name");
	ComboBox<Category> productCategory = new ComboBox<>("Category");
	TextArea productDescription = new TextArea ("Description");
	NumberField productPrice = new NumberField("Price");
	Checkbox productAvailable = new Checkbox("Available");

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");

	public ProductForm(List<Product> productList, List<Category> categoryList) {
		
		addClassName("product-form");
		binder.bindInstanceFields(this);
		productCategory.setItems(categoryList);
		productCategory.setItemLabelGenerator(Category::getCategoryName);
		productPrice.setPrefixComponent(VaadinIcon.DOLLAR.create());
		productDescription.setMinLength(5);
		productDescription.setMaxLength(255);

		productId.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				productId.setReadOnly(true);
			} else {
				productId.setReadOnly(false);
			}
		});
		productName.setRequired(true);
		productCategory.setRequired(true);
		productCategory.setRequiredIndicatorVisible(true);
		productDescription.setRequired(true);
		productPrice.setRequired(true);
		
		add(productId, productName, productCategory, productDescription, productPrice, productAvailable, createButtonsLayout());

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

		
		log.info("[createButtonsLayout] binder.isValid ? {}", binder.isValid());
		
		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}
	}

	public void setProduct(Product product) {
		binder.setBean(product);
	}

	public abstract static class ProductFormEvent extends ComponentEvent<ProductForm> implements Serializable {
		private static final long serialVersionUID = 3975659258019848690L;
		
		@Getter
		private Product product;

		protected ProductFormEvent(ProductForm source, Product p) {
			super(source, false);
			this.product = p;
		}
	}

	public static class SaveEvent extends ProductFormEvent {
		private static final long serialVersionUID = -3907520222847025462L;

		SaveEvent(ProductForm source, Product product) {
			super(source, product);
		}
	}

	public static class DeleteEvent extends ProductFormEvent {
		private static final long serialVersionUID = 1430468011063919968L;

		DeleteEvent(ProductForm source, Product product) {
			super(source, product);
		}
	}

	public static class CloseEvent extends ProductFormEvent {
		private static final long serialVersionUID = -9103794525679358562L;

		CloseEvent(ProductForm source) {
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
