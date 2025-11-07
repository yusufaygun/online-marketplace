package com.example.online_marketplace.security;

import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import com.example.online_marketplace.model.Role;
import com.example.online_marketplace.service.RoleService;
import com.example.online_marketplace.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.boot.CommandLineRunner;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/auth/login",
            "/auth/register",
            "/products",
            "/products/**",
            "/sellers",
            "/sellers/**"
    };

    private static final String[] AUTH_ADMIN_LIST = {
            "/admin",
            "/admin/**"
    };


    private final AuthUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(AuthUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(AUTH_WHITELIST).anonymous() // Allow anonymous access for whitelist URLs
                        .requestMatchers(AUTH_ADMIN_LIST).hasAnyAuthority("admin") // Only admins can access admin section
                        .anyRequest().hasAnyAuthority("admin", "user") // All other requests require "ADMIN" or "USER" role
                )
                .authenticationProvider(authenticationProvider(passwordEncoder())) // Use the DaoAuthenticationProvider
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter
                .exceptionHandling(customizer -> customizer
                        .accessDeniedHandler((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN)) // When access is denied
                        .authenticationEntryPoint((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED)) // When unauthorized access
                )
                .formLogin(customizer -> customizer
                        .loginProcessingUrl("/login")
                        .successHandler((req, resp, auth) -> resp.setStatus(HttpServletResponse.SC_OK)) // When login is successful
                        .failureHandler((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN)) // When login fails
                )
                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) // When logout is successful
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .httpBasic(Customizer.withDefaults()); // Use basic HTTP authentication

        return http.build();
    }

    @Bean
    public CommandLineRunner setupDefaultUser(UserService userService, RoleService roleService) {
        return args -> {
            // Create admin and user roles
            if (roleService.findRoleByName("ADMIN") == null) {
                roleService.saveRole(new Role("ADMIN"));
            }
            if (roleService.findRoleByName("USER") == null) {
                roleService.saveRole(new Role("USER"));
            }

            // Create an admin if it doesn't exist
            /*if (userService.findByUsername("admin") == null) {
            }*/

            UserInputDto admin = new UserInputDto();
            admin.setUsername("admin");
            admin.setPassword("password123"); // Ensure the password is strong and hashed
            admin.setName("Admin");
            admin.setSurname("User");
            UserDto savedAdmin = userService.saveUser(admin);
            userService.assignRoleToUser(savedAdmin.getUsername(), "ADMIN");

            // Create a user if it doesn't exist
            /*if (userService.findByUsername("user") == null) {
            }*/
            UserInputDto user = new UserInputDto();
            user.setUsername("user");
            user.setPassword("password123"); // Ensure the password is strong and hashed
            user.setName("Regular");
            user.setSurname("User");
            UserDto savedUser = userService.saveUser(user);
            userService.assignRoleToUser(savedUser.getUsername(), "USER");

            /*// Create sellers
            Seller seller1 = new Seller();
            seller1.setName("adidas");
            sellerService.saveSeller(seller1);

            Seller seller2 = new Seller();
            seller1.setName("modesan");
            sellerService.saveSeller(seller2);

            // Add products to sellers
            Product product1 = new Product();
            product1.setName("sneaker");
            product1.setDescription("an adidas sneaker");
            product1.setPrice(89.90);
            sellerService.addProductToSeller(seller1.getId(), product1);

            Product product2 = new Product();
            product2.setName("yaprak");
            product2.setDescription("tokat sarma yapragi");
            product2.setPrice(31.31);
            sellerService.addProductToSeller(seller1.getId(), product2);

            Product product3 = new Product();
            product3.setName("ahmet");
            product3.setDescription("tmehmetgi");
            product3.setPrice(23.12);
            sellerService.addProductToSeller(seller2.getId(), product3);

            Product product4 = new Product();
            product4.setName("abc");
            product4.setDescription("adcadnc adcadnc adcadnc");
            product4.setPrice(29.90);
            sellerService.addProductToSeller(seller2.getId(), product4);*/

        };
    }

}
