package cz.sm.ng.spring.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * This class is entry point of Spring Boot application.
 * Its place where configuration starts after application
 * is deployed on server. It scans for package prefix
 * <cz.sm.ng> which is used by all smweb-ng modules,
 * to register all modules functionality.
 *
 * @author Norbert Dopjera
 */
@SpringBootApplication
@ComponentScan({"cz.sm.ng"})
@EntityScan({"cz.sm.ng"})
@EnableJpaRepositories({"cz.sm.ng"})
public class SMWebApplicationLauncher extends SpringBootServletInitializer {

    public static void main(String[] args) { SpringApplication.run(SMWebApplicationLauncher.class, args); }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SMWebApplicationLauncher.class);
    }

} // SMWebApplicationLauncher

