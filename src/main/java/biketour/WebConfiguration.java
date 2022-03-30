package biketour;

import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Marcel Körner
 */
@EnableWebSecurity
public class WebConfiguration {

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		/**
		 * This method handles the Spring Security Configuration. It uses the {@link HttpSecurity} class for
		 * defining some basic security mechanisms according to login, register and several authentication
		 *
		 * @param httpSecurity object of the type {@link HttpSecurity}
		 * @throws Exception if exceptions happen
		 */
		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {

			httpSecurity
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/booking/**")
					.authenticated()
//					.antMatchers("/booking/*")
//					.hasRole("CUSTOMER")
					.antMatchers("/cart")
					.hasRole("CUSTOMER")
					.antMatchers("/tourCoach")
					.hasRole("TOURGUIDE")
					.antMatchers("/")
					.permitAll()
					.antMatchers("/login")
					.permitAll()
					.antMatchers("/management/resupplyStation/**")
					.hasAnyRole("TOURGUIDE", "BOSS")
					.antMatchers("/management/**")
					.hasRole("BOSS")
					.and()
					.formLogin()
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/")
					.and()
					.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/");
		}
	}

//	@Configuration
//	public class WebMvcConfiguration implements WebMvcConfigurer {
//
//		/**
//		 * This method creates a spring provided login controller for handling the login process.
//		 * @param viewControllerRegistry object of the type {@link ViewControllerRegistry}
//		 */
//
//		@Override
//		public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
//			viewControllerRegistry.addViewController("/login").setViewName("login");
//		}
//	}
}
