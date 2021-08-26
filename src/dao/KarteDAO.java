package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Karta;
import model.Kupac;

public class KarteDAO {
	public static final String fileSeparator = System.getProperty("file.separator");
    public static String resourceDir;

    private static String karteFileName;
    
    private static HashMap<String, Karta> karte;
    
	public KarteDAO(String realPath) {
		resourceDir = realPath;
        karteFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "karte.json";
        loadKarte();
	}
	public void saveKarte() {
		try {
            new ObjectMapper().writeValue((new FileWriter(karteFileName)), karte);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void loadKarte() {
		TypeReference<HashMap<String, Karta>> karteRef
        = new TypeReference<HashMap<String, Karta>>() {
		};
		try {
            karte = new ObjectMapper().readValue(new FileReader(karteFileName), karteRef);
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
