

package dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Korisnik;
import model.Kupac;
import model.enums.Pol;
import model.enums.Rola;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KorisniciDAO {
	public static final String projectDir = System.getProperty("user.dir");
	public static final String fileSeparator = System.getProperty("file.separator");
	
    private static final String korisniciFileName  = projectDir + fileSeparator + "WebContent" + fileSeparator
    		+ "RES" + fileSeparator + "korisnici.json";
    private HashMap<String, Korisnik> korisnici;

    public KorisniciDAO() {
        korisnici = new HashMap<>();
    	//loadKorisnici();
    }

    public void saveKorisnici() {
    	try {
			new ObjectMapper().writeValue((new FileWriter(korisniciFileName)), korisnici);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadKorisnici() {
		TypeReference<HashMap<String,Korisnik>> typeRef
				= new TypeReference<HashMap<String,Korisnik>>() {};
		try {
			korisnici = new ObjectMapper().readValue(korisniciFileName, typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void registrujKupca(Kupac k) {
    	k.setBlocked(false);
    	k.setBrojBodova(0);
    	k.setUloga(Rola.KUPAC);
    	k.setKarte(new ArrayList<>());
    	korisnici.put(k.getUsername(), k);
    	saveKorisnici();
	}
}
