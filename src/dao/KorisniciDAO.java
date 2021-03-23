

package dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import model.enums.Pol;
import model.enums.Rola;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KorisniciDAO {
    public static final String projectDir = System.getProperty("user.dir");
    public static final String fileSeparator = System.getProperty("file.separator");

    private static final String kupciFileName = projectDir + fileSeparator + "WebContent" + fileSeparator
            + "RES" + fileSeparator + "kupci.json";
    private static final String adminiFileName = projectDir + fileSeparator + "WebContent" + fileSeparator
            + "RES" + fileSeparator + "admini.json";
    private static final String prodavciFileName = projectDir + fileSeparator + "WebContent" + fileSeparator
            + "RES" + fileSeparator + "prodavci.json";

    private HashMap<String, Kupac> kupci;
    private HashMap<String, Prodavac> prodavci;
    private HashMap<String, Admin> admini;


    public KorisniciDAO() {
        loadKorisnici();
    }

    public void saveKupci() {
        try {
            new ObjectMapper().writeValue((new FileWriter(kupciFileName)), kupci);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAdmini() {
        try {
            new ObjectMapper().writeValue((new FileWriter(adminiFileName)), admini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProdavci() {
        try {
            new ObjectMapper().writeValue((new FileWriter(prodavciFileName)), prodavci);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadKorisnici() {
        TypeReference<HashMap<String, Kupac>> kupciRef
                = new TypeReference<HashMap<String, Kupac>>() {
        };
        TypeReference<HashMap<String, Kupac>> adminiRef
                = new TypeReference<HashMap<String, Kupac>>() {
        };
        TypeReference<HashMap<String, Kupac>> prodavciRef
                = new TypeReference<HashMap<String, Kupac>>() {
        };
        try {
            kupci = new ObjectMapper().readValue(new FileReader(kupciFileName), kupciRef);
            admini = new ObjectMapper().readValue(new FileReader(adminiFileName), adminiRef);
            prodavci = new ObjectMapper().readValue(new FileReader(prodavciFileName), prodavciRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String registrujKupca(Kupac k) {
        k.setUloga(Rola.KUPAC);
        k.setDeleted(false);
        k.setBlocked(false);
        k.setBrojBodova(0);
        k.setKarte(new ArrayList<>());
        k.setCookieToken(CookieToken.createTokenValue(k.getUsername(), k.getPassword()));
        if (kupci.containsKey(k.getUsername()))
            return "";
        kupci.put(k.getUsername(), k);
        saveKupci();
        return k.getCookieToken();
    }

    public String validateUser(String token) {
        Map<String, String> parsedToken;
        try {
            parsedToken = CookieToken.parseToken(token);
        } catch (CookieToken.CookieParseException e) {
            return "";
        }
        String usn = parsedToken.get("username");
        String pass = parsedToken.get("password");
        if (kupci.containsKey(usn)) {
            Kupac k = kupci.get(usn);
            if (k.getPassword().equals(pass)) {
                return "KUPAC";
            }
        } else if (admini.containsKey(usn)) {
            Admin a = admini.get(usn);
            if (a.getPassword().equals(pass)) {
                return "ADMIN";
            }
        } else if (prodavci.containsKey(usn)) {
            Prodavac p = prodavci.get(usn);
            if (p.getPassword().equals(pass)) {
                return "PRODAVAC";
            }
        }
        return "";
    }
}
