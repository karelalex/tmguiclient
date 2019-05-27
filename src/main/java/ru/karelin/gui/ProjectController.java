package ru.karelin.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import ru.karelin.dto.ProjectDto;
import ru.karelin.enumeration.Status;
import ru.karelin.factory.DateEditingCell;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ProjectController extends Controller implements Initializable {
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
    private TableColumn<ProjectDto, String> colStatus;

    @FXML
    private TableView<ProjectDto> projectTable;

    public void initialize(URL location, ResourceBundle resources) {
        projectList = FXCollections.observableArrayList();
//todo сделать загрузку по ресту
        for (int i = 0; i < 10; i++) {
            ProjectDto project = new ProjectDto();
            project.setName("Проект " + i);
            project.setDescription("Очень важный " + i + "-ый проект");
            project.setStartingDate(new Date());
            project.setFinishDate(new Date());
            project.setStatus(Status.PLANNED);
            projectList.add(project);
        }
        Callback<TableColumn<ProjectDto, Date>, TableCell<ProjectDto, Date>> dateCellFactory
                = (TableColumn<ProjectDto, Date> param) -> new DateEditingCell();
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colDesc.setCellFactory(TextFieldTableCell.forTableColumn());
        colStartDate.setCellFactory(dateCellFactory);
        colFinishDate.setCellFactory(dateCellFactory);
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProjectDto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProjectDto, String> param) {
                return new SimpleStringProperty(param.getValue().getStatus().getDisplayName());
            }
        });
        projectTable.setItems(projectList);
    }
}
