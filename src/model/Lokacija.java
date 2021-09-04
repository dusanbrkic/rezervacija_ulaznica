package model;

public class Lokacija {
	private double geografskaDuzina;
	private double geografskaSirina;
	private String adresa;
	private Boolean deleted;
	private String grad;

	public Lokacija() {
		super();
	}
	public double getGeografskaDuzina() {
		return geografskaDuzina;
	}
	public void setGeografskaDuzina(double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}
	public double getGeografskaSirina() {
		return geografskaSirina;
	}
	public void setGeografskaSirina(double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	
}
