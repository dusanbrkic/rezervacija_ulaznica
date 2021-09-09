package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.enums.ImeTipaKupca;
import model.enums.Pol;
import model.enums.Rola;

public class Kupac extends Korisnik {
	private List<String> karte;
	private int brojBodova;
	private ImeTipaKupca tip;

	public Kupac(){
		super();
		karte = new ArrayList<String>();
	}

	@Override
	public int takeBrojBodova() {
		return brojBodova;
	}
	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}
	public int getBrojBodova() {return brojBodova;}

		public List<String> getKarte() {
		return karte;
	}

	public void setKarte(List<String> karte) {
		this.karte = karte;
	}

	public ImeTipaKupca getTip() {
		return tip;
	}

	public void setTip(ImeTipaKupca tip) {
		this.tip = tip;
	}
	
}
