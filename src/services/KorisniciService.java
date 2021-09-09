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
import exceptions.*;
import model.Admin;
import model.Karta;
import model.Korisnik;
import model.Kupac;
import model.Prodavac;
import model.TipKupca;
import model.enums.ImeTipaKupca;
import model.enums.KupciSortingParam;
import model.enums.Rola;

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
	public Response validateUser(@PathParam("cookie") String cookie) {
		KorisniciDAO kd = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Rola rola = null;
		try {
			rola = kd.validateUser(cookie);
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Sesija je istekla").build();
		}
		return Response.status(Response.Status.OK).entity(rola).build();
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
	@POST
	@Path("/getKorisnici/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getKorisnici(
				@PathParam("cookie")String cookie,
				@QueryParam("ime") String ime,
				@QueryParam("prezime") String prezime,
				@QueryParam("username") String username,
				@QueryParam("sortat") KupciSortingParam sortAt,
				@QueryParam("kupci") Boolean kupci,
				@QueryParam("zaposleni") Boolean zaposleni,
				ArrayList<ImeTipaKupca> tipovi
				) {
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		KarteDAO karDao = (KarteDAO) context.getAttribute("karteDAO");
		HashMap<String, Korisnik> korisnici = new HashMap<String , Korisnik>();
		Korisnik koris = korisniciDao.findByCookie(cookie);
		if(koris.getUloga()==Rola.KUPAC) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		
		if(koris.getUloga()==Rola.PRODAVAC) {
			for(Karta kar : karDao.karte.values()) {
				if(kar.getProdavac().equals(koris.getUsername()) && !(kar.getDeleted())) {
					Kupac kp = korisniciDao.kupci.get(kar.getKupac());
					korisnici.put(kp.getUsername(), kp);					
				}
			}
		}
		
		if(koris.getUloga()==Rola.ADMIN) {
			for(Kupac ku : korisniciDao.kupci.values()) {
				korisnici.put(ku.getUsername(), ku);
			}
			for(Admin a : korisniciDao.admini.values()) {
				korisnici.put(a.getUsername(), a);
			}
			for(Prodavac p : korisniciDao.prodavci.values()) {
				korisnici.put(p.getUsername(), p);
			}
		}
		Iterator<String> iterator = korisnici.keySet().iterator();
		while(iterator.hasNext()) {
			String trenutni = iterator.next();
			Korisnik k = korisnici.get(trenutni);
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
			if(kupci!=null) {
				if(kupci==false) {
					if(k.getUloga()==Rola.KUPAC) {
						iterator.remove();
						continue;
					}
				}
			}
			if(zaposleni!=null) {
				if(zaposleni==false) {
					if(k.getUloga()==Rola.ADMIN || k.getUloga()==Rola.PRODAVAC) {
						iterator.remove();
						continue;
					}
				}
			}
			
			if(k.getUloga()==Rola.KUPAC) {
				Kupac ku = (Kupac) k;
				if(!tipovi.contains(ku.getTip())) {
					iterator.remove();
					continue;
				}
			}

		}
		Collection<Korisnik> ckupci = korisnici.values();
		ArrayList<Korisnik> results = new ArrayList<Korisnik>(ckupci);
		if(sortAt==null) {
			sortAt = KupciSortingParam.NISTA;
		}
		switch(sortAt) {
		case IMEASC : results.sort(Comparator.comparing(Korisnik::getIme));
			break;
		case IMEDESC : results.sort(Comparator.comparing(Korisnik::getIme).reversed());
			break;
		case PREZIMEASC : results.sort(Comparator.comparing(Korisnik::getPrezime));
			break;
		case PREZIMEDESC : results.sort(Comparator.comparing(Korisnik::getPrezime).reversed());
			break;
		case USERNAMEASC : results.sort(Comparator.comparing(Korisnik::getUsername));
			break;
		case USERNAMEDESC : results.sort(Comparator.comparing(Korisnik::getUsername).reversed());
			break;
		case BODOVIASC : results.sort(Comparator.comparing(Korisnik::takeBrojBodova));
			break;
		case BODOVIDESC : results.sort(Comparator.comparing(Korisnik::takeBrojBodova).reversed());
			break;
		}



		return Response.status(Response.Status.OK).entity(results).build();
	}

	
}
