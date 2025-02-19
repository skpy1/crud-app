package com.example.controller;

import com.example.model.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditDialogController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;

    private Stage dialogStage;
    private Entity entity;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        idField.setText(entity.getId() != null ? entity.getId().toString() : "");
        nameField.setText(entity.getName());
        descriptionField.setText(entity.getDescription());

    }

    public boolean isOkClicked() { return okClicked; }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            entity.setName(nameField.getText());
            entity.setDescription(descriptionField.getText());
            isOkClicked();
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() { dialogStage.close(); }


    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty() || nameField.getText().length() < 3) {
            errorMessage += "Имя должно содержать от 3 до 50 символов и не может быть пустым.\n";
        }
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty() || descriptionField.getText().length() > 255) {
            errorMessage += "Описание должно содержать до 255 символов и не может быть пустым.\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Некорректные данные");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public boolean isSaved() { return okClicked; }
}
