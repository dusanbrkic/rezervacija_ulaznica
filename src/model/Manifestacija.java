package model;

import java.util.Date;

import model.enums.TipManifestacije;

public class Manifestacija {
	private String id;
	private String naziv;
	private int brojMesta;
	private int brojSlobodnihMesta;
	private double regularCena;
	private String imgPath;
	private TipManifestacije tip;
	private Date vremeOdrzavanja;
	private Boolean aktivna;
	private Boolean deleted;
	public Manifestacija() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getBrojMesta() {
		return brojMesta;
	}
	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}
	public int getBrojSlobodnihMesta() {
		return brojSlobodnihMesta;
	}
	public void setBrojSlobodnihMesta(int brojSlobodnihMesta) {
		this.brojSlobodnihMesta = brojSlobodnihMesta;
	}
	public double getRegularCena() {
		return regularCena;
	}
	public void setRegularCena(double regularCena) {
		this.regularCena = regularCena;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public TipManifestacije getTip() {
		return tip;
	}
	public void setTip(TipManifestacije tip) {
		this.tip = tip;
	}
	public Date getVremeOdrzavanja() {
		return vremeOdrzavanja;
	}
	public void setVremeOdrzavanja(Date vremeOdrzavanja) {
		this.vremeOdrzavanja = vremeOdrzavanja;
	}
	public Boolean getAktivna() {
		return aktivna;
	}
	public void setAktivna(Boolean aktivna) {
		this.aktivna = aktivna;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
}
