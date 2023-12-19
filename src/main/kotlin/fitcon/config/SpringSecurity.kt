package fitcon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SpringSecurity(
    private val userDetailsService: UserDetailsService
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .csrf{ csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/register/**").permitAll()
                    .requestMatchers("/home", "/about", "/schedule", "/faq", "/contact").permitAll()
                    .anyRequest().authenticated()
            }.formLogin { form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/user")
                    .permitAll()
            }.logout { logout ->
                logout
                    .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                    .permitAll()
            }
        return http.build()
    }

    @Throws(Exception::class)
    @Override
    fun configure(auth: AuthenticationManagerBuilder){
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }
}