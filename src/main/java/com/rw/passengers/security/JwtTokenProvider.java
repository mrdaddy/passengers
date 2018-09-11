package com.rw.passengers.security;

import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${security.jwt.token.public-key}")
    private String realmPublicKey;
    private PublicKey publicKey;

    @PostConstruct
    protected void init() {
        try {
            publicKey = decodePublicKey(pemToDer(realmPublicKey));
        } catch (Exception e) {
            throw new RuntimeException("Invalid public key");
        }
    }

    public Authentication getAuthentication(String token) {
        String login = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();
        ArrayList<Map<String,String>> list = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().get("auth",ArrayList.class);
        String[] ids = list.stream()
                .map(am -> am.get("authority"))
                .toArray(String[]::new);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(ids)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(login, "",
                authorities);
        return new UsernamePasswordAuthenticationToken(principal, "test", authorities);
    }

    public User getUser(String token) {
        User user = new User();
        Map map = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().get("user", java.util.LinkedHashMap.class);
        user.setId(Long.parseLong(map.get("id").toString()));
        user.setEmail(map.get("email") != null ? map.get("email").toString() : null);
        user.setLogin(Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject());
        if(map.get("language")!=null) {
            user.setLanguage(map.get("language").toString());
        } else {
            user.setLanguage("ru");
        }
        return user;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Decode a PEM string to DER format
     *
     * @param pem
     * @return
     * @throws java.io.IOException
     */
    public static byte[] pemToDer(String pem) throws IOException {
        return Base64.getDecoder().decode(pem);
    }


    public static PublicKey decodePublicKey(byte[] der) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);

        KeyFactory kf = KeyFactory.getInstance("RSA"
                //        , "BC" //use provider BouncyCastle if available.
        );
        return kf.generatePublic(spec);
    }

}