package services;

import java.time.LocalDateTime;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.KarteDAO;
import dao.KorisniciDAO;
import dao.ManifestacijeDAO;
import model.Manifestacija;
import model.enums.ManifestacijaSortingParam;
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
			context.setAttribute("korisniciDAO", kard);		
		}
		if(context.getAttribute("manifestacijeDAO")==null) {
			ManifestacijeDAO md = new ManifestacijeDAO(context.getRealPath("/"));
			context.setAttribute("manifestacijeDAO", md);
		}
	}
	@GET
	@Path("/getManifestacije")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManifestacije(@QueryParam("naziv") String naziv,
			 						 @QueryParam("lokacija") String lokacija,
									 @QueryParam("datumod") LocalDateTime datumOd,
	 								 @QueryParam("datumdo") LocalDateTime datumDo,
	 								 @QueryParam("lokacijaGd") String lokacijaGd, 
	 								 @QueryParam("cenaod") Double cenaOd,
	 								 @QueryParam("cenado") Double cenaDo,
									 @QueryParam("tip") TipManifestacije tip, 
									 @QueryParam("irasprodata")Boolean irasprodata,
									 @QueryParam("sortat") ManifestacijaSortingParam sortAt
									 ) {
		HashMap<String , Manifestacija> manifestacije = (HashMap<String, Manifestacija>) context.getAttribute("manifestacijeDAO");
		Iterator<String> iterator = manifestacije.keySet().iterator();
		while(iterator.hasNext()) {
			String trenutni = iterator.next();
			Manifestacija mf = manifestacije.get(trenutni);
			if(mf.getNaziv().toLowerCase().contains(naziv.toLowerCase())){
				iterator.remove();
				continue;
			}
			if(mf.getLokacija().getAdresa().toLowerCase().contains(lokacija.toLowerCase())){
				iterator.remove();
				continue;
			}
			if(datumOd!=null && datumDo!=null) {
				if(mf.getVremeOdrzavanja().compareTo(datumOd)<=0 || mf.getVremeOdrzavanja().compareTo(datumDo)>=0) {
					iterator.remove();
					continue;
				}
			}
			if(mf.getLokacija().getGrad().toLowerCase().contains(lokacijaGd.toLowerCase())) {
				iterator.remove();
				continue;
			}
			if(mf.getRegularCena()<cenaOd || mf.getRegularCena()>cenaDo) {
				iterator.remove();
				continue;
			}
			if(tip!=null) {
				if(tip!=mf.getTip()) {
					iterator.remove();
				}
				
			}
			if(irasprodata == false) {
				if(mf.getRasprodata()==true) {
					iterator.remove();
					continue;
				}
			}
		}
		ArrayList<Manifestacija> results = (ArrayList<Manifestacija>) manifestacije.values();
		switch(sortAt) {
		case NAZIVASC : results.sort(Comparator.comparing(Manifestacija::getNaziv));
			break;
		case NAZIVDESC : results.sort(Comparator.comparing(Manifestacija::getNaziv).reversed());
		break;
		case VREMEASC : results.sort(Comparator.comparing(Manifestacija::getVremeOdrzavanja));
		break;
		case VREMEDESC : results.sort(Comparator.comparing(Manifestacija::getVremeOdrzavanja).reversed());
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
}
