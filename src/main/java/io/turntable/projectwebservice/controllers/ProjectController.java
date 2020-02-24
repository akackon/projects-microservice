package io.turntable.projectwebservice.controllers;

import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporters.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpansProcessor;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.turntable.projectwebservice.Models.Project;
import io.turntable.projectwebservice.configurations.OTConfig;
import io.turntable.projectwebservice.serviceImplementors.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Api
@RestController
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @ApiOperation("get all projects")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping({"/project", "/"})
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @CrossOrigin
    @ApiOperation("get project by name")
    @GetMapping("/project/search/name/{name}")
    public Optional<List<Project>> getAllProjectByName(@PathVariable String name) {
        return projectService.getProjectByName(name);
    }

    @CrossOrigin
    @ApiOperation("add new project")
    @PostMapping("/project/add")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addProject(@RequestBody Project project) {
        projectService.addProject(project);
    }


    @CrossOrigin
    @ApiOperation("delete project")
    @DeleteMapping("/project/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
    }

    @CrossOrigin
    @ApiOperation("update existing project")
    @PutMapping("/project/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProjectRecord(@PathVariable String id, @RequestBody Project project) {
        Project projectToUpdate = projectService.getProjectById(id);
        projectToUpdate.setProject_name(project.getProject_name());
        projectToUpdate.setDescription(project.getDescription());
        projectService.updateProject(projectToUpdate);
    }

    @CrossOrigin
    @ApiOperation("get project by id")
    @GetMapping("/project/search/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Project getProjectById(@PathVariable String id) {
       return projectService.getProjectById(id);
    }

    @GetMapping("api/v1/docker")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String testDocker() {
        SpanProcessor spanProcessor = OTConfig.spanConfig();
        Tracer tracer = OTConfig.observabilityConfig(spanProcessor);

        Span span = tracer.spanBuilder("foo").startSpan();
        span.setAttribute("operation.id", 111);
        span.addEvent("operation.111");

        try (Scope scope = tracer.withSpan(span))
        {
            return projectService.getDocker(tracer);
        } finally {
            span.end();
            spanProcessor.shutdown();
        }
    }



//    ---------------------------------------------------------
//@GetMapping("api/v1/docker")
//@ResponseStatus(value = HttpStatus.CREATED)
//public String testDocker() {
//    SpanProcessor spanProcessor = OTConfig.spanConfig();
//    Tracer tracer = OTConfig.observabilityConfig(spanProcessor);
//
//    // Add a single Span.
//    Span span = tracer.spanBuilder("foo").startSpan();
//    span.setAttribute("operation.id", 111);
//    span.addEvent("operation.111");
//
//    // Set it as the current Span.
//    try (Scope scope = tracer.withSpan(span))   // same span closed at finally
//    {
//        // Add another Span, as an implicit child.
//        Span childSpan = tracer.spanBuilder("bar").startSpan();
//        childSpan.setAttribute("operation.id", 222);
//        childSpan.addEvent("operation.222");
//
//        try (Scope childScope = tracer.withSpan(childSpan)) {
//            Span childChildSpan = tracer.spanBuilder("goo").startSpan();
//            childChildSpan.setAttribute("operation.id", 333);
//            childChildSpan.addEvent("operation.333");
//
//            try(Scope childChildScope = tracer.withSpan(childChildSpan)) {
//                Span babyChild = tracer.spanBuilder("hoo").startSpan();
//                babyChild.setAttribute("operation.id", 444);
//                babyChild.addEvent("operation.444");
//            }finally {
//                childChildSpan.end();
//            }
//
//
//        } finally {
//            childSpan.end();
//
//        }
//
//    } finally {
//        span.end();
//    }
//    spanProcessor.shutdown();
//    return projectService.getDocker();
//}
}





/*
* NB
* @RequestMapping("/project/searchId/{id}") .... provides many controller verbs at Swagger-UI
* getProjectById() is needed for update
* */
