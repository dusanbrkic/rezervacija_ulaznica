package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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
	public String registracijaKupca(Kupac k) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		return kd.registrujKupca(k);
	}
	@POST
	@Path("/validateKupac/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean validateKupac(@PathParam("cookie") String cookie) {
		System.out.println("JA SAM "+ cookie);
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		return kd.validateKupac(cookie);
	}
	@POST
	@Path("/validateProdavac")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean validateProdavac(String cookieToken) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		return kd.validateProdavac(cookieToken);
	}
	@POST
	@Path("/validateAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean validateAdmin(String cookieToken) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		return kd.validateAdmin(cookieToken);
	}
}
