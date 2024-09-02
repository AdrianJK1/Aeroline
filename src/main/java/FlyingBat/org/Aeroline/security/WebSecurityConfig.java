package FlyingBat.org.Aeroline.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        users.setUsersByUsernameQuery("select email as username, password, true from usuarios where email = ?");
        users.setAuthoritiesByUsernameQuery("select u.email, r.nombre from usuarios u " +
                "inner join roles r on r.id = u.rol_id " +
                "where u.email = ?");

        return users;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        // Permitir acceso a recursos estáticos
                        .requestMatchers("/assets/**", "/css/**", "/js/**").permitAll()
                        // Permitir acceso a ciertas URLs

                        .requestMatchers("/", "/privacy", "/registro").permitAll()
                        // Permitir acceso a la ruta de descarga del PDF
                        .requestMatchers("/downloadPdf/**").permitAll()

                        .requestMatchers("/reserva/**").hasAuthority("usuario")

                        .requestMatchers("/aerolinea/**", "/vuelo/**").hasAuthority("aerolinea")
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.permitAll()
                );

        return http.build();
    }

}
