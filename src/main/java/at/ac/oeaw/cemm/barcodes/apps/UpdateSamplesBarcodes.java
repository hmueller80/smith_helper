/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.apps;

import at.ac.oeaw.cemm.barcodes.entity.SampleEntity;
import at.ac.oeaw.cemm.barcodes.parser.BarcodeParser;
import java.io.File;

/**
 *
 * @author dbarreca
 */
public class UpdateSamplesBarcodes {
    
    public static void main(String[] args) throws Exception{
        UpdateSamplesBarcodes thisObj = new UpdateSamplesBarcodes();
        
       
        for (SampleEntity sample : BarcodeParser.parse(thisObj.getFile()).values()){
            System.out.println("Sample with id "+sample.getId()
                    +" Index "+sample.getBarcodeI7().getBarcodeType()+": "+sample.getBarcodeI7().getSequence()
                    +" Index "+sample.getBarcodeI5().getBarcodeType()+": "+sample.getBarcodeI5().getSequence());
        }
        
    }
    
    private File getFile(){
         ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("barcodes.csv").getFile());       
        return file;
    }
}
