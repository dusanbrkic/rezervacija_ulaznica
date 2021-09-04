package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Komentar;
import model.Kupac;
import model.Manifestacija;

public class ManifestacijeDAO {
	public static final String fileSeparator = System.getProperty("file.separator");
    public static String resourceDir;
    
    private static String manifestacijeFileName;
    private static String komentariFileName;
    
    public static HashMap<String, Manifestacija> manifestacije;
    public static HashMap<String, Komentar> komentari;
    
	public ManifestacijeDAO(String realPath) {
		resourceDir = realPath;
        manifestacijeFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "manifestacije.json";
        komentariFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "komentari.json";
        loadManifestacije();
	}
	
	public void saveManifestacije() {
		try {
            new ObjectMapper().writeValue((new FileWriter(manifestacijeFileName)), manifestacije);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public void saveKomentari() {
		try {
            new ObjectMapper().writeValue((new FileWriter(komentariFileName)), komentari);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void loadManifestacije() {
		TypeReference<HashMap<String, Manifestacija>> manifestacijeRef
        = new TypeReference<HashMap<String, Manifestacija>>() {
		};
		try {
            manifestacije = new ObjectMapper().readValue(new FileReader(manifestacijeFileName), manifestacijeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
		
	private void loadKomentari() {
		TypeReference<HashMap<String, Komentar>> komentariRef
        = new TypeReference<HashMap<String, Komentar>>() {
		};
		try {
            komentari = new ObjectMapper().readValue(new FileReader(komentariFileName), komentariRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Manifestacija findManifestacija(String key) {
		
		if(manifestacije.containsKey(key)) {
			return manifestacije.get(key);
		}
		return null;
	}

	public void dodajManifestaciju(Manifestacija mf) {
		String id = mf.getNaziv()+LocalDateTime.now().toString();
		mf.setId(id);
		mf.setRasprodata(false);
		mf.setId(id);
		mf.setDeleted(false);
		manifestacije.put(id, mf);
		saveManifestacije();
	}

}
