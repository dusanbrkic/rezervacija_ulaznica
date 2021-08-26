package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.KarteDAO;
import dao.KorisniciDAO;
import dao.ManifestacijeDAO;
import exceptions.UnknownUsernameException;
import exceptions.UsernameExistsException;
import exceptions.WrongPasswordException;
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
			KorisniciDAO kd = new KorisniciDAO(context.getRealPath("/"));
			context.setAttribute("korisniciDAO", kd);
		}
		if(context.getAttribute("karteDAO")==null) {
			KarteDAO kard = new KarteDAO(context.getRealPath("/"));
			context.setAttribute("korisniciDAO", kard);		
		}
		if(context.getAttribute("manifestacijeDAO")==null) {
			ManifestacijeDAO md = new ManifestacijeDAO(context.getRealPath("/"));
			context.setAttribute("manifestacijeDAO", md);
		}
	}
	
	@POST
	@Path("/registracijaKupca")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String registracijaKupca(Kupac k) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		String cookie = "";
		try {
			cookie = kd.registrujKupca(k);
		} catch (UsernameExistsException e) {
			//return error
		}
		return cookie;
	}
	@GET
	@Path("/validateUser/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String validateUser(@PathParam("cookie") String cookie) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		return kd.validateUser(cookie);
	}
	@GET
	@Path("/loginUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String loginUser(@QueryParam("username") String username,
							@QueryParam("password") String password) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		String cookie = "";
		try {
			cookie = kd.loginUser(username, password);
		} catch (WrongPasswordException e) {
			//return error
		} catch (UnknownUsernameException e) {
			//return error
		}
		return cookie;
	}
}
