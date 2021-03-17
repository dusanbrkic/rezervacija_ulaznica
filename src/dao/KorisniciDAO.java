

package dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Korisnik;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KorisniciDAO {
    private static final String korisniciFileName  = "../RES/korisnici.json";
    private ArrayList<Korisnik> korisnici;

    //public

    public KorisniciDAO() {
        System.out.println("dao sam ti tortu");
        //loadKorisnici();
    }

    private void loadKorisnici() {
        ObjectMapper om = new ObjectMapper();
        try {
            List<Korisnik> korisnici = Arrays.asList(om.readValue(korisniciFileName, Korisnik[].class));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
