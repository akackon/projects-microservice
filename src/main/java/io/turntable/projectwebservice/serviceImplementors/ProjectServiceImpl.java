package io.turntable.projectwebservice.serviceImplementors;

import io.turntable.projectwebservice.models.Project;
import io.turntable.projectwebservice.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    Logger log = LoggerFactory.getLogger(this.getClass());

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = jdbcTemplate.query("Select * from projects",
                BeanPropertyRowMapper.newInstance(Project.class));
        System.out.println("record retrieved successfully");
        log.info("all projects retrieved successfully");
        return projects;
    }

    @Override
    public Optional<List<Project>> getProjectByName(String productName) {
        log.info("searching project by name");
        Optional<List<Project>> projects = Optional.ofNullable(jdbcTemplate.query("select * from projects where project_name like ?",
                new Object[]{"%" + productName.toLowerCase() + "%"},
                BeanPropertyRowMapper.newInstance(Project.class)));
        return projects;
    }

    @Override
    public void addProject(Project project) {
        System.out.println("...inside add service" + project);
        jdbcTemplate.update("insert into projects (project_name, description) values (?, ?)",
                new Object[]{project.getProject_name(), project.getDescription()}
        );
        System.out.println("new project added successfully");
        log.info("added new project");
    }

    @Override
    public void deleteProject(int projectId) {
        jdbcTemplate.update("delete from projects where project_id = ?", new Object[]{projectId});
        System.out.println("project with id = " + projectId + " deleted successfully");
    }

    @Override
    public void updateProject(Project updatedProject) {
        this.jdbcTemplate.update(
                "update projects set project_name = ?, description = ? where project_id = ?",
                updatedProject.getProject_name(),
                updatedProject.getDescription(),
                updatedProject.getProject_id());
        System.out.println("project id = " + updatedProject.getProject_id() + " updated successfully");
        log.info("project id=" + updatedProject.getProject_id() + "updated");
    }

    @Override
    public Project getProjectById(int id) {
        if (id < 1) {
            log.warn("invalid project id access: " + id);
        }
        Project project = jdbcTemplate.queryForObject("select * from projects where project_id = ?",
                new Object[]{id},
                BeanPropertyRowMapper.newInstance(Project.class)
        );
        System.out.println("accessed project id = " + id);
        log.info("project id=" + id + "accessed");
        return project;
    }
}
