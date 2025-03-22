package es.grupo04.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.grupo04.backend.security.jwt.JwtRequestFilter;
import es.grupo04.backend.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Autowired
    public RepositoryUserDetailsService userDetailService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(2) // Careful with the orders, the api is a priority, more than this
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/").permitAll()
                    .requestMatchers("/product/**", "/products/**").permitAll()
					.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.requestMatchers("/signin").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/product/image/**").permitAll()
					.requestMatchers("/user/image/**").permitAll()
					.requestMatchers("/profile/**").permitAll()
					.requestMatchers("/v3/api-docs", "/v3/api-docs**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // to generate the OpenAPI documentation
					// PRIVATE PAGES
					.requestMatchers("/profile").hasAnyRole("USER","ADMIN")
					.requestMatchers("/adminProfile").hasAnyRole("ADMIN")
					.requestMatchers("/newProduct").hasAnyRole("USER")
					.requestMatchers("/editProfile").hasAnyRole("USER","ADMIN")
					.requestMatchers("/chat/**").hasAnyRole("USER")
					.requestMatchers("/chat").hasAnyRole("USER")
					.requestMatchers("/editProduct/**").hasAnyRole("USER")
					.requestMatchers("delete/**").hasAnyRole("USER","ADMIN")
					.requestMatchers("/deleteAccount/**").hasAnyRole("ADMIN","USER")
					.requestMatchers("/chat/sellScreen/**").hasAnyRole("USER")
					.requestMatchers("/chat/sell/**").hasRole("USER")
					.requestMatchers("/reports").hasAnyRole("ADMIN")
					.requestMatchers("/stats/get").hasAnyRole("USER")
					.requestMatchers("/review/*/delete").hasRole("ADMIN")
					.requestMatchers("/solve-report/**").hasRole("ADMIN")
					.requestMatchers("/add/image/**").hasAnyRole("USER")
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.failureUrl("/loginerror")
					.defaultSuccessUrl("/", true)
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

		return http.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
					//TODO check this urls
                    // PUBLIC ENDPOINTS
					.requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/products/{id}").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/v1/users/signin").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/users/{userId}").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/users/{userId}/reviews").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/reviews/{reviewId}").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/profile/image/{id}").permitAll()

					// PRIVATE ENDPOINTS
					.requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasAnyRole("ADMIN","USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/products/favorites").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/products/purchases").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/products/sales").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/v1/products/{id}/images").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/v1/products/{productId}/images/{imageId}").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyRole("USER", "ADMIN")	
					.requestMatchers(HttpMethod.DELETE, "/api/v1/profile/{userId}").hasAnyRole("ADMIN","USER")
					.requestMatchers(HttpMethod.PUT, "/api/v1/users/{userId}").hasAnyRole("ADMIN","USER")
					.requestMatchers(HttpMethod.POST, "/api/v1/users/{userId}/reviews").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/{reviewId}").hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET, "/api/v1/reports").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST, "/api/v1/reports/products/{productId}").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/v1/reports/{reportId}").hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET, "/api/v1/chats").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/chats/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/v1/chats/{productId}").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/v1/chats/{id}/send").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/v1/chats/{id}/sell").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/v1/profile/stats").hasRole("USER")

					.anyRequest().permitAll()
					);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
