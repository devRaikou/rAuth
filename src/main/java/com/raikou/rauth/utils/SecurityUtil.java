package com.raikou.rauth.utils;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String candidate, String hashed) {
        if (hashed == null || !hashed.startsWith("$2a$"))
            return false;
        return BCrypt.checkpw(candidate, hashed);
    }
}
