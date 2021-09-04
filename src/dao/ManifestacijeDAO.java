package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Kupac;
import model.Manifestacija;

public class ManifestacijeDAO {
	public static final String fileSeparator = System.getProperty("file.separator");
    public static String resourceDir;
    
    private static String manifestacijeFileName;
    
    public static HashMap<String, Manifestacija> manifestacije;
    
	public ManifestacijeDAO(String realPath) {
		resourceDir = realPath;
        manifestacijeFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "manifestacije.json";
        loadManifestacije();
	}
	
	public void saveManifestacije() {
		try {
            new ObjectMapper().writeValue((new FileWriter(manifestacijeFileName)), manifestacije);
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
	public void dodajManifestaciju(Manifestacija mf) {
		String id = mf.getNaziv()+LocalDateTime.now().toString();
		mf.setId(id);
		mf.setRasprodata(false);
		mf.setId(id);
		mf.setDeleted(false);
		manifestacije.put(id, mf);
	}

}
