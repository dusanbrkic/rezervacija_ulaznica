package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CookieToken {

    public static String createTokenValue(String username, String password) {
        return username + "-" + password;
    }

    public static Map<String, String> parseToken(String token) throws CookieParseException {
        if (!token.contains("-"))
            throw new CookieParseException();
        Map<String, String> retVal = new HashMap<>();
        String[] values = token.split("-");
        retVal.put("username", values[0]);
        retVal.put("password", values[1]);
        return retVal;
    }

    public static class CookieParseException extends Exception {}
}
