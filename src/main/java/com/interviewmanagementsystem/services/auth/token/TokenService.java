package com.interviewmanagementsystem.services.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenService implements ITokenService {

	@NonFinal
	@Value("${jwt.secret}")
	private String secretKey;

	@NonFinal
	@Value("${jwt.expiration}")
	private Integer expiresTime;

	@Override
	public String generateToken(Authentication authentication) {
		String roles = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(","));
		/* ADMIN,MANAGER,RECRUITER,INTERVIEWER,CANDIDATE */

		return generateAccessToken(authentication.getName(), roles);
	}

	private String generateAccessToken(String name, String roles) {
		LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiresTime);

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

		// generate token
		return Jwts.builder()
			  .subject(name)
			  .claim("roles", roles)
			  .expiration(expiration)
			  .signWith(key)
			  .compact();
	}

	@Override
	public Authentication getAuthentication(String token) {
		if (token == null) {
			return null;
		}
		// SecretKey
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		try {
			Claims claims = Jwts.parser()
				  .verifyWith(key)
				  .build()
				  .parseSignedClaims(token)
				  .getPayload();

			String roles = claims.get("roles").toString();

			Set<GrantedAuthority> authorities = Set.of(roles.split(",")).stream()
				  .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

			User principle = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principle, token, authorities);
		} catch (Exception e) {
			return null;
		}
	}
}
