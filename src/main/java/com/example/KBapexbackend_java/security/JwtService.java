package com.example.KBapexbackend_java.security;

import com.example.KBapexbackend_java.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Minimal HS256 JWT implementation backed by the JDK's HMAC primitives and the
 * application's Jackson mapper. Kept dependency-free on purpose to avoid pulling
 * in a JWT library that conflicts with the Jackson 3 runtime.
 */
@Service
public class JwtService {

    public record JwtPayload(String email, String username, String role) {
    }

    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64D = Base64.getUrlDecoder();

    private final byte[] secret;
    private final long expirationMs;
    private final JsonMapper mapper = JsonMapper.builder().build();

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserModel user) {
        long nowSeconds = System.currentTimeMillis() / 1000;
        long expSeconds = nowSeconds + (expirationMs / 1000);

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("iat", nowSeconds);
        claims.put("exp", expSeconds);

        String headerB64 = encode(toJson(header));
        String payloadB64 = encode(toJson(claims));
        String signingInput = headerB64 + "." + payloadB64;
        return signingInput + "." + sign(signingInput);
    }

    public Optional<JwtPayload> parse(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }
            String signingInput = parts[0] + "." + parts[1];
            if (!constantTimeEquals(sign(signingInput), parts[2])) {
                return Optional.empty();
            }
            JsonNode payload = mapper.readTree(new String(B64D.decode(parts[1]), StandardCharsets.UTF_8));
            long exp = payload.path("exp").asLong(0);
            if (exp > 0 && exp < System.currentTimeMillis() / 1000) {
                return Optional.empty();
            }
            return Optional.of(new JwtPayload(
                    payload.path("sub").asText(null),
                    payload.path("username").asText(null),
                    payload.path("role").asText(null)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String toJson(Map<String, Object> value) {
        return mapper.writeValueAsString(value);
    }

    private String encode(String value) {
        return B64.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return B64.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to sign JWT", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
