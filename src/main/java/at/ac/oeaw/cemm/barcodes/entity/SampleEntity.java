package at.ac.oeaw.cemm.barcodes.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "sample")
public class SampleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "sam_id", nullable = false, columnDefinition = "serial")    
    private Integer id; 
    
    @JoinColumn(name = "barcode_i5", referencedColumnName = "id")
    @ManyToOne
    private Barcode barcodeI5;
        
    @JoinColumn(name = "barcode_i7", referencedColumnName = "id")
    @ManyToOne
    private Barcode barcodeI7;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sequencingIndexId")
    private SequencingIndexEntity sequencingIndexes;

    public Integer getId() {
      
        return this.id;
    }

    public void setId(Integer samId) {
        this.id = samId;
    }
   
   
    @Override
    public int hashCode() {
        //Integer i = new Integer(id);
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SampleEntity other = (SampleEntity) obj;
        Integer i = new Integer(id);
        Integer o = ((SampleEntity)obj).id;
        if (i == null) {
            if (o != null) {
                return false;
            }
        } else if (!i.equals(o)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
    
   
    public Barcode getBarcodeI5() {
        return barcodeI5;
    }

    public void setBarcodeI5(Barcode barcodeI5) {
        this.barcodeI5 = barcodeI5;
    }

    public Barcode getBarcodeI7() {
        return barcodeI7;
    }

    public void setBarcodeI7(Barcode barcodeI7) {
        this.barcodeI7 = barcodeI7;
    }
    
    public SequencingIndexEntity getSequencingIndexes() {
        return this.sequencingIndexes;
    }

    public void setSequencingIndexes(SequencingIndexEntity sequencingindexes) {
        this.sequencingIndexes = sequencingindexes;
    }
    
    public String getSequencingIndex1(){
        if(this.sequencingIndexes == null){
            return "NONE";
        }
        if(this.sequencingIndexes.getIndex().length() <= 8){
            return this.sequencingIndexes.getIndex();
        }
        if(this.sequencingIndexes.getIndex().length() > 8){
            return this.sequencingIndexes.getIndex().substring(0,8);
        }
        return "";
    }
    
    public String getSequencingIndex2(){
        if(this.sequencingIndexes == null){
            return "NONE";
        }        
        if(this.sequencingIndexes.getIndex().length() <= 8){
            return "NONE";
        }        
        if(this.sequencingIndexes.getIndex().length() > 8){
            return this.sequencingIndexes.getIndex().substring(8);
        }
        return "";
    }

}
