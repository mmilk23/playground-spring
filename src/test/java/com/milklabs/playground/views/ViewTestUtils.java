package com.milklabs.playground.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;

public final class ViewTestUtils {
    private ViewTestUtils() {}

    public static <T extends Component> List<T> findComponents(Component root, Class<T> type) {
        List<T> out = new ArrayList<>();
        walk(root, type, out);
        return out;
    }

    private static <T extends Component> void walk(Component c, Class<T> type, List<T> out) {
        if (type.isInstance(c)) out.add(type.cast(c));
        c.getChildren().forEach(ch -> walk(ch, type, out));
    }

    public static Optional<Button> findButtonByText(Component root, String text) {
        return findComponents(root, Button.class).stream()
                .filter(b -> Objects.equals(text, b.getText()))
                .findFirst();
    }

    public static Optional<TextField> findTextFieldByLabel(Component root, String label) {
        return findComponents(root, TextField.class).stream()
                .filter(f -> Objects.equals(label, f.getLabel()))
                .findFirst();
    }

    public static Optional<IntegerField> findIntegerFieldByLabel(Component root, String label) {
        return findComponents(root, IntegerField.class).stream()
                .filter(f -> Objects.equals(label, f.getLabel()))
                .findFirst();
    }

    public static Optional<NumberField> findNumberFieldByLabel(Component root, String label) {
        return findComponents(root, NumberField.class).stream()
                .filter(f -> Objects.equals(label, f.getLabel()))
                .findFirst();
    }

    public static Optional<Checkbox> findCheckboxByLabel(Component root, String label) {
        return findComponents(root, Checkbox.class).stream()
                .filter(f -> Objects.equals(label, f.getLabel()))
                .findFirst();
    }

    public static <T> Optional<ComboBox<T>> findComboBoxByLabel(Component root, String label) {
        // We have to locate ComboBox instances without knowing their generic parameter.
        @SuppressWarnings({"rawtypes", "unchecked"})
        List<ComboBox<?>> boxes = (List) findComponents(root, (Class) ComboBox.class);

        @SuppressWarnings({"rawtypes", "unchecked"})
        Optional<ComboBox<T>> match = (Optional) boxes.stream()
                .filter(cb -> Objects.equals(label, cb.getLabel()))
                .findFirst();
        return match;
    }

    public static Optional<H1> findH1Containing(Component root, String contains) {
        return findComponents(root, H1.class).stream()
                .filter(h -> h.getText() != null && h.getText().contains(contains))
                .findFirst();
    }

    public static Optional<H2> findH2Containing(Component root, String contains) {
        return findComponents(root, H2.class).stream()
                .filter(h -> h.getText() != null && h.getText().contains(contains))
                .findFirst();
    }

    public static int gridItemCount(Grid<?> grid) {
        return (int) grid.getDataProvider().fetch(new Query<>()).count();
    }
}
