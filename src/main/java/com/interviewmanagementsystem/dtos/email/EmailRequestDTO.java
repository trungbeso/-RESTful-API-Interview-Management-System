package com.interviewmanagementsystem.dtos.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequestDTO {
	String templateName;

	String to;

	String cc;

	String bcc;

	String subject;

	String body;

	Map<String,Object> variables;
}
