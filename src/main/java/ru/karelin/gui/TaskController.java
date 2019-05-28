package ru.karelin.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import ru.karelin.rest.ProjectMapStorage;
import ru.karelin.rest.SessionService;
import ru.karelin.rest.TaskService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class TaskController extends Controller implements Initializable, ApplicationContextAware {
    @Autowired
    TaskService taskService;

    @Autowired
    SessionService sessionService;

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

    public void initialize(URL location, ResourceBundle resources) {
        taskList = FXCollections.observableArrayList();
        taskList.addAll(taskService.getTaskList(projectId));

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
                    if (!taskService.update(taskDto)) {
                        taskDto.setName(t.getOldValue());
                    }

                });
        colDesc.setCellFactory(TextFieldTableCell.forTableColumn());
        colDesc.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, String> t) -> {
            TaskDto taskDto = t.getTableView().getItems().get(t.getTablePosition().getRow());
            taskDto.setDescription(t.getNewValue());
            if (!taskService.update(taskDto)) {
                taskDto.setDescription(t.getOldValue());
            }
        });
        colStartDate.setCellFactory(dateCellFactory);
        colStartDate.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Date> t) -> {
            final TaskDto task = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            task.setStartingDate(t.getNewValue());
            if (!taskService.update(task)) {
                task.setStartingDate(t.getOldValue());
            }
        });
        colFinishDate.setCellFactory(dateCellFactory);
        colFinishDate.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Date> t) -> {
            final TaskDto task = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            task.setFinishDate(t.getNewValue());
            if (!taskService.update(task)) {
                task.setFinishDate(t.getOldValue());
            }
        });
        colStatus.setCellFactory(comboCellFactory);
        colStatus.setCellValueFactory(new PropertyValueFactory<TaskDto, Status>("status"));
        colStatus.setOnEditCommit((TableColumn.CellEditEvent<TaskDto, Status> t) -> {
            TaskDto task = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            task.setStatus(t.getNewValue());
            if (!taskService.update(task)) {
                task.setStatus(t.getOldValue());
            }
        });
        taskTable.setItems(taskList);

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
            TaskDto taskDto = taskService.create(idForProject);
            if (taskDto != null) taskList.add(taskDto);
        }
    }

    public void removeTask() {
        TaskDto task = taskTable.getSelectionModel().getSelectedItem();
        if (taskService.delete(task)) {
            taskList.remove(task);
        }
    }

    public void updateList() {
        taskList.clear();
        taskList.addAll(taskService.getTaskList(projectId));
    }

    public void logout() throws IOException {
        if (sessionService.logout()) {
            StageLoader.loadMain().show();
            taskTable.getScene().getWindow().hide();
        }
    }

    public void showAll(){
        projectId=null;
        updateList();
    }

    public void showProjects() throws IOException {
        Stage stage = (Stage) taskTable.getScene().getWindow();
        stage.setTitle("Список проектов");
        stage.setScene(StageLoader.loadProjects());
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
