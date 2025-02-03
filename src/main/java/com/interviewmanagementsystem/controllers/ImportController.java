package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.dtos.jobs.JobDTO;
import com.ninja_in_pyjamas.services.job.ExcelJobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@RestController
@RequestMapping("/api/jobs")
public class ImportController {

     private final ExcelJobService excelJobService;

     public ImportController(ExcelJobService excelJobService){

        this.excelJobService = excelJobService;
     }
   @PostMapping("/upload")
public ResponseEntity<?> uploadJobs(@RequestParam("file") MultipartFile file) {
    if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Vui lòng upload file Excel hợp lệ.");
    }

    try {
        List<JobDTO> jobs = excelJobService.importJobs(file.getInputStream());
        return ResponseEntity.ok(jobs);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Lỗi khi xử lý file: " + e.getMessage());
    }
}

}
