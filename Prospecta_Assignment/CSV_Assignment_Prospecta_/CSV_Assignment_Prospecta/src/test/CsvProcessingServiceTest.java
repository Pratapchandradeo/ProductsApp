package com.prospecta.service;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsvProcessingServiceTest {

    private CsvProcessingService csvProcessingService;

    @Before
    public void setUp() {
        csvProcessingService = new CsvProcessingService();
    }

    @Test
    public void testProcessCsvFile_Input1() throws Exception {
        // Load input1.csv from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-inputs/input1.csv");

        // Check if the input stream is not null
        assertNotNull("Test file not found: input1.csv", inputStream);

        ByteArrayInputStream result = csvProcessingService.processCsvFile(inputStream);
        assertNotNull(result);

        // Define the expected output for input1.csv
        String expectedOutput1 = "A,B,C\n5,3,10\n7,8,15\n9,9,24\n"; 
        assertEquals(expectedOutput1, new String(result.readAllBytes()));
    }

    @Test
    public void testProcessCsvFile_Input2() throws Exception {
        // Load input2.csv from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-inputs/input2.csv");

        assertNotNull("Test file not found: input2.csv", inputStream);

        ByteArrayInputStream result = csvProcessingService.processCsvFile(inputStream);
        assertNotNull(result);

        // Define the expected output for input2.csv
        String expectedOutput2 = "A,B,C,D\n2,3,5,10.0\n4,5,-1,-0.5\n6,7,42,52.0\n"; 
        assertEquals(expectedOutput2, new String(result.readAllBytes()));
    }

    @Test
    public void testProcessCsvFile_Input3() throws Exception {
        // Load input3.csv from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-inputs/input3.csv");

        assertNotNull("Test file not found: input3.csv", inputStream);

        ByteArrayInputStream result = csvProcessingService.processCsvFile(inputStream);
        assertNotNull(result);

        // Define the expected output for input3.csv
        String expectedOutput3 = "A,B,C,D\n10,20,5,25\n15,35,70,75\n25,20,95,-55\n"; 
        assertEquals(expectedOutput3, new String(result.readAllBytes()));
    }
}
