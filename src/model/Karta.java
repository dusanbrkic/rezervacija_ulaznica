package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dao.ManifestacijeDAO;
import model.enums.StatusKarte;
import model.enums.TipKarte;

public class Karta{
	private String id;
	private String kupac;
	private String prodavac;
	private String manifestacija;
	private double cena;
	private String vremeManifestacije;
	private StatusKarte status;
	private TipKarte tip;
	public Karta() {
		super();
	}
	@JsonIgnore
	public Karta getDTOcopy() {
		Karta k = new Karta();
		k.id = this.id;
		//k.kupac = ManifestacijeDAO.manifestacije.get(this.manifestacija).getNaziv();
		k.kupac = this.kupac;
		k.prodavac = this.prodavac;
		k.manifestacija = this.manifestacija;
		k.cena = this.cena;
		k.vremeManifestacije = this.vremeManifestacije;
		k.status = this.status;
		k.tip = tip;
		return k;
	}
	
	@JsonIgnore
	public String getNazivManifestacije() 
	{
		Manifestacija mf = ManifestacijeDAO.manifestacije.get(manifestacija);
		return mf.getNaziv();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKupac() {
		return kupac;
	}
	public void setKupac(String kupac) {
		this.kupac = kupac;
	}
	public String getProdavac() {
		return prodavac;
	}
	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}
	public String getManifestacija() {
		return manifestacija;
	}
	public void setManifestacija(String manifestacija) {
		this.manifestacija = manifestacija;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public String getVremeManifestacije() {
		return vremeManifestacije;
	}
	public void setVremeManifestacije(String vremeManifestacije) {
		this.vremeManifestacije = vremeManifestacije;
	}
	@JsonIgnore
	public LocalDateTime getVremeManifestacijeLDT() {
		return LocalDateTime.parse(this.vremeManifestacije, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
	public StatusKarte getStatus() {
		return status;
	}
	public void setStatus(StatusKarte status) {
		this.status = status;
	}
	public TipKarte getTip() {
		return tip;
	}
	public void setTip(TipKarte tip) {
		this.tip = tip;
	}
	@JsonIgnore
	@Override
	public String toString() {
		return "Karta [id=" + id + ", kupac=" + kupac + ", prodavac=" + prodavac + ", manifestacija=" + manifestacija
				+ ", cena=" + cena + ", vremeManifestacije=" + vremeManifestacije + ", status=" + status + ", tip="
				+ tip + "]";
	}
	
}
