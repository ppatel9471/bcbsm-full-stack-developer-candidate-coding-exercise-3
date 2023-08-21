package com.example.feedback.dto;

import lombok.Data;

@Data
public class ResponseDTO {
	Boolean status;
	String message;
	Object data;
}
