package io.turntable.projectwebservice.services;

import io.turntable.projectwebservice.DomainTO.Project;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<List<Project>> getProjectByName(String productName);
    Project getProjectById(String id);
    void deleteProject(String productID);
    void addProject(Project project);
    void updateProject(Project project);
}