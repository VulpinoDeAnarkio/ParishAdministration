package pl.kea.parafia.konfiguracja;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.kea.parafia.bezpieczenstwo.KontoUzytkownika;
import pl.kea.parafia.bezpieczenstwo.UzytkownikRepository;

@Configuration
@EnableMethodSecurity
public class KonfiguracjaBezpieczenstwa {

    @Bean
    SecurityFilterChain filtr(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/ogloszenia-publiczne", "/css/**", "/js/**", "/favicon.svg", "/error").permitAll()
                        .requestMatchers("/finanse/**", "/raporty/finanse", "/raporty/finanse/**").hasAnyRole("ADMIN", "PROBOSZCZ", "SKARBNIK")
                        .requestMatchers("/duszpasterstwo/**").hasAnyRole("ADMIN", "PROBOSZCZ", "DUSZPASTERZ")
                        .requestMatchers("/uzytkownicy/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?blad")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/wyloguj")
                        .logoutSuccessUrl("/login?wylogowano")
                        .permitAll())
                .exceptionHandling(obsluga -> obsluga.accessDeniedPage("/brak-dostepu"));
        return http.build();
    }

    @Bean
    UserDetailsService uzytkownicy(UzytkownikRepository repozytorium) {
        return email -> repozytorium.findByEmailIgnoreCase(email)
                .map(KontoUzytkownika::new)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono konta"));
    }

    @Bean
    PasswordEncoder koderHasel() {
        return new BCryptPasswordEncoder();
    }
}
