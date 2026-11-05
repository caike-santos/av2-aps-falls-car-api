package falls_car_api.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //rotas publicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()

                        //rotas funcionario
                        .requestMatchers("/api/relatorios/**").hasRole("FUNCIONARIO")
                        .requestMatchers("/api/motoristas/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PATCH, "/api/locacoes/*/finalizar").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/api/lojas").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/lojas/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/api/automoveis").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PATCH, "/api/automoveis/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/api/reservas").hasRole("FUNCIONARIO")

                        //rotas cliente e funcionario
                        .requestMatchers(HttpMethod.GET, "/api/lojas/**").hasAnyRole("CLIENTE", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/api/automoveis/**").hasAnyRole("CLIENTE", "FUNCIONARIO")
                        .requestMatchers("/api/reservas/**").hasAnyRole("CLIENTE", "FUNCIONARIO")
                        .requestMatchers("/api/locacoes/**").hasAnyRole("CLIENTE", "FUNCIONARIO")
                        .requestMatchers("/api/clientes/**").hasAnyRole("CLIENTE", "FUNCIONARIO")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}