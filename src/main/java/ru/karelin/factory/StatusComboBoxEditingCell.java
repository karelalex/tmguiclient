package ru.karelin.factory;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import ru.karelin.enumeration.Status;

public class StatusComboBoxEditingCell<T> extends TableCell<T, Status> {

        private ComboBox<Status> comboBox;

        public StatusComboBoxEditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                setText(null);
                setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().getDisplayName());
            setGraphic(null);
        }

        @Override
        public void updateItem(Status item, boolean empty) {
            super.updateItem(item, empty);
            if (item==null){
                setText(null);
            }
            else {
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (comboBox != null) {
                            comboBox.setValue(getItem());
                        }
                        setText(null);
                        setGraphic(comboBox);
                    } else {
                        setText(getItem().getDisplayName());
                        setGraphic(null);
                    }
                }
            }

        }

        private void createComboBox() {
            comboBox = new ComboBox<>();
            comboBox.setConverter(new StringConverter<Status>() {
                @Override
                public String toString(Status object) {
                    return object.getDisplayName();
                }

                @Override
                public Status fromString(String string) {
                    return Status.valueOf(string);
                }
            });
            comboBox.setCellFactory(new Callback<ListView<Status>, ListCell<Status>>(){

                @Override
                public ListCell<Status> call(ListView<Status> param) {
                    return new ListCell<Status>() {

                        @Override
                        protected void updateItem(Status item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.getDisplayName());
                            } else {
                                setText("");
                            }
                        }
                    };
                }
            });
            comboBox.setItems(FXCollections.observableArrayList(Status.values()));
            comboBox.valueProperty().set(getItem());
            comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            comboBox.setOnAction((e) -> {
                System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
                commitEdit(comboBox.getSelectionModel().getSelectedItem());
            });
//
        }
}
