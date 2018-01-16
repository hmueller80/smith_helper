/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.apps;

import at.ac.oeaw.cemm.barcodes.entity.Barcode;
import at.ac.oeaw.cemm.barcodes.entity.BarcodeKit;
import at.ac.oeaw.cemm.barcodes.entity.HibernateUtil;
import at.ac.oeaw.cemm.barcodes.entity.SampleEntity;
import at.ac.oeaw.cemm.barcodes.parser.BarcodeParser;
import at.ac.oeaw.cemm.bsf.barcode.IlluminaAdapterSequences;
import java.io.File;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
public class UpdateSamplesBarcodes {
    
    public static void main(String[] args) throws Exception {
        UpdateSamplesBarcodes thisObj = new UpdateSamplesBarcodes();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {            
            
            Collection<SampleEntity> parsedSamples = BarcodeParser.parse(thisObj.getFile()).values();
            Integer count = 0;
            Integer previousStatus = 0;
            for (SampleEntity sample : parsedSamples) {
                Integer status = (int) (count.doubleValue()/parsedSamples.size() * 100.0);
                if (status % 5 == 0 && !status.equals(previousStatus)){
                    System.out.println("Status: "+status);
                    previousStatus = status;
                }
                
                Barcode barcodeI5 = getBarcodeFromDB(sample.getBarcodeI5(),session);
                if (barcodeI5 == null) {
                    System.out.println("WARN: I5 Barcode not found in DB: "+sample.getBarcodeI5().getSequence()
                            + " Kit INFO: "+IlluminaAdapterSequences.getDetailedIndexInfo(sample.getBarcodeI5().getSequence()));
                    barcodeI5 = sample.getBarcodeI5();
                    BarcodeKit unknownKit = new BarcodeKit("UNKOWN","I5_"+barcodeI5.getSequence());
                    unknownKit.setBarcodeId(barcodeI5);
                    session.persist(barcodeI5);
                    session.update(barcodeI5);
                    session.persist(unknownKit);
                }
                
                
                Barcode barcodeI7 = getBarcodeFromDB(sample.getBarcodeI7(),session);
                if (barcodeI7 == null) {
                    System.out.println("WARN: I7 Barcode not found in DB: "+sample.getBarcodeI7().getSequence()
                            + " Kit INFO: "+IlluminaAdapterSequences.getDetailedIndexInfo(sample.getBarcodeI7().getSequence()));
                    barcodeI7 = sample.getBarcodeI7();
                    BarcodeKit unknownKit = new BarcodeKit("UNKOWN","I7_"+barcodeI7.getSequence());
                    unknownKit.setBarcodeId(barcodeI7);
                    session.persist(barcodeI7);
                    session.update(barcodeI7);
                    session.persist(unknownKit);
                }
                
                SampleEntity sampleFromDB = (SampleEntity) session.get(SampleEntity.class, sample.getId());
                if (sampleFromDB==null){
                    System.out.println("Sample with ID "+sample.getId()+" not found in DB");
                }else{
                    sampleFromDB.setBarcodeI5(barcodeI5);
                    sampleFromDB.setBarcodeI7(barcodeI7);
                    session.persist(sampleFromDB);
                }
                
                count +=1;
                
            }   
            
            Criteria emptySequenceCriteria = session.createCriteria(SampleEntity.class);
            emptySequenceCriteria.add(Restrictions.isNull("barcodeI7"));
            
            for (SampleEntity sample: (List<SampleEntity>) emptySequenceCriteria.list()){
                System.out.println("WARN: sample "+sample.getId()+" has no INDEX set");
                
                Barcode barcodeI5 = getBarcodeFromDB(sample.getSequencingIndex2(),"i5",session);
                if (barcodeI5 == null) {
                    System.out.println("WARN: I5 Barcode not found in DB: "+sample.getSequencingIndex2()
                            + " Kit INFO: "+IlluminaAdapterSequences.getDetailedIndexInfo(sample.getSequencingIndex2()));
                    barcodeI5 = new Barcode();
                    barcodeI5.setSequence(sample.getSequencingIndex2());
                    barcodeI5.setBarcodeType("i5");
                    BarcodeKit unknownKit = new BarcodeKit("UNKOWN","I5_"+barcodeI5.getSequence());
                    unknownKit.setBarcodeId(barcodeI5);
                    session.persist(barcodeI5);
                    session.update(barcodeI5);
                    session.persist(unknownKit);
                }
                
                Barcode barcodeI7 = getBarcodeFromDB(sample.getSequencingIndex1(),"i7",session);
                if (barcodeI7 == null) {
                   System.out.println("WARN: I5 Barcode not found in DB: "+sample.getSequencingIndex1()
                            + " Kit INFO: "+IlluminaAdapterSequences.getDetailedIndexInfo(sample.getSequencingIndex1()));
                    barcodeI7 = new Barcode();
                    barcodeI7.setSequence(sample.getSequencingIndex1());
                    barcodeI7.setBarcodeType("i7");
                    BarcodeKit unknownKit = new BarcodeKit("UNKOWN","I7_"+barcodeI7.getSequence());
                    unknownKit.setBarcodeId(barcodeI7);
                    session.persist(barcodeI7);
                    session.update(barcodeI7);
                    session.persist(unknownKit);
                }
                
                sample.setBarcodeI5(barcodeI5);
                sample.setBarcodeI7(barcodeI7);
                
                session.persist(sample);
                
            }
            
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }
    
    private File getFile(){
         ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("barcodes.csv").getFile());       
        return file;
    }
    
    private static Barcode getBarcodeFromDB(Barcode toSearch, Session session) {
        return getBarcodeFromDB(toSearch.getSequence(), toSearch.getBarcodeType(), session);
    }
    
    private static Barcode getBarcodeFromDB(String sequence, String type, Session session) {
        Criteria barcodeCriteria = session.createCriteria(Barcode.class);
        barcodeCriteria.add(Restrictions.eq("sequence", sequence));
        barcodeCriteria.add(Restrictions.eq("barcodeType", type));
        Barcode barcodeFound = (Barcode) barcodeCriteria.uniqueResult();

        return barcodeFound;
    }
}
