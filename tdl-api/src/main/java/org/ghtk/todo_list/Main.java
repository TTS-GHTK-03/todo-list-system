package org.ghtk.todo_list;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class Main implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    log.debug("------------------START------------------------------");
    System.out.println("Author: Team 03 ");
    System.out.println("Date: 08/07/2024");
    System.out.println("Project name: Todo list management");
    log.debug("------------------START------------------------------");
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}