/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.apps;

import at.ac.oeaw.cemm.bsf.barcode.BarcodeSequenceKit;
import at.ac.oeaw.cemm.barcodes.entity.Barcode;
import at.ac.oeaw.cemm.barcodes.entity.BarcodeKit;
import at.ac.oeaw.cemm.barcodes.entity.BarcodeKitPK;
import at.ac.oeaw.cemm.barcodes.entity.HibernateUtil;
import at.ac.oeaw.cemm.bsf.barcode.IlluminaAdapterSequences;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
public class Illumina2BarcodeDB {
    
    public static void main(String[] args) {
        List<BarcodeKit> kitsToPersist = new LinkedList<>();

        try {
            
            
            IlluminaAdapterSequences allKits = new IlluminaAdapterSequences();
            Field kitsField = allKits.getClass().getDeclaredField("kits");
            kitsField.setAccessible(true);
            Collection<String> kits = (Collection<String>) kitsField.get(allKits);
            for (String kit : kits) {
                
                Class kitClass = Class.forName("at.ac.oeaw.cemm.bsf.barcode." + kit);
                BarcodeSequenceKit kitObj = (BarcodeSequenceKit) kitClass.newInstance();

                Field[] declaredFields = kitClass.getDeclaredFields();
                Map<String, String> i7Indexes = new HashMap<>();
                Map<String, String[]> i5Indexes = new HashMap<>();

                for (Field field : declaredFields) {
                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                        if (field.getType().equals(String.class)) {
                            String fieldValue = (String) field.get(kitObj);
                            if (fieldValue.matches("[ATCG ]+")) {
                                i7Indexes.put(field.getName(), fieldValue);

                            }
                        } else if (field.getType().equals(String[].class)) {
                            String[] fieldValue = (String[]) field.get(kitObj);
                            if (fieldValue.length == 2 && fieldValue[0].matches("[ATCG ]+") && fieldValue[1].matches("[ATCG ]+")) {
                                i5Indexes.put(field.getName(), fieldValue);

                            }
                        }
                    }
                }
                
                for (Entry<String,String> i7IndexEntry: i7Indexes.entrySet()){
                    if (!kitObj.getI7indices().contains(i7IndexEntry.getValue())){
                        System.out.println("WARN: index "+i7IndexEntry.getValue()+" not found in i7 indexes");
                    }
                    //System.out.println(kit+" i7Index: "+i7IndexEntry.getKey()+" "+i7IndexEntry.getValue());
                    BarcodeKitPK barcodeKitKey = new BarcodeKitPK(kit,i7IndexEntry.getKey());
                    BarcodeKit barcodeKitLine = new BarcodeKit(barcodeKitKey);
                    Barcode barcode = new Barcode();
                    barcode.setBarcodeType("i7");
                    barcode.setSequence(i7IndexEntry.getValue().trim());
                    barcodeKitLine.setBarcodeId(barcode);
                    
                    kitsToPersist.add(barcodeKitLine);

                }
                
                for (String index: kitObj.getI7indices()){
                    if (!i7Indexes.values().contains(index)){
                        System.out.println("WARN: I7 index "+index+" not found in fields of kit "+kit);
                    }
                    
                }
                
                
                for (Entry<String,String[]> i5IndexEntry: i5Indexes.entrySet()){
                    if (!kitObj.getI5indices_HiSeq2000_HiSeq2500_MiSeq().contains(i5IndexEntry.getValue()[0])){
                        System.out.println("WARN: index "+i5IndexEntry.getValue()[0]+" not found in i5 MiSeq indexes");
                    }else if (!kitObj.getI5indices_NextSeq_HiSeq3000_HiSeq4000().contains(i5IndexEntry.getValue()[1])){
                        System.out.println("WARN: index "+i5IndexEntry.getValue()[1]+" not found in i5 HiSeq indexes");
                       
                    }
                    
                    BarcodeKitPK barcodeKitKey = new BarcodeKitPK(kit,i5IndexEntry.getKey());
                    BarcodeKit barcodeKitLine = new BarcodeKit(barcodeKitKey);
                    Barcode barcode = new Barcode();
                    barcode.setBarcodeType("i5");
                    barcode.setSequence(i5IndexEntry.getValue()[1].trim());
                    barcodeKitLine.setBarcodeId(barcode);
                    kitsToPersist.add(barcodeKitLine);
                    //System.out.println(kit+" i7Index: "+i5IndexEntry.getKey()+" "+i5IndexEntry.getValue()[0]+" "+i5IndexEntry.getValue()[1]);
                }
                
                 
                for (String index: kitObj.getI5indices_HiSeq2000_HiSeq2500_MiSeq()){
                    boolean found = false;
                    for (String[] indexes: i5Indexes.values()){
                        if (index.equals(indexes[0])){
                            found = true;
                            break;
                        }
                    }
                    if (!found){
                        System.out.println("WARN: i5 MiSeq Index: "+index+" not found in fields of kit "+kit);
                    }
                }
                
                 for (String index: kitObj.getI5indices_NextSeq_HiSeq3000_HiSeq4000()){
                    boolean found = false;
                    for (String[] indexes: i5Indexes.values()){
                        if (index.equals(indexes[1])){
                            found = true;
                            break;
                        }
                    }
                    if (!found){
                        System.out.println("WARN: i5 HiSeq Index: "+index+" not found in fields of kit "+kit);
                    }
                }
                

            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            for (BarcodeKit kit: kitsToPersist){
                Barcode barcode = kit.getBarcodeId();
                Criteria barcodeCriteria = session.createCriteria(Barcode.class);
                barcodeCriteria.add(Restrictions.eq("sequence", barcode.getSequence()));
                barcodeCriteria.add(Restrictions.eq("barcodeType", barcode.getBarcodeType()));
                Barcode barcodeFound = (Barcode) barcodeCriteria.uniqueResult();
                if (barcodeFound == null) {
                    session.persist(barcode);
                    barcodeFound = barcode;
                    session.update(barcodeFound);
                }
                kit.setBarcodeId(barcodeFound);
                session.persist(kit);
                
            }
            tx.commit();
            
        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally{
            if (session.isOpen()){
                session.close();
            }
        }        
    }

}
