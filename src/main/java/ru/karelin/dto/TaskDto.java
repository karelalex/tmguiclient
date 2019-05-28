package ru.karelin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import ru.karelin.enumeration.Status;
import java.util.Date;


public class TaskDto extends AbstractEntity {
    private String name = "Имя Задачи";

    private String description = "Описание задачи";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date startingDate = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date finishDate = new Date();

    private Status status = Status.PLANNED;

    private String  projectId;

    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
