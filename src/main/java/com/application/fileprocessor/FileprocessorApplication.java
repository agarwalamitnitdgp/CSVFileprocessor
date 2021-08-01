package com.application.fileprocessor;

import com.application.fileprocessor.service.FileProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FileprocessorApplication implements CommandLineRunner {

    @Autowired
    FileProcessorService fileProcessorService;

    public static void main(String[] args) {
        SpringApplication.run(FileprocessorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        fileProcessorService.processFiles();
    }

}
