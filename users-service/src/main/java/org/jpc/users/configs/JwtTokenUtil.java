package org.jpc.users.configs;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.jpc.users.entities.Customer;
import org.jpc.users.entities.Wallet;
import org.jpc.users.repos.RepoCustomer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	private final Properties properties;
	private final RepoCustomer rCustomer;
	private String userToken;

	public void setUserToken(String token) {
		this.userToken = token;
	}

	public Customer getCustomer() {
		String username = getUsernameFromToken(userToken);
		return rCustomer.findByEmailAddressOrPhoneNumber(username, username).get();
	}

	public Wallet getWallet() {
		return getCustomer().getWallet();
	}
	
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateRefreshToken(claims, userDetails.getUsername());
	}

	public String generateOtherToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateOtherToken(claims, userDetails.getUsername());
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, username);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + properties.getJwtExpirationInMs()))
				.signWith(SignatureAlgorithm.HS256, properties.getJwtSecret()).compact();
	}

	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + properties.getRefreshExpirationInMs()))
				.signWith(SignatureAlgorithm.HS256, properties.getJwtSecret()).compact();
	}

	public String doGenerateOtherToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + properties.getJwtOtherTokenInMs()))
				.signWith(SignatureAlgorithm.HS256, properties.getJwtSecret()).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Boolean validateRefreshToken(String token, UserDetails userDetails) {
		Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token);
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Boolean validateOtherToken(String token, UserDetails userDetails) {
		Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token);
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Boolean tokenExpired(String token) {
		Boolean res = false;
		try {
			isTokenExpired(token);
		} catch (Exception e) {
			res = true;
		}
		return res;
	}
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
}
