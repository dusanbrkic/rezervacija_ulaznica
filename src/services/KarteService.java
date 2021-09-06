package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import dao.KarteDAO;
import dao.KorisniciDAO;
import dao.ManifestacijeDAO;
import model.Karta;
import model.Kupac;
import model.Manifestacija;
import model.enums.ImeTipaKupca;
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
	
	@GET
	@Path("/proveriCenu/{cookie}/{num}/{idm}")
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
	@Path("/rezervisiKarte/{cookie}/{num}/{idm}")
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
		while(reg<0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.REGULAR);
			dobijeniBodovi+= kar.getCena()/1000*133;
			reg--;
		}
		while(vip<0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.VIP);
			dobijeniBodovi+= kar.getCena()/1000*133;
			vip--;
		}
		while(fan<0) {
			Karta kar = karDao.dodajKartu(k, mf, TipKarte.FAN_PIT);
			dobijeniBodovi+= kar.getCena()/1000*133;
			fan--;
		}
		mf.setBrojSlobodnihMesta(mf.getBrojSlobodnihMesta()-num);
		k.setBrojBodova(k.getBrojBodova()+dobijeniBodovi);
		if(k.getBrojBodova()>5000) {
			k.setTip(ImeTipaKupca.ZLATNI);
		}else if(k.getBrojBodova()>2000) {
			k.setTip(ImeTipaKupca.SREBRNI);
		}else {
			k.setTip(ImeTipaKupca.BRONZANI);
		}
		
		return Response.status(Response.Status.OK).build();
	}
}
