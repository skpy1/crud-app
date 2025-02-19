package com.example.controller;

import com.example.model.Entity;
import com.example.service.EntityService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public class MainController {
    @FXML
    private TableView<Entity> tableView;
    @FXML
    private TableColumn<Entity, UUID> idColumn;
    @FXML
    private TableColumn<Entity, String> nameColumn;
    @FXML
    private TableColumn<Entity, String> descriptionColumn;
    @FXML
    private TableColumn<Entity, LocalDateTime> createdAtColumn;
    @FXML
    private TableColumn<Entity, LocalDateTime> updatedAtColumn;

    private final EntityService service = new EntityService();
    private final ObservableList<Entity> entities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("formattedUpdatedAt"));
        loadEntities();
    }


    private void loadEntities() {
        entities.setAll(service.getAllEntities());
        tableView.setItems(entities);
        tableView.refresh();
    }


    @FXML
    private void handleAdd() {
        showEditDialog(new Entity());
    }

    @FXML
    private void handleEdit() {
        Entity selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        }
    }

    @FXML
    private void handleDelete() {
        Entity selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Entity");
            alert.setHeaderText("Delete " + selected.getName() + "?");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                service.deleteEntity(selected.getId());
                loadEntities();
            }
        }
    }

    @FXML
    private void showEditDialog(Entity entity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/example/view/edit-dialog.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование записи");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EditDialogController controller = loader.getController();
            controller.setEntity(entity);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isSaved()) {
                if (entity.getId() == null) {
                    service.addEntity(entity);
                } else {
                    service.updateEntity(entity);
                }
                service.addEntity(entity);
                loadEntities();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
