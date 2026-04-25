package com.example.PDS_BACKEND.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretKey;

    public JWTService() {
        secretKey = generateSecretkey();
    }

    //[3]
    //method for generating secret key which is used inside generateToken() method
    public String generateSecretkey()
    {
        try
        {
            // Create a KeyGenerator instance for HmacSHA256 algorithm
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

            // Generate a secret key using the above algorithm
            SecretKey secretKey = keyGen.generateKey();

            // Printing the SecretKey object directly
            // NOTE: This does NOT print the actual key value, just a reference (like javax.crypto.spec.SecretKeySpec@xxx)
            System.out.println("Secret Key : " + secretKey.toString());

            // Convert the raw key bytes into Base64 encoded string
            // This makes the key readable and easy to store or send (e.g., in config files, JWT signing)
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    //[1]
    //method for generating jwt token
    public String generateToken(String username)
    {

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)      //user data
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  //current time
                .setExpiration(new Date(System.currentTimeMillis()  + 1000*60*3 ))  //current time + 3 minutes
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //[2]
    //getKey() method used for generating jwt token in generateToken() method
    public Key getKey()
    {

        // Decode the Base64 encoded secret key string back into raw bytes
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // Create an HMAC-SHA key using those bytes
        // This is typically used for signing/verifying JWT tokens
        return Keys.hmacShaKeyFor(keyBytes);

    }

    //methods for extracting username and validate the token:

    // Extracts the username (subject) from the JWT token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // get all claims
        final Claims claims = extractAllClaims(token);
        // return specific claim
        return claimResolver.apply(claims);
    }

    //parses token and gets all payload data
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    //check username + expiry
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token expiration date is before current time
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // get expiry time
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    }


