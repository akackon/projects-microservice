package io.turntable.projectwebservice.trial;

//import io.turntable.projectwebservice.log.Sub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ProjectWebserviceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectWebserviceApplication.class, args);
		Alien a = context.getBean(Alien.class);
		a.show();
		Alien a2 = context.getBean(Alien.class);
		a2.show();

//
	}

}
