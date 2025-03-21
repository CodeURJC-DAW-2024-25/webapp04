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
                     // PUBLIC ENDPOINTS
					.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/users/signin").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/users/{userId}").permitAll()

					// PRIVATE ENDPOINTS
					.requestMatchers(HttpMethod.POST, "/api/products").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasAnyRole("ADMIN","USER")
					.requestMatchers(HttpMethod.GET, "/api/products/favorites").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/products/purchased").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/products/sold").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/products/{id}/images").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/products/{productId}/images/{imageId}").hasRole("USER")
					//.requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyRole("USER", "ADMIN") --> Don't add cause it requires full authentification				
					.requestMatchers(HttpMethod.DELETE, "/api/users/{userId}").hasAnyRole("ADMIN","USER")

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
