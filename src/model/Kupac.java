package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.enums.Pol;
import model.enums.Rola;

public class Kupac extends Korisnik {
	private List<String> karte;
	private int brojBodova;
	
	public Kupac(String username, String password, String ime, String prezime, Date datumRodjenja, Rola uloga, Pol pol) {
		super(username, password, ime, prezime, datumRodjenja, uloga, pol);
		setBrojBodova(0);
		karte = new ArrayList<String>();
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}
	
}
