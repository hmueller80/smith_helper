/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.parser;


import at.ac.oeaw.cemm.barcodes.entity.Barcode;
import at.ac.oeaw.cemm.barcodes.entity.SampleEntity;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class BarcodeParser {
   
    public  static Map<Integer,SampleEntity> parse(File fileToParse) throws Exception {
        
        System.out.println("Parsing file "+fileToParse.getAbsolutePath());
         Map<Integer,SampleEntity> resultSet = new HashMap<>();
        
        Reader reader = new FileReader(fileToParse);

        CSVParser parser = new CSVParser(reader,
                CSVFormat.RFC4180
                        .withHeader(BarcodesCSVHeader.class)
                        .withSkipHeaderRecord()
                        .withDelimiter(';')
                        .withIgnoreEmptyLines());

        List<CSVRecord> records = parser.getRecords();
        
        Integer maxId = 0;
        
        for (CSVRecord record : records) {
            SampleEntity sample = new SampleEntity();
            sample.setId(Integer.parseInt(record.get(BarcodesCSVHeader.SampleID)));
            
            Barcode barcodeI5 = new Barcode();
            String sequencei5 = record.get(BarcodesCSVHeader.Indexi5).trim().toUpperCase();
            if (sequencei5.isEmpty() || sequencei5.equalsIgnoreCase("undefined") || sequencei5.equalsIgnoreCase("none")){
                sequencei5 = "NONE";
            }else if (!sequencei5.matches("[ATCG]+")){
                 System.out.println("WARN: Wrong i5 index: "+sequencei5+" in sample"+sample.getId());
            }
            barcodeI5.setSequence(sequencei5);
            barcodeI5.setBarcodeType("i5");
            
            Barcode barcodeI7 = new Barcode();
            String sequencei7 = record.get(BarcodesCSVHeader.Indexi7).trim().toUpperCase();
            if (sequencei7.equalsIgnoreCase("undefined") || sequencei7.equalsIgnoreCase("none")){
                sequencei7 = "NONE";
            }else if (sequencei7.isEmpty() || !sequencei7.matches("[ATCG]+")){
                 System.out.println("WARN: Wrong i7 index: "+sequencei7+" in sample"+sample.getId());
            }
            barcodeI7.setSequence(sequencei7);
            barcodeI7.setBarcodeType("i7");
            
            sample.setBarcodeI5(barcodeI5);
            sample.setBarcodeI7(barcodeI7);
            
            Integer sampleId = sample.getId();
            if (resultSet.containsKey(sampleId)){
                SampleEntity existingSample = resultSet.get(sampleId);
                if (!existingSample.getBarcodeI5().getSequence().equals(sample.getBarcodeI5().getSequence())){
                    System.out.println("WARN: Duplicate sample "+sample.getId()+" with two different i5 indices");
                }
                if (!existingSample.getBarcodeI7().getSequence().equals(sample.getBarcodeI7().getSequence())){
                    System.out.println("WARN: Duplicate sample "+sample.getId()+" with two different i7 indices");
                }
                
            }else {
                resultSet.put(sampleId, sample);
            }
            if (sampleId>maxId){
                maxId = sampleId;
            }
        }
        
        return resultSet;
    
    }
}
   
