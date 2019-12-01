package io.turntable.projectwebservice;

//import io.turntable.projectwebservice.log.Sub;

import io.turntable.projectwebservice.controllers.ProjectController;
import io.turntable.projectwebservice.log.Sub;
import io.turntable.projectwebservice.models.Project;
import io.turntable.projectwebservice.serviceImplementors.ProjectServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@EnableSwagger2
@SpringBootApplication
public class ProjectWebserviceApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(ProjectWebserviceApplication.class, args);
//		Sub.receiveMessage();
//	}
//
//	@Bean
//	public ProjectServiceImpl getProjectService(){
//		return new ProjectServiceImpl();
//	}
//
//	@Bean
//	public Sub sub() {
//		return new Sub();
//	}


    // with cli ui
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProjectWebserviceApplication.class, args);
        ProjectServiceImpl projectService = applicationContext.getBean(ProjectServiceImpl.class);
        Sub sub = applicationContext.getBean(Sub.class);
//        sub.receiveMessage();


        /////////////////////////////////
        System.out.println("...inside cli");
        Scanner sn = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println("\n##############\t Enter Option \t##################");
            System.out.println("##\t 1. View all projects");
            System.out.println("##\t 2. Search project");
            System.out.println("##\t 3. Delete project");
            System.out.println("##\t 4. Add new project");
            System.out.println("##\t 5. Quit this Application");
            System.out.println("####################################################");

            System.out.println("Select option:");
            userInput = sn.nextLine();

            switch (userInput) {
                case "1":
                    Thread getProjectThread = new Thread(() -> {
                        List<Project> projects = projectService.getAllProjects();
//                        System.out.println(projectService.getAllProjects());
                        List<Integer> ids = projects.stream().map(e -> e.getProject_id()).collect(Collectors.toList());
                        List<String> names = projects.stream().map(e -> e.getProject_name()).collect(Collectors.toList());
                        List<String> descs = projects.stream().map(e -> e.getDescription()).collect(Collectors.toList());
                        System.out.println(ids);
                        for (int i = 0; i < ids.size(); i++) {
                            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                            System.out.println("*****************************************");
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + ids.get(i) + AnsiConsole.RESET);
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + names.get(i) + AnsiConsole.RESET);
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + descs.get(i) + AnsiConsole.RESET);
                            System.out.println("*****************************************");
                        }
                    }, "retrieveProjectThread");
                    getProjectThread.start();
                    try {getProjectThread.join();} catch (InterruptedException e) {e.printStackTrace();}
                    break;

                case "5":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option");

            }
        }

    }


}
