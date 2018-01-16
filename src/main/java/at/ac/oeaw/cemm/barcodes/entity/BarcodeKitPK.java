/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Embeddable
public class BarcodeKitPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "kit_name")
    private String kitName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sequence_name")
    private String sequenceName;

    public BarcodeKitPK() {
    }

    public BarcodeKitPK(String kitName, String sequenceName) {
        this.kitName = kitName;
        this.sequenceName = sequenceName;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kitName != null ? kitName.hashCode() : 0);
        hash += (sequenceName != null ? sequenceName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BarcodeKitPK)) {
            return false;
        }
        BarcodeKitPK other = (BarcodeKitPK) object;
        if ((this.kitName == null && other.kitName != null) || (this.kitName != null && !this.kitName.equals(other.kitName))) {
            return false;
        }
        if ((this.sequenceName == null && other.sequenceName != null) || (this.sequenceName != null && !this.sequenceName.equals(other.sequenceName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKitPK[ kitName=" + kitName + ", sequenceName=" + sequenceName + " ]";
    }
    
}
