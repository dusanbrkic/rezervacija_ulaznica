package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.KarteDAO;
import dao.KorisniciDAO;
import dao.ManifestacijeDAO;
import exceptions.InvalidTokenException;
import model.Komentar;
import model.Korisnik;
import model.Manifestacija;
import model.enums.ManifestacijaSortingParam;
import model.enums.Rola;
import model.enums.StatusManifestacije;
import model.enums.TipManifestacije;

@Path("/manifestacije")
public class ManifestacijeService {
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
	@Path("/getManifestacije")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManifestacije(@QueryParam("naziv") String naziv,
			 						 @QueryParam("lokacija") String lokacija,
									 @QueryParam("datumod") String sdatumOd,
	 								 @QueryParam("datumdo") String sdatumDo,
	 								 @QueryParam("lokacijaGd") String lokacijaGd, 
	 								 @QueryParam("cenaod") Double cenaOd,
	 								 @QueryParam("cenado") Double cenaDo,
									 ArrayList<TipManifestacije> tip,
									 @QueryParam("rasprodate")StatusManifestacije rasprodate,//true - samo rasprodate false - samo nerasprodate null-sve
									 @QueryParam("sortat") ManifestacijaSortingParam sortAt
									 ) {
		LocalDateTime datumOd = null;
		LocalDateTime datumDo = null;
		if(sdatumDo!=null && sdatumOd!=null) {
			datumOd = LocalDateTime.parse(sdatumOd, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
			datumDo = LocalDateTime.parse(sdatumDo, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		}
		ManifestacijeDAO manifestacijeDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		HashMap<String, Manifestacija> manifestacije = (HashMap<String, Manifestacija>) manifestacijeDao.manifestacije.clone();
		Iterator<String> iterator = manifestacije.keySet().iterator();
		while(iterator.hasNext()) {
			String trenutni = iterator.next();
			Manifestacija mf = manifestacije.get(trenutni);
			if(mf.getDeleted()) {
				iterator.remove();
				continue;
			}
			if(!mf.getNaziv().toLowerCase().contains(naziv.toLowerCase())){
				iterator.remove();
				continue;
			}
			if(!mf.getLokacija().getAdresa().toLowerCase().contains(lokacija.toLowerCase())){
				iterator.remove();
				continue;
			}
			if(datumOd!=null && datumDo!=null) {
				if(mf.getVremeOdrzavanjaLDT().compareTo(datumOd)<=0 || mf.getVremeOdrzavanjaLDT().compareTo(datumDo)>=0) {
					iterator.remove();
					continue;
				}
			}
			if(!mf.getLokacija().getGrad().toLowerCase().contains(lokacijaGd.toLowerCase())) {
				iterator.remove();
				continue;
			}
			if(mf.getRegularCena()<cenaOd || mf.getRegularCena()>cenaDo) {
				iterator.remove();
				continue;
			}
			if(tip!=null) {
				if(!tip.contains(mf.getTip())) {
					iterator.remove();
					continue;
				}
				
			}
			if(mf.getAktivna()==false) {
				iterator.remove();
				continue;
			}
			if(rasprodate == StatusManifestacije.SVE) {
				continue;
			}
				else if(rasprodate == StatusManifestacije.RASPRODATE ) {
					if(mf.getRasprodata()==false) {
						iterator.remove();
						continue;
					}
				}else {
					if(mf.getRasprodata()==true) {
						iterator.remove();
						continue;
					}
				}
				
			}
		ArrayList<Manifestacija> results = new ArrayList<Manifestacija> (manifestacije.values());
		switch(sortAt) {
		case NAZIVASC : results.sort(Comparator.comparing(Manifestacija::getNaziv));
			break;
		case NAZIVDESC : results.sort(Comparator.comparing(Manifestacija::getNaziv).reversed());
		break;
		case VREMEASC : results.sort(Comparator.comparing(Manifestacija::getVremeOdrzavanjaLDT));
		break;
		case VREMEDESC : results.sort(Comparator.comparing(Manifestacija::getVremeOdrzavanjaLDT).reversed());
		break;
		case CENAASC : results.sort(Comparator.comparing(Manifestacija::getRegularCena));
		break;
		case CENADESC : results.sort(Comparator.comparing(Manifestacija::getRegularCena).reversed());
		break;
		case LOKACIJAASC : results.sort(Comparator.comparing(Manifestacija::getNazivLokacije));
		break;
		case LOKACIJADESC : results.sort(Comparator.comparing(Manifestacija::getNazivLokacije).reversed());
		break;
		}
		return Response.status(Response.Status.OK).entity(results).build();
	}
	@GET
	@Path("/getManifestacija/{idm}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManifestacija(@PathParam("idm")String idm) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		Manifestacija mf = mDao.findManifestacija(idm);
		if(mf==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.status(Response.Status.OK).entity(mf).build();
	}
	
	@GET
	@Path("/getManifestacijeZaOdobriti/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManifestacijeZaOdobriti(@PathParam("cookie")String cookie) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Korisnik k  = kDao.findByCookie(cookie);
		if(k.getUloga()!=Rola.ADMIN) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		ArrayList<Manifestacija> manifestacije = new ArrayList<Manifestacija>();
		for(Manifestacija mf : mDao.manifestacije.values()) {
			if(mf.getAktivna()==false) {
				manifestacije.add(mf);
			}
		}
		return Response.status(Response.Status.OK).entity(manifestacije).build();
	}
	
	
	@GET
	@Path("/getMojeManifestacije/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMojeManifestacije(@PathParam("cookie") String cookie) {
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Korisnik k = kDao.findByCookie(cookie);
		if(k.getUloga()!=Rola.PRODAVAC) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		ArrayList<Manifestacija> manifestacije = new ArrayList<Manifestacija>();
		for(Manifestacija mf : mDao.manifestacije.values()) {
			if(mf.getProdavac().equals(k.getUsername())){
				manifestacije.add(mf);
			}
			
		}
		return Response.status(Response.Status.OK).entity(manifestacije).build();
	}
	
	@POST
	@Path("/dodajManifestaciju/{cookie}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dodajManifestaciju(Manifestacija mf, @PathParam("cookie") String cookie,
			@QueryParam("vreme") String svreme) {
		LocalDateTime vreme = LocalDateTime.parse(svreme, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		mf.setVremeOdrzavanjaLDT(vreme);
		if(mf.getNaziv()==null || mf.getVremeOdrzavanjaLDT()==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Korisnik k = kDao.findByCookie(cookie);
		if(k==null || (!(k.getUloga()==Rola.PRODAVAC))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		mf.setProdavac(k.getUsername());
		mf.setAktivna(false);
		mDao.dodajManifestaciju(mf);
		mDao.saveManifestacije();
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/obrisiManifestaciju/{cookie}/{idm}")
	//@Consumes(MediaType.APPLICATION_JSON)
	public Response obrisiManifestaciju(@PathParam("cookie") String cookie,@PathParam("idm")String idm) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Korisnik k = kDao.findByCookie(cookie);
		if(k.getUloga()!=Rola.ADMIN) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Manifestacija mf = mDao.findManifestacija(idm);
		if(mf==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		mf.setDeleted(true);
		mDao.saveManifestacije();
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/odobriManifestaciju/{idm}/{cookie}")
	public Response odobriManifestaciju(@PathParam("idm")String idm, @PathParam("cookie") String cookie) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		
		Korisnik k = kDao.findByCookie(cookie);
		if(k.getUloga()!=Rola.ADMIN) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Manifestacija mf = mDao.findManifestacija(idm);
		if(mf==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		mf.setAktivna(true);
		mDao.saveManifestacije();
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/postaviKomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postaviKomentar(Komentar k) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KarteDAO kDao = (KarteDAO) context.getAttribute("karteDAO");
		//Manifestacija mf = mDao.findManifestacija(k.getManifestacija());
		if(LocalDateTime.now().isBefore(mDao.manifestacije.get(k.getManifestacija()).getVremeOdrzavanjaLDT())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		if(!kDao.proveriRezervisanost(k.getManifestacija(), k.getKupac())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		k.setDeleted(false);
		mDao.dodajKomentar(k);
		mDao.saveKomentari();
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/odobriKomentar/{komentar}/{odobren}")
	public Response odobriKomentar(@PathParam("komentar") String komentar, @PathParam("odobren") boolean odobren) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		Komentar k = mDao.findKomentar(komentar);
		k.setOdobren(odobren);
		mDao.saveKomentari();
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/getKomentari/{idm}/{cookie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKomentari(@PathParam("idm") String idm,@PathParam("cookie") String cookie) {
		
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		Korisnik kor = kDao.findByCookie(cookie);
		ArrayList<Komentar> komentari = new ArrayList<Komentar>();
		if(kor==null || kor.getUloga()==Rola.KUPAC) {
			for(Komentar k : mDao.komentari.values()) {
				if(k.getManifestacija().equals(idm)) {
					if(!k.getDeleted()) {
						if(k.getOdobren()) {
							komentari.add(k);
						}
					}
				}
			}
		}
		
		if(kor.getUloga()==Rola.PRODAVAC || kor.getUloga()==Rola.ADMIN) {
			for(Komentar k : mDao.komentari.values()) {
				if(k.getManifestacija().equals(idm)) {
					if(!k.getDeleted()) {
						komentari.add(k);
					}
				}
			}
		}
		
		
		return Response.status(Response.Status.OK).entity(komentari).build();
	}
	
	
}
