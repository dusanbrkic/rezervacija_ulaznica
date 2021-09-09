package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import model.Karta;
import model.Korisnik;
import model.Kupac;
import model.Manifestacija;
import model.enums.ImeTipaKupca;
import model.enums.KarteSortingParam;
import model.enums.Rola;
import model.enums.StatusKarte;
import model.enums.TipKarte;

@Path("/karte")
public class KarteService {
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
	@Path("/getMojeKarte/{cookie}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMojeKarte(@PathParam("cookie") String cookie,
			@QueryParam("naziv") String naziv,
			@QueryParam("cenaod") double cenaOd,
			@QueryParam("cenado") double cenaDo,
			@QueryParam("datumod") String sdatumOd,
			@QueryParam("datumdo") String sdatumDo,
			@QueryParam("sortat") KarteSortingParam sortat,
			@QueryParam("statusKarte") StatusKarte status,
			ArrayList<TipKarte> tipovi
								) {
		LocalDateTime datumOd = null;
		LocalDateTime datumDo = null;
		if(sdatumDo!=null && sdatumOd!=null) {
			datumOd = LocalDateTime.parse(sdatumOd, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
			datumDo = LocalDateTime.parse(sdatumDo, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		}
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		Korisnik kr = kDao.findByCookie(cookie);
		KarteDAO karDao = (KarteDAO) context.getAttribute("karteDAO");
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		ArrayList<Karta> karte = new ArrayList<Karta>();
		if(kr.getUloga()==Rola.KUPAC) {
			for(Karta k : karDao.karte.values()) {
				if(k.getKupac().equals(kr.getUsername())){
					karte.add(k);
				}
			}
		}
		if(kr.getUloga()==Rola.PRODAVAC) {
			for(Karta k : karDao.karte.values()) {
				if(k.getProdavac().equals(kr.getUsername())) {
					karte.add(k);
				}
			}
		}
		if(kr.getUloga()==Rola.ADMIN) {
			karte = new ArrayList<Karta>(karDao.karte.values());
		}
		Iterator<Karta> iterator = karte.iterator();
		while(iterator.hasNext()) {
			Karta k = iterator.next();
			Manifestacija mf = mDao.findManifestacija(k.getManifestacija());
			if(naziv!=null && !naziv.equals("")) {
				
				if(!mf.getId().toLowerCase().contains(naziv.toLowerCase())){
					iterator.remove();
					continue;
				}
			}
			if(datumOd!=null && datumDo!=null) {
				if(mf.getVremeOdrzavanjaLDT().compareTo(datumOd)<=0 || mf.getVremeOdrzavanjaLDT().compareTo(datumDo)>=0) {
					iterator.remove();
					continue;
				}
			}
			if(k.getCena()<cenaOd || k.getCena()>cenaDo) {
				iterator.remove();
				continue;
			}
			if(tipovi!=null) {
				if(!tipovi.contains(k.getTip())) {
					iterator.remove();
					continue;
				}
				
			}
			if(status!=null) {
				if(kr.getUloga()==Rola.PRODAVAC && k.getStatus()==StatusKarte.ODUSTANAK) {
					iterator.remove();
					continue;
				}
				
				if(status!=k.getStatus()) {
					iterator.remove();
					continue;
				}
					
			}
		}
		switch(sortat) {
		case NAZIVASC: karte.sort(Comparator.comparing(Karta::getNazivManifestacije));
			break;
		case NAZIVDESC : karte.sort(Comparator.comparing(Karta::getNazivManifestacije).reversed());
			break;
		case CENAASC : karte.sort(Comparator.comparing(Karta::getCena));
			break;
		case CENADESC : karte.sort(Comparator.comparing(Karta::getCena).reversed());
			break;
		case DATUMASC : karte.sort(Comparator.comparing(Karta::getVremeManifestacijeLDT));
			break;
		case DATUMDESC : karte.sort(Comparator.comparing(Karta::getVremeManifestacijeLDT).reversed());
			break;
		}
		ArrayList<Karta> karteC = new ArrayList<Karta>();
		for(Karta k : karte) {
			Karta kC = k.getDTOcopy();
			karteC.add(kC);
		}
		return Response.status(Response.Status.OK).entity(karteC).build();
	}
	
	@GET
	@Path("/proveriCenu/{cookie}/{idm}")
	public Response proveriCenu(@PathParam("cookie") String idk,
			@QueryParam("regular") int reg,
			@QueryParam("vip") int vip,
			@QueryParam("fan") int fan,
			@PathParam("idm") String idm) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		KarteDAO karDao = (KarteDAO) context.getAttribute("karteDAO");
		Manifestacija mf = mDao.findManifestacija(idm);
		Kupac k = (Kupac) kDao.findByCookie(idk);
		int num = reg + vip + fan;
		double cena = 0;
		double regCena = mf.getRegularCena();
		cena += regCena*reg + regCena*fan*2 + regCena*vip*4;
		switch(k.getTip()) {
		case BRONZANI : break;
		case SREBRNI : cena = cena*0.98;
			break;
		case  ZLATNI : cena = cena*0.95;
			break;
		}
		return Response.status(Response.Status.OK).entity(cena).build();
	}
	
	
	@POST
	@Path("/rezervisiKarte/{cookie}/{idm}")
	public Response rezervisiKarte(@PathParam("cookie") String idk,
			@QueryParam("regular") int reg,
			@QueryParam("vip") int vip,
			@QueryParam("fan") int fan,
			@PathParam("idm") String idm) {
		ManifestacijeDAO mDao = (ManifestacijeDAO) context.getAttribute("manifestacijeDAO");
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		KarteDAO karDao = (KarteDAO) context.getAttribute("karteDAO");
		Manifestacija mf = mDao.findManifestacija(idm);
		int num = reg + vip + fan;
		Kupac k = (Kupac) kDao.findByCookie(idk);
		if(num>mf.getBrojSlobodnihMesta()) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		int dobijeniBodovi = 0;
		while(reg>0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.REGULAR);
			dobijeniBodovi+= kar.getCena()/1000*133;
			reg--;
		}
		while(vip>0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.VIP);
			dobijeniBodovi+= kar.getCena()/1000*133;
			vip--;
		}
		while(fan>0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.FAN_PIT);
			dobijeniBodovi+= kar.getCena()/1000*133;
			fan--;
		}
		mf.setBrojSlobodnihMesta(mf.getBrojSlobodnihMesta()-num);
		if(mf.getBrojSlobodnihMesta()==0) {
			mf.setRasprodata(true);
		}
		k.setBrojBodova(k.getBrojBodova()+dobijeniBodovi);
		if(k.getBrojBodova()>5000) {
			k.setTip(ImeTipaKupca.ZLATNI);
		}else if(k.getBrojBodova()>2000) {
			k.setTip(ImeTipaKupca.SREBRNI);
		}else {
			
			k.setTip(ImeTipaKupca.BRONZANI);
		}
		kDao.saveKupci();
		karDao.saveKarte();
		mDao.saveManifestacije();
		
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/otkaziKartu/{cookie}/{idk}")
	public Response otkaziKartu(@PathParam("cookie")String cookie, @PathParam("idk") String idk) {
		KorisniciDAO kDao = (KorisniciDAO) context.getAttribute("korisniciDAO");
		KarteDAO karDao = (KarteDAO) context.getAttribute("karteDAO");
		Korisnik ks = kDao.findByCookie(cookie);
		if(ks.getUloga()!=Rola.KUPAC) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Kupac kp = (Kupac) ks;
		Karta k = karDao.findKarta(idk);
		if(k==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		k.setStatus(StatusKarte.ODUSTANAK);
		double izg = 0;
		izg = k.getCena()/1000*133*4;
		kp.setBrojBodova((int) (kp.getBrojBodova()-izg));
		if(kp.getBrojBodova()>5000) {
			kp.setTip(ImeTipaKupca.ZLATNI);
		}else if(kp.getBrojBodova()>2000) {
			kp.setTip(ImeTipaKupca.SREBRNI);
		}else {
			kp.setTip(ImeTipaKupca.BRONZANI);
		}
		karDao.saveKarte();
		kDao.saveKupci();
		return Response.status(Response.Status.OK).build();
	}
}
