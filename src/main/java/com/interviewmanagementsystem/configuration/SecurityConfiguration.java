package com.interviewmanagementsystem.configuration;

import com.interviewmanagementsystem.services.auth.token.ITokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

	private final ITokenService tokenService;

	public SecurityConfiguration(ITokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		// Allow 4200 and 3000 ports for development
		config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
		config.addAllowedHeader("*"); // X-Requested-With, Content-Type, Authorization, Origin, Accept
		config.addAllowedMethod("*"); // GET, POST, PUT, DELETE, PATCH, OPTIONS
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:5173");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		  .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(new JWTFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers("/forgotPassword", "/forgotPassword/**").permitAll()
				  .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
				  .requestMatchers("/api/employees/**").permitAll()
				  .requestMatchers("/api/candidates/**").authenticated()
				  .requestMatchers("/api/offers/**").permitAll()
				  .requestMatchers("/api/interviews/**").authenticated()
				  .requestMatchers("/api/jobs/**").permitAll()
					.requestMatchers("/api/roles/**").permitAll()
					.requestMatchers("/api/departments/**").permitAll()
					.requestMatchers("/api/skills/**").permitAll()
					.requestMatchers("/api/benefits/**").permitAll()
					.requestMatchers("/api/imports/**").permitAll()
					.requestMatchers("/api/forgotPassword/**").permitAll()
				  .anyRequest().authenticated()
			  );
		return http.build();
	}

}
