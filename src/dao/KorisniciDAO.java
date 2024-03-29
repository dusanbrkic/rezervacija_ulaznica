

package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CookieParseException;
import exceptions.UnknownUsernameException;
import exceptions.UsernameExistsException;
import exceptions.WrongPasswordException;
import model.*;
import model.enums.Rola;

import javax.servlet.ServletContext;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class KorisniciDAO {
    public static final String fileSeparator = System.getProperty("file.separator");
    public static String resourceDir;

    private static String kupciFileName;
    private static String adminiFileName;
    private static String prodavciFileName;

    private HashMap<String, Kupac> kupci;
    private HashMap<String, Prodavac> prodavci;
    private HashMap<String, Admin> admini;


    public KorisniciDAO(String realPath) {
        resourceDir = realPath;
        kupciFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "kupci.json";
        adminiFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "admini.json";
        prodavciFileName = resourceDir + fileSeparator + "RES" + fileSeparator + "prodavci.json";
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

    public String registrujKupca(Kupac k) throws UsernameExistsException {
        k.setUloga(Rola.KUPAC);
        k.setDeleted(false);
        k.setBlocked(false);
        k.setBrojBodova(0);
        k.setKarte(new ArrayList<>());
        k.setCookieToken(CookieToken.createTokenValue(k.getUsername(), k.getPassword()));
        if (kupci.containsKey(k.getUsername()))
            throw new UsernameExistsException();
        kupci.put(k.getUsername(), k);
        saveKupci();
        return k.getCookieToken();
    }

    public String validateUser(String token) {
        Map<String, String> parsedToken;
        try {
            parsedToken = CookieToken.parseToken(token);
        } catch (CookieParseException e) {
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

    public String loginUser(String username, String password) throws WrongPasswordException, UnknownUsernameException {
        if (kupci.containsKey(username)) {
            Kupac k = kupci.get(username);
            if (k.getPassword().equals(password)) {
                return k.getCookieToken();
            }
        } else if (admini.containsKey(username)) {
            Admin a = admini.get(username);
            if (a.getPassword().equals(password)) {
                return a.getCookieToken();
            }
        } else if (prodavci.containsKey(username)) {
            Prodavac p = prodavci.get(username);
            if (p.getPassword().equals(password)) {
                return p.getCookieToken();
            }
        } else {
            throw new UnknownUsernameException();
        }
        throw new WrongPasswordException();
    }
}
