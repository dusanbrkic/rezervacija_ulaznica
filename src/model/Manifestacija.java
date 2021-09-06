package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import model.enums.TipManifestacije;

public class Manifestacija {
	private String id;
	private String naziv;
	private String prodavac;
	private int brojMesta;
	private int brojSlobodnihMesta;
	private double regularCena;
	private String poster;
	private TipManifestacije tip;
	private String vremeOdrzavanja;
	private Boolean rasprodata;
	private Boolean aktivna;
	private Boolean deleted;
	private Lokacija lokacija;

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
	public TipManifestacije getTip() {
		return tip;
	}
	public void setTip(TipManifestacije tip) {
		this.tip = tip;
	}
	@JsonIgnore
	public LocalDateTime getVremeOdrzavanjaLDT() {
		return LocalDateTime.parse(this.vremeOdrzavanja, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
	@JsonIgnore
	public void setVremeOdrzavanjaLDT(LocalDateTime vremeOdrzavanja) {
		this.vremeOdrzavanja = vremeOdrzavanja.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}

	public String getVremeOdrzavanja() {
		return vremeOdrzavanja;
	}

	public void setVremeOdrzavanja(String vremeOdrzavanja) {
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
	public Boolean getRasprodata() {
		return rasprodata;
	}
	public void setRasprodata(Boolean rasprodata) {
		this.rasprodata = rasprodata;
	}
	public Lokacija getLokacija() {
		return lokacija;
	}
	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}
	@JsonIgnore
	public String getNazivLokacije() {
		return this.lokacija.getAdresa()+" "+this.lokacija.getGrad();
	}
	public String getProdavac() {
		return prodavac;
	}
	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
}
