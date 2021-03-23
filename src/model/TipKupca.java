package model;

import model.enums.ImeTipaKupca;

public class TipKupca {
    private ImeTipaKupca imeTipaKupca;
    private double popust;
    private int potrebanBrBodova;

    public ImeTipaKupca getImeTipaKupca() {
        return imeTipaKupca;
    }

    public void setImeTipaKupca(ImeTipaKupca imeTipaKupca) {
        this.imeTipaKupca = imeTipaKupca;
    }

    public double getPopust() {
        return popust;
    }

    public void setPopust(double popust) {
        this.popust = popust;
    }

    public int getPotrebanBrBodova() {
        return potrebanBrBodova;
    }

    public void setPotrebanBrBodova(int potrebanBrBodova) {
        this.potrebanBrBodova = potrebanBrBodova;
    }
}
