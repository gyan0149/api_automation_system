package com.example.apiTest.Controller;

import com.example.apiTest.Service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/processApis")
    public String processApis() {
        System.out.println("Attempting to read the file...");
        File file = new File("C:\\springg\\apis.txt"); // Updated to Excel file

        // Log the file path and existence status
        System.out.println("File path: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        System.out.println("File readable: " + file.canRead());

        if (file.exists() && file.canRead()) {
            try {
                apiService.processApis(file.getAbsolutePath());
                return "API processing completed. Report generated as 'ApiReport.xlsx'.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error occurred: " + e.getMessage();
            }
        } else {
            String errorMsg = "File not found or not readable: " + file.getAbsolutePath();
            System.out.println(errorMsg);
            return errorMsg;
        }
    }
}
