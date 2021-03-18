package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.KorisniciDAO;
import model.Korisnik;
import model.Kupac;

@Path("/korisnici")
public class KorisniciService {
	
	@Context
	ServletContext context;
	
	@Context
	HttpServletRequest request;

	@PostConstruct
	public void init() {
		if(context.getAttribute("korisniciDAO")==null) {
			KorisniciDAO kd = new KorisniciDAO();
			context.setAttribute("korisniciDAO", kd);
		}
	}
	
	@POST
	@Path("/registracijaKupca")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean registracijaKupca(Kupac k) {
		System.out.println(k);
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		kd.registrujKupca(k);
		return false;
	}
}
