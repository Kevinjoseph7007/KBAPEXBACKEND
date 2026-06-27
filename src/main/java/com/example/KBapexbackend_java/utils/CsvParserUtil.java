package com.example.KBapexbackend_java.utils;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// This file is used to parse the CSV file and it will store the records  that are therein the csv file in a list and it will return a list
public class CsvParserUtil {
    public static List<String[]> parseCsv(MultipartFile file) throws Exception{
        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
        List<String[]> rows = new ArrayList<>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            rows.add(line);
        }
        return rows;
    }
}
