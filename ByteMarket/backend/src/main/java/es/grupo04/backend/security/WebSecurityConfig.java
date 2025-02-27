package es.grupo04.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
    public RepositoryUserDetailsService userDetailService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
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
					// PRIVATE PAGES
					.requestMatchers("/profile").hasAnyRole("USER")
					.requestMatchers("/newProduct").hasAnyRole("USER")
					.requestMatchers("/editProfile").hasAnyRole("USER")
					.requestMatchers("/editProduct/**").hasAnyRole("USER")
					.requestMatchers("delete/**").hasAnyRole("USER","ADMIN")
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

}
