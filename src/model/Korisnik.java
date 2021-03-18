package model;

import java.util.Date;

import model.enums.Pol;
import model.enums.Rola;

public class Korisnik {
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private Date datumRodjenja;
	private Rola uloga;
	private Pol pol;
	private Boolean blocked;
	private Boolean deleted;
	
	public Korisnik(String username, String password, String ime, String prezime, Date datumRodjenja, Rola uloga,
			Pol pol) {
		super();
		this.username = username;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.datumRodjenja = datumRodjenja;
		this.uloga = uloga;
		this.pol = pol;
		this.blocked = false;
		this.deleted = false;
	}

	public Korisnik() {

	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public Date getDatumRodjenja() {
		return datumRodjenja;
	}
	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	public Rola getUloga() {
		return uloga;
	}
	public void setUloga(Rola uloga) {
		this.uloga = uloga;
	}
	public Boolean getBlocked() {
		return blocked;
	}
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Pol getPol() {
		return pol;
	}
	public void setPol(Pol pol) {
		this.pol = pol;
	}
	
 }
