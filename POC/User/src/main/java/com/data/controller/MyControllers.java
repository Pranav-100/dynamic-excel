package com.data.controller;

import java.util.List;

import com.data.dao.BData;
import com.data.services.ExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyControllers {
	
	@Autowired
	private ExcelExporter services;
	
	@GetMapping("/get")
	public void getBooks() throws Exception {
		services.generateExcelFromDB();
//		return (ResponseEntity) ResponseEntity.ok();
	}

}
