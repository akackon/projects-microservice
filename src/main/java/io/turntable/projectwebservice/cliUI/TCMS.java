package io.turntable.projectwebservice.cliUI;

import io.turntable.projectwebservice.controllers.ProjectController;
import io.turntable.projectwebservice.serviceImplementors.ProjectServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Scanner;

public class TCMS {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
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

            System.out.print("Select option:");
            userInput = sn.nextLine();
            ProjectServiceImpl projectService = new ProjectServiceImpl();
            ProjectController projectController = new ProjectController();

            switch (userInput) {
                case "1":
//                    projectService.getAllProjects();
                    String location = "http://localhost:1996/project/";
                    URI url;
                    url = new URI(location);
                    HttpResponse<String> res = HttpClient.newBuilder().build().send(HttpRequest.newBuilder(url).build(),
                            HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
                    System.out.println(res);

                    projectController.getAllProjects();
                case "5":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option");

            }


        }
    }
}
