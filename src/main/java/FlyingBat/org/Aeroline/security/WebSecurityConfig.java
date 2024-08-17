package FlyingBat.org.Aeroline.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        // Consulta para obtener el usuario por email
        users.setUsersByUsernameQuery("select email as username, password, true from usuarios where email = ?");

        // Consulta para obtener los roles asociados al usuario
        users.setAuthoritiesByUsernameQuery("select u.email, r.nombre from usuarios u " +
                "inner join roles r on r.id = u.rol_id " +
                "where u.email = ?");

        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorize -> authorize
                // aperturar el acceso a los recursos estáticos
                .requestMatchers("/assets/**", "/css/**", "/js/**").permitAll()
                // las vistas públicas no requieren autenticación
                .requestMatchers("/", "/privacy", "/terms").permitAll()
                // todas las demás vistas requieren autenticación
                .anyRequest().authenticated());
        http.formLogin(form -> form.permitAll());

        return http.build();
    }
}
