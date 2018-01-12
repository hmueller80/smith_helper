/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.barcodes.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "barcode_kit")
@NamedQueries({
    @NamedQuery(name = "BarcodeKit.findAll", query = "SELECT b FROM BarcodeKit b")})
public class BarcodeKit implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BarcodeKitPK barcodeKitPK;
    @JoinColumn(name = "barcode_id", referencedColumnName = "id")
    @ManyToOne
    private Barcode barcodeId;

    public BarcodeKit() {
    }

    public BarcodeKit(BarcodeKitPK barcodeKitPK) {
        this.barcodeKitPK = barcodeKitPK;
    }

    public BarcodeKit(String kitName, String sequenceName) {
        this.barcodeKitPK = new BarcodeKitPK(kitName, sequenceName);
    }

    public BarcodeKitPK getBarcodeKitPK() {
        return barcodeKitPK;
    }

    public void setBarcodeKitPK(BarcodeKitPK barcodeKitPK) {
        this.barcodeKitPK = barcodeKitPK;
    }

    public Barcode getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(Barcode barcodeId) {
        this.barcodeId = barcodeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (barcodeKitPK != null ? barcodeKitPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BarcodeKit)) {
            return false;
        }
        BarcodeKit other = (BarcodeKit) object;
        if ((this.barcodeKitPK == null && other.barcodeKitPK != null) || (this.barcodeKitPK != null && !this.barcodeKitPK.equals(other.barcodeKitPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKit[ barcodeKitPK=" + barcodeKitPK + " ]";
    }
    
}
