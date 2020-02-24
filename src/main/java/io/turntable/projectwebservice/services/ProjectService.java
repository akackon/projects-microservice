package io.turntable.projectwebservice.services;

import io.turntable.projectwebservice.Models.Project;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // applied on DAO
public interface ProjectService {
    List<Project> getAllProjects();
    Optional<List<Project>> getProjectByName(String productName);
    Project getProjectById(String id);
    void deleteProject(String productID);
    void addProject(Project project);
    void updateProject(Project project);
}
