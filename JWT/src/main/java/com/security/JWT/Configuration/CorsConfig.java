/*
 * package com.security.JWT.Configuration;
 * 
 * import java.util.List;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.web.cors.CorsConfiguration; import
 * org.springframework.web.cors.CorsConfigurationSource; import
 * org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 * 
 * @Configuration public class CorsConfig {
 * 
 * @Bean public CorsConfigurationSource corsConfigurationSource() {
 * 
 * CorsConfiguration configuration = new CorsConfiguration();
 * 
 * configuration.setAllowedOrigins(List.of("*"));
 * configuration.setAllowedMethods( List.of("GET", "POST", "PUT", "DELETE",
 * "OPTIONS") ); configuration.setAllowedHeaders(List.of("*"));
 * 
 * // ⭐ THIS IS THE KEY FIX ⭐ configuration.setExposedHeaders(
 * List.of("jwt-token", "Authorization") );
 * 
 * UrlBasedCorsConfigurationSource source = new
 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
 * configuration);
 * 
 * return source; } }
 */