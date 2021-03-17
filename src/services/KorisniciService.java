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

@Path("/korisnici")
public class KorisniciService {
	
	@Context
	ServletContext context;
	
	@Context
	HttpServletRequest request;

	@PostConstruct
	public void init() {
		if(context.getAttribute("komentariDAO")==null) {
			KorisniciDAO dao = new KorisniciDAO();
			context.setAttribute("komentariDAO", dao);
		}
	}
	
	@POST
	@Path("/registracija")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean registracija(Korisnik k) {
		System.out.println("Registruje se korisnik: "+k.getDatumRodjenja());
		
		return false;
	}
}
