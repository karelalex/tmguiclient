package ru.karelin.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.karelin.dto.ProjectDto;
import ru.karelin.dto.TaskDto;
import ru.karelin.enumeration.Status;
import ru.karelin.factory.DateEditingCell;
import ru.karelin.factory.StatusComboBoxEditingCell;
import ru.karelin.rest.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class TaskController extends Controller implements Initializable, ApplicationContextAware {
    @Autowired
    TaskRestControllerI taskRestController;

    @Autowired
    LoginRestControllerI loginRestController;

    @Autowired
    ProjectMapStorage projectMapStorage;

    private String projectId;

    private ObservableList<TaskDto> taskList;

    @FXML
    private TableColumn<TaskDto, String> colId;

    @FXML
    private TableColumn<TaskDto, String> colProjectName;

    @FXML
    private TableColumn<TaskDto, String> colName;

    @FXML
    private TableColumn<TaskDto, String> colDesc;

    @FXML
    private TableColumn<TaskDto, Date> colStartDate;

    @FXML
    private TableColumn<TaskDto, Date> colFinishDate;

    @FXML
    private TableColumn<TaskDto, Status> colStatus;

    @FXML
    private TableView<TaskDto> taskTable;

    @FXML
    private Button changeProjectButton;

    public void initialize(URL location, ResourceBundle resources) {
        taskList = FXCollections.observableArrayList();
        taskList.addAll(taskRestController.getTaskList(projectId));

        Callback<TableColumn<TaskDto, Date>, TableCell<TaskDto, Date>> dateCellFactory
                = (TableColumn<TaskDto, Date> param) -> new DateEditingCell<TaskDto>();
        Callback<TableColumn<TaskDto, Status>, TableCell<TaskDto, Status>> comboCellFactory
                = (TableColumn<TaskDto, Status> param) -> new StatusComboBoxEditingCell<>();

        colProjectName.setCellValueFactory(cellData -> new SimpleStringProperty(projectMapStorage.getItem(cellData.getValue().getProjectId()).getName()));

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(
                (TableColumn.CellEditEvent<TaskDto, String> t) -> {
                    TaskDto taskDto = t.getTableView().getItems()
                            .get(t.getTablePosition().getRow());
                    taskDto.setName(t.getNewValue());
                    if (!taskRestController.editTask(taskDto).isSuccess()) {
                        taskDto.setName(t.getOldValue());
                    }

                });
        colDesc.setCellFactory(TextFieldTableCell.forTableColumn());
        colDesc.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, String> t) -> {
            TaskDto taskDto = t.getTableView().getItems().get(t.getTablePosition().getRow());
            taskDto.setDescription(t.getNewValue());
            if (!taskRestController.editTask(taskDto).isSuccess()) {
                taskDto.setDescription(t.getOldValue());
            }
        });
        colStartDate.setCellFactory(dateCellFactory);
        colStartDate.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Date> t) -> {
            final TaskDto taskDto = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            taskDto.setStartingDate(t.getNewValue());
            if (!taskRestController.editTask(taskDto).isSuccess()) {
                taskDto.setStartingDate(t.getOldValue());
            }
        });
        colFinishDate.setCellFactory(dateCellFactory);
        colFinishDate.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Date> t) -> {
            final TaskDto task = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            task.setFinishDate(t.getNewValue());
            if (!taskRestController.editTask(task).isSuccess()) {
                task.setFinishDate(t.getOldValue());
            }
        });
        colStatus.setCellFactory(comboCellFactory);
        colStatus.setCellValueFactory(new PropertyValueFactory<TaskDto, Status>("status"));
        colStatus.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Status> t) -> {
            TaskDto task = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            task.setStatus(t.getNewValue());
            if (!taskRestController.editTask(task).isSuccess()) {
                task.setStatus(t.getOldValue());
            }
        });
        taskTable.setItems(taskList);
        taskTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                changeProjectButton.setVisible(false);
                changeProjectButton.setManaged(false);
            } else {
                changeProjectButton.setVisible(true);
                changeProjectButton.setManaged(true);
            }
        }));
    }


    public void createTask() {
        String idForProject = null;
        if (projectId == null || projectId.isEmpty()) {
            ChoiceDialog<ProjectDto> choiceDialog = new ChoiceDialog<>();
            choiceDialog.getItems().addAll(projectMapStorage.getAll());
            choiceDialog.setTitle("Выбери проект");
            Optional<ProjectDto> optional = choiceDialog.showAndWait();
            if (optional.isPresent()) {
                idForProject = optional.get().getId();
            }

        } else {
            idForProject = projectId;
        }
        if (idForProject != null && !idForProject.isEmpty()) {
            TaskDto taskInit = new TaskDto();
            taskInit.setProjectId(idForProject);
            if(taskRestController.createTask(taskInit).isSuccess()){
                TaskDto taskDto = taskRestController.getTask(taskInit.getId());
                if (taskDto != null) taskList.add(taskDto);
            }

        }
    }

    public void removeTask() {
        TaskDto task = taskTable.getSelectionModel().getSelectedItem();
        if (taskRestController.removeTask(task.getId()).isSuccess()) {
            taskList.remove(task);
        }
    }

    public void updateList() {
        taskList.clear();
        taskList.addAll(taskRestController.getTaskList(projectId));
    }

    public void logout() throws IOException {
        if (loginRestController.logout().isSuccess()) {
            StageLoader.loadMain().show();
            taskTable.getScene().getWindow().hide();
        }
    }

    public void showAll() {
        projectId = null;
        updateList();
    }

    public void showProjects() throws IOException {
        Stage stage = (Stage) taskTable.getScene().getWindow();
        stage.setTitle("Список проектов");
        stage.setScene(StageLoader.loadProjects());
    }

    public void changeProject() {
        TaskDto taskDto = taskTable.getSelectionModel().getSelectedItem();
        if (taskDto == null) return;
        ChoiceDialog<ProjectDto> choiceDialog = new ChoiceDialog<>();
        choiceDialog.getItems().addAll(projectMapStorage.getAll());
        choiceDialog.setTitle("Выбор проекта");
        choiceDialog.setContentText("Выбирай проект из списка");
        Optional<ProjectDto> optional = choiceDialog.showAndWait();
        optional.ifPresent(projectDto -> {
            String oldProjectId = taskDto.getProjectId();
            taskDto.setProjectId(projectDto.getId());
            if(taskRestController.editTask(taskDto).isSuccess())
            taskTable.refresh();
            else taskDto.setProjectId(oldProjectId);
        });
        if (projectId != null && !projectId.isEmpty()) {
            optional.ifPresent(projectDto -> projectId = projectDto.getId());
            updateList();
        }

    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
