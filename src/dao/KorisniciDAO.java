

package dao;

import com.fasterxml.jackson.core.JsonParseException;
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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KorisniciDAO {
	public static final String projectDir = System.getProperty("user.dir");
	public static final String fileSeparator = System.getProperty("file.separator");
	
    private static final String korisniciFileName  = projectDir + fileSeparator + "WebContent" + fileSeparator
    		+ "RES" + fileSeparator + "korisnici.json";
    private ArrayList<Korisnik> korisnici;

    public KorisniciDAO() {
        //loadKorisnici();
    	System.out.println(System.getProperty("user.dir"));
    	writeKorisniciFromCode();
    }

    private void writeKorisniciFromCode() {
    	String sDate1="31/12/1998";
    	Date date1 = null;
		try {
			date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Kupac k = new Kupac("usn", "pass", "ime", "prz", date1, Rola.KUPAC, Pol.MUSKI);
    	List<Korisnik> korisnici = new ArrayList<Korisnik>();
    	korisnici.add(k);
    	try {
			new ObjectMapper().writeValue(new BufferedWriter(new FileWriter(new File(korisniciFileName))), korisnici);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadKorisnici() {
        try {
            List<Korisnik> korisnici = Arrays.asList(
            		new ObjectMapper().readValue(korisniciFileName, Korisnik[].class));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
