package ru.karelin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.karelin.dto.ProjectDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectMapStorage {

    @Autowired
    ProjectRestControllerI projectRestController;
    private Map<String, ProjectDto> map = new HashMap<>();
    private boolean full=false;

    public void addItem(ProjectDto projectDto){
        map.put(projectDto.getId(), projectDto);
    }

    public void removeItem (ProjectDto projectDto) {
        map.remove(projectDto.getId());
        full=false;
    }

    public void updateItems(List<ProjectDto> projectDtoList) {
        map.clear();
        for (ProjectDto p : projectDtoList) {
            addItem(p);
        }
        full=true;
    }

    public ProjectDto getItem(String id){
        ProjectDto projectDto = map.get(id);
        if(projectDto==null){
            projectDto=projectRestController.getProject(id);
            addItem(projectDto);
        }
        return projectDto;
    }

    public List<ProjectDto> getAll() {
        if(full)
        return new ArrayList<ProjectDto>(map.values());
        else {
            final List<ProjectDto> projectList = projectRestController.getProjectList();
            updateItems(projectList);
            return projectList;
        }
    }
}
