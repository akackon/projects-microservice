package io.turntable.projectwebservice.services;

import io.turntable.projectwebservice.models.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<List<Project>> getProjectByName(String productName);
    Project getProjectById(int id);
    void deleteProject(int productID);
    void addProject(Project project);
    void updateProject(Project project);
}
