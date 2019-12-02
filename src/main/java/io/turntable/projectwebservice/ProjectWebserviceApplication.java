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

import javax.swing.text.html.Option;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@EnableSwagger2
@SpringBootApplication
public class ProjectWebserviceApplication {

    private Object lock = new Object();
    private int idSearchUserInput;
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
        String mainMenuUserInput;

        while (true) {
            System.out.println("*************************************************************");
            System.out.println("*****  Welcome To Turntabl Project Management System   ******");
            System.out.println("*************************************************************");
            System.out.println("\n##############\t Menu Option \t##################");
            System.out.println("##\t 1. View all projects");
            System.out.println("##\t 2. Search project");
            System.out.println("##\t 3. Add new project");
            System.out.println("##\t 4. Update existing project");
            System.out.println("##\t 5. Delete project");
            System.out.println("##\t 6. Quit this Application");
            System.out.println("####################################################");

            System.out.println("Select option:");
            mainMenuUserInput = sn.nextLine();

            switch (mainMenuUserInput) {
                case "1":
                    Thread getProjectThread = new Thread(() -> {
                        List<Project> projects = projectService.getAllProjects();

//                        System.out.println(projectService.getAllProjects());
                        List<Integer> ids = projects.stream().map(e -> e.getProject_id()).collect(Collectors.toList());
                        List<String> names = projects.stream().map(e -> e.getProject_name()).collect(Collectors.toList());
                        List<String> descs = projects.stream().map(e -> e.getDescription()).collect(Collectors.toList());
//                        System.out.println(ids);

                        for (int i = 0; i < ids.size(); i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("*****************************************");
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + ids.get(i) + AnsiConsole.RESET);
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + names.get(i) + AnsiConsole.RESET);
                            System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + descs.get(i) + AnsiConsole.RESET);
                            System.out.println("*****************************************");
                        }
                    }, "retrieveProjectThread");

                    getProjectThread.start();
                    try {
                        getProjectThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case "2":
                    System.out.println("*********** Search Option *********");
                    System.out.println("1. Search by name");
                    System.out.println("2. Search by id");
                    System.out.println("\nKindly enter preferred search option: ");
//                    Scanner searchUserInput = new Scanner(System.in);
                    int searchChoiceUserInput = sn.nextInt();

                    switch (searchChoiceUserInput) {
                        case 1:
                            System.out.println("Enter project name to search for :) ");
                            String projectNameUserInput = sn.nextLine();        // hilarious
                            System.out.println("....ip1");
                            String searchByProjectNameUserInput = sn.nextLine();
                            System.out.println("....ip2");

                            List<Project> projectsByNameSearch = projectService.getProjectByName(searchByProjectNameUserInput);
                            if (projectsByNameSearch.isEmpty()) {
                                System.out.printf("Sorry...No record found: %s \n", searchByProjectNameUserInput);
                            } else {
                                List<Integer> ids = projectsByNameSearch.stream().map(e -> e.getProject_id()).collect(Collectors.toList());
                                List<String> names = projectsByNameSearch.stream().map(e -> e.getProject_name()).collect(Collectors.toList());
                                List<String> descs = projectsByNameSearch.stream().map(e -> e.getDescription()).collect(Collectors.toList());

                                for (int i = 0; i < ids.size(); i++) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("*****************************************");
                                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + ids.get(i) + AnsiConsole.RESET);
                                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + names.get(i) + AnsiConsole.RESET);
                                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + descs.get(i) + AnsiConsole.RESET);
                                    System.out.println("*****************************************");
                                }
                            }
                            break;

                        case 2:
                            System.out.println("Enter project id to search for ");
                            int idSearchUserInput = sn.nextInt();
//                            if(idSearchUserInput){}
                            Project projectSearchById = projectService.getProjectById(idSearchUserInput);

                            if (projectSearchById == null) {
                                System.out.printf("Sorry...Invalid project name: %s", mainMenuUserInput);
                            } else {
//                                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                                System.out.println("*****************************************");
                                System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + projectSearchById.getProject_id() + AnsiConsole.RESET);
                                System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + projectSearchById.getProject_name() + AnsiConsole.RESET);
                                System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + projectSearchById.getDescription() + AnsiConsole.RESET);
                                System.out.println("*****************************************\n");
                            }
                            break;
                        default:
                            System.out.println("Invalid option: " + searchChoiceUserInput);
                            break;

                    }

                    break;
                case "3":
                    System.out.println("Enter project name? ");
                    String projectName = sn.nextLine().toLowerCase();
                    System.out.println("Enter project description? ");
                    String projectDesc = sn.nextLine().toLowerCase();
                    Project newProject = new Project();
                    newProject.setProject_name(projectName);
                    newProject.setDescription(projectDesc);
                    projectService.addProject(newProject);
                    System.out.println(AnsiConsole.GREEN + "Project added successfully" + AnsiConsole.RESET);
                    break;

                case "4":
                    System.out.println("Enter project id to update: ");
                    int idUserInput = sn.nextInt();

                    Project projectToUpdate = projectService.getProjectById(idUserInput);
                    System.out.println("*****************************************");
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + projectToUpdate.getProject_id() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + projectToUpdate.getProject_name() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + projectToUpdate.getDescription() + AnsiConsole.RESET);
                    System.out.println("*****************************************\n");

                    Scanner inp = new Scanner(System.in);
                    System.out.println("Enter new project name? ");
                    String projNmUpt = inp.nextLine().toLowerCase();
//                    String projectNameUpdate = sn.nextLine().toLowerCase();
                    System.out.println("Enter project description? ");
//                    String projectDescUpdate = sn.nextLine().toLowerCase();
                    String projDecUpt = inp.nextLine();

                    projectToUpdate.setProject_name(projNmUpt);
                    projectToUpdate.setDescription(projDecUpt);

                    projectService.updateProject(projectToUpdate);
                    System.out.println("******************* updated **********************");
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + projectToUpdate.getProject_id() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + projectToUpdate.getProject_name() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + projectToUpdate.getDescription() + AnsiConsole.RESET);
                    System.out.println("*************************************************");
                    System.out.println(AnsiConsole.GREEN + "Project with id=" + idUserInput + " updated successfully\n" + AnsiConsole.RESET);

                    break;

                case "5":
                    System.out.println("Enter project id to be deleted");
                    int delIdUserInput =  new Scanner(System.in).nextInt();

                    Project projectToDel = projectService.getProjectById(delIdUserInput);
                    System.out.println("*****************************************");
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject id:   " + AnsiConsole.RESET + AnsiConsole.YELLOW + projectToDel.getProject_id() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject name: " + AnsiConsole.RESET + AnsiConsole.GREEN + projectToDel.getProject_name() + AnsiConsole.RESET);
                    System.out.println(AnsiConsole.WHITE_BOLD + "\tProject desc: " + AnsiConsole.RESET + AnsiConsole.BLUE + projectToDel.getDescription() + AnsiConsole.RESET);
                    System.out.println("********************************************\n");

                    System.out.println(AnsiConsole.RED + "Are you sure you want to delete ? (y/n)" + AnsiConsole.RESET);
                    String respond = new Scanner(System.in).nextLine().toLowerCase();

                    System.out.println("respond: " + respond);
                    if(respond == "y" | respond == "yes"){
                        System.out.println("...inside y option");
                        projectService.deleteProject(delIdUserInput);
                        System.out.println(AnsiConsole.GREEN + "Project with id=" + delIdUserInput + " deleted successfully\n" + AnsiConsole.RESET);
                        System.out.println();
                    } else if (respond == "n" | respond == "no") {
                        break;
                    }
                    else {
                        System.out.println("Invalid option: " + respond);
                        continue;
                    }
                    break;
                case "6":
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option: " + mainMenuUserInput);

            }
        }

    }


}
