/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.parser;

/**
 *
 * @author dbarreca
 */public enum BarcodesCSVHeader {
    RunID,
    Lanes,
    SampleID,
    Operator,
    Requester,
    Flowcell,
    SequencingIndexReported,
    SequencingIndexFromBam,
    RunFolder,
    SampleDBName,
    Spacer5P,
    Indexi7,
    SpaerCenter,
    Indexi5,
    IndexLength,
    Machine;
      
    public static char getSeparator(){
        return ';';
    }
   
}
