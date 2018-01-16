package at.ac.oeaw.cemm.barcodes.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequencingindexes")
public class SequencingIndexEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idsequencingindexes")
    private int id;

    @Column(name = "index")
    private String index = "none";//default value for index sequence.


    public SequencingIndexEntity() {
    }

    public SequencingIndexEntity(String index) {
        this.index = index;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int idsequencingindexes) {
        this.id = idsequencingindexes;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }


}
