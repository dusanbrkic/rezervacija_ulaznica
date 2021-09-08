package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Karta;
import model.Kupac;
import model.Manifestacija;
import model.enums.StatusKarte;
import model.enums.TipKarte;

public class KarteDAO {
	public static final String fileSeparator = System.getProperty("file.separator");
    public static String resourceDir;

    private static String karteFileName;
    
    public static HashMap<String, Karta> karte;
    private static int sequencer = 1000000000;
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
		sequencer+= karte.size();

	}
	
	public Karta dodajKartu(Kupac kp , Manifestacija mf, TipKarte tip) {
		Karta k = new Karta();
		sequencer++;
		k.setId(Integer.toString(sequencer));
		k.setKupac(kp.getUsername());
		k.setManifestacija(mf.getId());
		k.setProdavac(mf.getProdavac());
		k.setStatus(StatusKarte.REZERVISANA);
		k.setVremeManifestacije(mf.getVremeOdrzavanjaLDT().toString());
		k.setTip(tip);
		switch(tip) {
		case VIP : k.setCena(mf.getRegularCena()*4);
			break;
		case FAN_PIT : k.setCena(mf.getRegularCena()*2);
			break;
		case REGULAR : k.setCena(mf.getRegularCena());
			break;
		}
		karte.put(k.getId(), k);
		return k;
	}
	
	public Karta findKarta(String key) {
		
		if(karte.containsKey(key)) {
			return karte.get(key);
		}
		
		return null;
	}
	
	public boolean proveriRezervisanost(String mf, String kupac) {
		for(Karta k : karte.values()){
			if(k.getManifestacija().equals(mf) && k.getStatus()==StatusKarte.REZERVISANA && k.getKupac().equals(kupac)) {
				return false;
			}
		}
		
		return false;
	}

}
