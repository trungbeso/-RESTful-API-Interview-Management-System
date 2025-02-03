package com.interviewmanagementsystem.services.job;

import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import com.interviewmanagementsystem.entities.Job;
import com.interviewmanagementsystem.enums.JobStatus;
import com.interviewmanagementsystem.enums.Level;
import com.interviewmanagementsystem.repositories.IJobRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelJobService {

    @Autowired
    private IJobRepository jobRepository;

    public List<JobDTO> importJobs(InputStream inputStream) {
        List<JobDTO> jobs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                JobDTO jobDTO = new JobDTO();
                try {
                    Iterator<Cell> cellsInRow = currentRow.iterator();

                    int cellIndex = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();

                        switch (cellIndex) {
                            case 0 -> jobDTO.setTitle(getCellValueAsString(currentCell));
                            case 1 -> jobDTO.setStartDate(parseLocalDateTime(currentCell, formatter));
                            case 2 -> jobDTO.setEndDate(parseLocalDateTime(currentCell, formatter));
                            case 3 -> jobDTO.setLevel(parseEnum(Level.class, currentCell));
                            case 4 -> jobDTO.setStatus(parseEnum(JobStatus.class, currentCell));
                            case 5 -> jobDTO.setWorkingAddress(getCellValueAsString(currentCell));
                            case 6 -> jobDTO.setDescription(getCellValueAsString(currentCell));
                            case 7 -> jobDTO.setSalaryFrom(parseBigDecimal(currentCell));
                            case 8 -> jobDTO.setSalaryTo(parseBigDecimal(currentCell));
                            default -> throw new IllegalArgumentException("Unexpected cell index: " + cellIndex);
                        }
                        cellIndex++;
                    }

                    validateJob(jobDTO);

                    Job job = convertToEntity(jobDTO);
                    jobRepository.save(job);

                    jobs.add(jobDTO);
                } catch (Exception e) {
                    System.err.println("Lỗi tại dòng " + rowNumber + ": " + e.getMessage());
                }
                rowNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi đọc file Excel: " + e.getMessage());
        }

        return jobs;
    }

    private void validateJob(JobDTO job) {
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title không được để trống");
        }
        if (job.getSalaryFrom() == null || job.getSalaryTo() == null || job.getSalaryFrom().compareTo(job.getSalaryTo()) > 0) {
            throw new IllegalArgumentException("Mức lương không hợp lệ");
        }
        if (job.getStartDate() != null && job.getEndDate() != null && job.getStartDate().isAfter(job.getEndDate())) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
        }
    }

    private Job convertToEntity(JobDTO jobDTO) {
        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setStartDate(jobDTO.getStartDate());
        job.setEndDate(jobDTO.getEndDate());
        job.setLevel(jobDTO.getLevel());
        job.setStatus(jobDTO.getStatus());
        job.setWorkingAddress(jobDTO.getWorkingAddress());
        job.setDescription(jobDTO.getDescription());
        job.setSalaryFrom(jobDTO.getSalaryFrom());
        job.setSalaryTo(jobDTO.getSalaryTo());
        return job;
    }

    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> throw new IllegalArgumentException("Kiểu dữ liệu không được hỗ trợ: " + cell.getCellType());
        };
    }

    private LocalDateTime parseLocalDateTime(Cell cell, DateTimeFormatter formatter) {
        if (cell.getCellType() == CellType.STRING) {
            return LocalDateTime.parse(cell.getStringCellValue(), formatter);
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate().atStartOfDay();
        }
        throw new IllegalArgumentException("Dữ liệu ngày không hợp lệ: " + cell);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, Cell cell) {
        String value = getCellValueAsString(cell);
        return Enum.valueOf(enumType, value.toUpperCase());
    }

    private BigDecimal parseBigDecimal(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            return new BigDecimal(cell.getStringCellValue());
        }
        throw new IllegalArgumentException("Dữ liệu lương không hợp lệ: " + cell);
    }
}
