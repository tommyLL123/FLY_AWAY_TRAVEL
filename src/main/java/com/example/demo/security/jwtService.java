package com.example.demo.security;

import com.example.demo.users.users;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class jwtService {

    private static final String SECRET = "demo-flight-booking-secret";
    private static final long EXPIRATION_SECONDS = 60 * 60 * 24;

    public String generateToken(users user) {
        long expiresAt = Instant.now().getEpochSecond() + EXPIRATION_SECONDS;
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + user.getId() + "\",\"email\":\"" + user.getEmail()
                + "\",\"firstName\":\"" + user.getFirstName() + "\",\"lastName\":\"" + user.getLastName()
                + "\",\"exp\":" + expiresAt + "}";

        String encodedHeader = encode(header);
        String encodedPayload = encode(payload);
        String signature = sign(encodedHeader + "." + encodedPayload);

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public Map<String, String> validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new RuntimeException("Token invalido");
        }

        String expectedSignature = sign(parts[0] + "." + parts[1]);
        if (!expectedSignature.equals(parts[2])) {
            throw new RuntimeException("Token invalido");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Map<String, String> claims = parsePayload(payload);
        long expiration = Long.parseLong(claims.get("exp"));
        if (Instant.now().getEpochSecond() > expiration) {
            throw new RuntimeException("Token expirado");
        }

        return claims;
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo firmar el token");
        }
    }

    private Map<String, String> parsePayload(String payload) {
        Map<String, String> claims = new HashMap<>();
        String cleanPayload = payload.substring(1, payload.length() - 1);
        String[] entries = cleanPayload.split(",");
        for (String entry : entries) {
            String[] pair = entry.split(":", 2);
            String key = pair[0].replace("\"", "");
            String value = pair[1].replace("\"", "");
            claims.put(key, value);
        }
        return claims;
    }
}
