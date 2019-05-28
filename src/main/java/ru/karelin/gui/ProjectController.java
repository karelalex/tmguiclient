package ru.karelin.gui;

import com.sun.istack.internal.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import ru.karelin.enumeration.Status;
import ru.karelin.factory.DateEditingCell;
import ru.karelin.factory.StatusComboBoxEditingCell;
import ru.karelin.rest.ProjectService;
import ru.karelin.rest.SessionService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ProjectController extends Controller implements Initializable, ApplicationContextAware {
    @Autowired
    ProjectService projectService;

    @Autowired
    SessionService sessionService;

    @Autowired
    TaskController taskController;

    private ObservableList<ProjectDto> projectList;

    @FXML
    private TableColumn<ProjectDto, String> colId;

    @FXML
    private TableColumn<ProjectDto, String> colName;

    @FXML
    private TableColumn<ProjectDto, String> colDesc;

    @FXML
    private TableColumn<ProjectDto, Date> colStartDate;

    @FXML
    private TableColumn<ProjectDto, Date> colFinishDate;

    @FXML
    private TableColumn<ProjectDto, Status> colStatus;

    @FXML
    private TableView<ProjectDto> projectTable;

    public void initialize(URL location, ResourceBundle resources) {
        projectList = FXCollections.observableArrayList();
        projectList.addAll(projectService.getProjectList());

        Callback<TableColumn<ProjectDto, Date>, TableCell<ProjectDto, Date>> dateCellFactory
                = (TableColumn<ProjectDto, Date> param) -> new DateEditingCell<>();
        Callback<TableColumn<ProjectDto, Status>, TableCell<ProjectDto, Status>> comboCellFactory
                = (TableColumn<ProjectDto, Status> param) -> new StatusComboBoxEditingCell<>();
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(
                (TableColumn.CellEditEvent<ProjectDto, String> t) -> {
                    ProjectDto projectDto = t.getTableView().getItems()
                            .get(t.getTablePosition().getRow());
                    projectDto.setName(t.getNewValue());
                    if (!projectService.update(projectDto)) {
                        projectDto.setName(t.getOldValue());
                    }

                });
        colDesc.setCellFactory(TextFieldTableCell.forTableColumn());
        colDesc.setOnEditCommit((TableColumn.CellEditEvent<ProjectDto, String> t) -> {
            ProjectDto projectDto = t.getTableView().getItems().get(t.getTablePosition().getRow());
            projectDto.setDescription(t.getNewValue());
            if (!projectService.update(projectDto)) {
                projectDto.setDescription(t.getOldValue());
            }
        });
        colStartDate.setCellFactory(dateCellFactory);
        colStartDate.setOnEditCommit((TableColumn.CellEditEvent<ProjectDto, Date> t) -> {
            final ProjectDto project = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            project.setStartingDate(t.getNewValue());
            if(!projectService.update(project)){
                project.setStartingDate(t.getOldValue());
            }
        });
        colFinishDate.setCellFactory(dateCellFactory);
        colFinishDate.setOnEditCommit((TableColumn.CellEditEvent<ProjectDto, Date> t) -> {
            final ProjectDto project = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            project.setFinishDate(t.getNewValue());
            if(!projectService.update(project)){
                project.setFinishDate(t.getOldValue());
            }
        });
        colStatus.setCellFactory(comboCellFactory);
        colStatus.setCellValueFactory(new PropertyValueFactory<ProjectDto, Status>("status"));
        colStatus.setOnEditCommit((TableColumn.CellEditEvent<ProjectDto, Status> t) -> {
            ProjectDto project = t.getTableView().getItems()
                    .get(t.getTablePosition().getRow());
            project.setStatus(t.getNewValue());
            if (!projectService.update(project)) {
                project.setStatus(t.getOldValue());
            }
        });
        projectTable.setItems(projectList);
    }


    public void createProject() {
        ProjectDto projectDto = projectService.create();
        if (projectDto != null) projectList.add(projectDto);
    }

    public void removeProject() {
        ProjectDto project = projectTable.getSelectionModel().getSelectedItem();
        if (projectService.delete(project)) {
            projectList.remove(project);
        }
    }

    public void updateList() {
        projectList.clear();
        projectList.addAll(projectService.getProjectList());
    }

    public void logout() throws IOException {
        if(sessionService.logout()){
            StageLoader.loadMain().show();
            projectTable.getScene().getWindow().hide();
        }
    }
    public void showTasks() throws IOException {
        @Nullable final ProjectDto projectDto = projectTable.getSelectionModel().getSelectedItem();
        if(projectDto==null) {
            taskController.setProjectId(null);
        }
        else {
            taskController.setProjectId(projectDto.getId());
        }
        Stage stage = (Stage) projectTable.getScene().getWindow();
        stage.setTitle("Список задач");
        stage.setScene(StageLoader.loadTasks());

    }


}
