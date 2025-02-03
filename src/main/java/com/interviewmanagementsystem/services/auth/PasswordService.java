package com.interviewmanagementsystem.services.auth;

import com.ninja_in_pyjamas.entities.PasswordResetToken;
import com.ninja_in_pyjamas.repositories.IPasswordResetTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class PasswordService implements IPasswordService{

	private final PasswordEncoder passwordEncoder;
	private final IPasswordResetTokenRepository tokenRepository;

	public PasswordService(PasswordEncoder passwordEncoder, IPasswordResetTokenRepository tokenRepository) {
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
	}

	@Value("${jwt.expiration}")
	private int expireTime;

	@Value("${jwt.secret}")
	private String secretKey;

	@Override
	public String generatePassword(int length) {
		if (length < 8) {
			throw new IllegalArgumentException("Password length must be at least 8");
		}

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*()_+";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}

		return sb.toString();
	}


	@Override
	public String hashPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean verifyPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	@Override
	public String generatePasswordResetToken(String email) {
		//generate token id
		String jsonTokenId = UUID.randomUUID().toString();
		LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(expireTime);
		Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		String jsonWebToken = Jwts.builder()
			  .subject(email)
			  .id(jsonTokenId)
			  .expiration(expiration)
			  .signWith(key)
			  .compact();

		//hash the `jsonTokenId`
		String hashedTokenId = passwordEncoder.encode(jsonTokenId);

		//save token details to database
		PasswordResetToken token = new PasswordResetToken(
			  hashedTokenId,
			  email,
			  ZonedDateTime.now().plusMinutes(15)
		);

		tokenRepository.save(token);

		// return the token
		return jsonWebToken;
	}

	@Override
	public boolean validatePasswordResetToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		Claims claims;
		try {
			claims = Jwts.parser()
				  .verifyWith(key)
				  .build()
				  .parseSignedClaims(token)
				  .getPayload();
		} catch (JwtException e) {
			throw new IllegalArgumentException("Invalid token", e);
		}

		String jsonTokenId = claims.getId();
		String email = claims.getSubject();

		PasswordResetToken resetToken = tokenRepository.findByEmail(email)
			  .orElseThrow(() -> new IllegalArgumentException("Token not found"));

		if (!passwordEncoder.matches(jsonTokenId, resetToken.getHashedTokenId())) {
			throw new IllegalArgumentException("Invalid token");
		}

		if (resetToken.getExpiration().isBefore(ZonedDateTime.now())) {
			throw new IllegalArgumentException("Expired token");
		}

		return true;
	}


	@Override
	public String getEmailFromPasswordResetToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		// Parse the jsonWebToken
		Claims claims = Jwts.parser()
			  .verifyWith(key)
			  .build()
			  .parseSignedClaims(token)
			  .getPayload();

		return claims.getSubject();
	}

	@Override
	public boolean deletePasswordResetToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		//parse the jsonWebToken
		Claims claims = Jwts.parser()
			  .verifyWith(key)
			  .build()
			  .parseSignedClaims(token)
			  .getPayload();

		//check database for hashed token
		PasswordResetToken resetToken = tokenRepository.findByEmail(claims.getSubject())
			  .orElseThrow(() -> new IllegalArgumentException("Token not found"));

		tokenRepository.deleteByHashedTokenId(resetToken.getHashedTokenId());

		var result = tokenRepository.existsById(resetToken.getId());

		return !result;
	}

	@Override
	public boolean matchHashedPassword(String oldPassword, String hashedPassword) {
		return passwordEncoder.matches(oldPassword, hashedPassword);
	}
}
