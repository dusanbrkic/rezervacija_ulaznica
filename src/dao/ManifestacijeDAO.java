package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Komentar;
import model.Kupac;
import model.Manifestacija;
import services.ManifestacijeService;

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
        loadKomentari();
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
	
	public void dodajKomentar(Komentar k) {
		komentari.put(k.getId(), k);
	}
	
	public Komentar findKomentar(String key) {
		if(komentari.containsKey(key)) {
			komentari.get(key);
		}
		return null;
	}

	public void dodajManifestaciju(Manifestacija mf) {
		String id = mf.getId();
		mf.setId(id);
		mf.setRasprodata(false);
		mf.setDeleted(false);
		manifestacije.put(id, mf);
		String slike = resourceDir + fileSeparator + "RES" + File.separator + "slicice" + File.separator + "posteri";
		String imageDataBytes = mf.getPoster().substring(mf.getPoster().indexOf(",")+1);
		mf.setPoster("./RES/slicice/posteri/" + id +".jpg"); // ./RES/slicice/posteri/exit_poster.jpg

	   byte[] data = Base64.getDecoder().decode(imageDataBytes);
	   
	   	try(OutputStream stream = new FileOutputStream(slike + File.separator + id +".jpg")) {
	   		stream.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveManifestacije();
	}
	
	public void izmeniManifestaciju(Manifestacija nova, Manifestacija stara) {
		stara.setBrojMesta(nova.getBrojMesta());
		stara.setBrojSlobodnihMesta(nova.getBrojMesta());
		stara.setLokacija(nova.getLokacija());
		stara.setRegularCena(nova.getRegularCena());
		stara.setTip(nova.getTip());
		if(!(nova.getPoster()==null || nova.getPoster().equals(""))) {
			String slike = resourceDir + fileSeparator + "RES" + File.separator + "slicice" + File.separator + "posteri";
			String imageDataBytes = nova.getPoster().substring(nova.getPoster().indexOf(",")+1);
			stara.setPoster("./RES/slicice/posteri/" +stara.getId() +".jpg");
			byte[] data = Base64.getDecoder().decode(imageDataBytes);
			   
		   	try(OutputStream stream = new FileOutputStream(slike + File.separator + stara.getId() +".jpg")) {
		   		stream.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		saveManifestacije();
	}

}
