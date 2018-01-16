/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "barcode")
public class Barcode implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 100)
    @Column(name = "sequence")
    private String sequence;
    
    @Size(max = 10)
    @Column(name = "barcode_type")
    private String barcodeType;
    
    @OneToMany(mappedBy = "barcodeId")
    private Set<BarcodeKit> barcodeKitSet;

    public Barcode() {
    }

    public Barcode(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public Set<BarcodeKit> getBarcodeKitSet() {
        return barcodeKitSet;
    }

    public void setBarcodeKitSet(Set<BarcodeKit> barcodeKitSet) {
        this.barcodeKitSet = barcodeKitSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Barcode)) {
            return false;
        }
        Barcode other = (Barcode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.Barcode[ id=" + id + " ]";
    }
    
}
