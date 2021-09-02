package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

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
import model.Admin;
import model.Korisnik;
import model.Kupac;
import model.Prodavac;
import model.enums.KupciSortingParam;

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
			context.setAttribute("karteDAO", kard);		
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
	@GET
	@Path("/getKupci")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKupci(
				@QueryParam("ime") String ime,
				@QueryParam("prezime") String prezime,
				@QueryParam("username") String username,
				@QueryParam("sortat") KupciSortingParam sortAt 
				) {
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		HashMap<String, Kupac> kupci = (HashMap<String, Kupac>) korisniciDao.kupci.clone();
		Iterator<String> iterator = kupci.keySet().iterator();
		//System.out.println(username);
		while(iterator.hasNext()) {
			String trenutni = iterator.next();
			Kupac k = kupci.get(trenutni);
			if(k.getDeleted()) {
				iterator.remove();
				continue;
			}
			if(!k.getIme().toLowerCase().contains(ime.toLowerCase())) {
				iterator.remove();
				continue;
			}
			if(!k.getPrezime().toLowerCase().contains(prezime.toLowerCase())) {
				iterator.remove();
				continue;
			}
			if(!k.getUsername().toLowerCase().contains(username.toLowerCase())) {
				iterator.remove();
				continue;
			}
			
		}
		Collection<Kupac> ckupci = kupci.values();
		ArrayList<Kupac> results = new ArrayList<Kupac>(ckupci);
		if(sortAt==null) {
			sortAt = KupciSortingParam.NISTA;
		}
		switch(sortAt) {
		case IMEASC : results.sort(Comparator.comparing(Kupac::getIme));
			break;
		case IMEDESC : results.sort(Comparator.comparing(Kupac::getIme).reversed());
			break;
		case PREZIMEASC : results.sort(Comparator.comparing(Kupac::getPrezime));
			break;
		case PREZIMEDESC : results.sort(Comparator.comparing(Kupac::getPrezime).reversed());
			break;
		case USERNAMEASC : results.sort(Comparator.comparing(Kupac::getUsername));
			break;
		case USERNAMEDESC : results.sort(Comparator.comparing(Kupac::getUsername).reversed());
			break;
		case BODOVIASC : results.sort(Comparator.comparing(Kupac::getBrojBodova));
			break;
		case BODOVIDESC : results.sort(Comparator.comparing(Kupac::getBrojBodova).reversed());
			break;
		}
		
		
		
		return Response.status(Response.Status.OK).entity(results).build();
	}
	
	public Response getKorisnici(
								@QueryParam("ime") String ime,
								@QueryParam("prezime") String prezime,
								@QueryParam("username") String username,
								@QueryParam("password") String password) {
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		HashMap<String, Kupac> kupci = (HashMap<String, Kupac>) korisniciDao.kupci.clone();
		HashMap<String, Admin> admini = (HashMap<String, Admin>) korisniciDao.admini.clone();
		HashMap<String, Prodavac> prodavci = (HashMap<String, Prodavac>) korisniciDao.prodavci.clone();
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		for (Kupac k : kupci.values()) {
			korisnici.put(k.getUsername(), k);
		}
		for (Admin a : admini.values()) {
			korisnici.put(a.getUsername(), a);
		}
		for (Prodavac p : prodavci.values()) {
			korisnici.put(p.getUsername(), p);
		}
		ArrayList<Korisnik> results = (ArrayList<Korisnik>) korisnici.values();
		return Response.status(Response.Status.OK).entity(results).build();
	}
}
