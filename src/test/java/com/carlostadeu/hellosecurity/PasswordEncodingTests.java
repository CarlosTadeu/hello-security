package com.carlostadeu.hellosecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncodingTests {

    static final String PASSWORD = "password";

    @Test
    void bcrypt15() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);
        String result = bcrypt.encode(PASSWORD);
        System.out.println(result);
        assertTrue(bcrypt.matches(PASSWORD, result));
    }

    @Test
    void bcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String result = bcrypt.encode(PASSWORD);
        System.out.println(result);
        assertTrue(bcrypt.matches(PASSWORD, result));
    }

    @Test
    void sha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        String result = sha256.encode(PASSWORD);
        System.out.println(result);
        assertTrue(sha256.matches(PASSWORD, result));
    }

    @Test
    void ldap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        String result = ldap.encode(PASSWORD);
        System.out.println(result);
        assertTrue(ldap.matches(PASSWORD, result));
    }

    @Test
    void hashing() {
        PasswordEncoder hash = new MessageDigestPasswordEncoder("md5");
        String salted = PASSWORD + "thisissalt";
        String result = hash.encode(salted);
        System.out.println(result);
        assertTrue(hash.matches(salted, result));
    }

    @Test
    void noOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        String result = noOp.encode(PASSWORD);
        System.out.println(result);
        assertTrue(noOp.matches(PASSWORD, result));
    }
}
