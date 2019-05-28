package ru.karelin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import ru.karelin.enumeration.Status;

import java.util.Date;


public class ProjectDto extends AbstractEntity {
    private String name = "Имя проекта";

    private String description = "Описание проекта";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date startingDate = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date finishDate = new Date();

    private Status status = Status.PLANNED;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name;
    }
}
