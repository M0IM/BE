package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.global.error.GeneralException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
public class JwtOIDCUtil {

    private final String KID = "kid";

    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        return (String) getUnsignedTokenClaims(token, iss, aud).getHeader().get(KID);
    }

    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) {

        String unsignedToken = getUnsignedToken(token);
        log.info("token : {}", token);
        log.info("unsignedToken : {}", unsignedToken);

        try {
            Jwt<Header, Claims> testJjwts = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(unsignedToken);

            log.info("Parsed Issuer: {}", testJjwts.getBody().getIssuer());
            log.info("Parsed Audience: {}", testJjwts.getBody().getAudience());
            log.info("iss : {}", iss);
            log.info("aud : {}", aud);

            Jwt<Header, Claims> jwts = Jwts.parserBuilder()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseClaimsJwt(unsignedToken);

            log.info("jwts = {}", jwts);
            return jwts;
        } catch (ExpiredJwtException e) {
            log.error("만료된 ID 토큰");
            throw new GeneralException(ID_TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error("유효하지 않은 ID 토큰");
            throw new GeneralException(ID_TOKEN_INVALID);
        }
    }

    private String getUnsignedToken(String token) {

        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) {
            throw new GeneralException(OAUTH_INVALID_TOKEN);
        }
        return splitToken[0] + "." + splitToken[1] + ".";
    }

    public OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent) {

        log.info("modules : {}", modulus);
        log.info("exponent : {}", exponent);

        Claims body = getOIDCTokenJws(token, modulus, exponent).getBody();
        return new OIDCDecodePayload(
                body.getIssuer(),
                body.getAudience(),
                body.getSubject(),
                body.get("email", String.class)
        );
    }

    private Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.error("만료된 ID 토큰");
            throw new GeneralException(AUTH_EXPIRED_TOKEN);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("유효하지 않은 ID 토큰");
            throw new GeneralException(OAUTH_INVALID_TOKEN);
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        log.info("keyfactory : {}", keyFactory);

        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        log.info("decodeN : {}", decodeN);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        log.info("decodeE : {}", decodeE);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        log.info("keySpec : {}", keySpec);
        Key key = keyFactory.generatePublic(keySpec);
        log.info("key : {}", key);
        return key;
    }
}
