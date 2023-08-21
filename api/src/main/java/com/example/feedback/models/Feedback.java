package com.example.feedback.models;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

	private int rating_value;
	private String comment;
	private String userid;
	private Date date;
}
