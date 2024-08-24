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
                        .requestMatchers(AUTH_WHITELIST).anonymous() // Whitelist URL'ler için anonim erişime izin ver
                        .requestMatchers(AUTH_ADMIN_LIST).hasAnyAuthority("admin") // admin kısmına sadece adminler erişebilsin
                        .anyRequest().hasAnyAuthority("admin", "user") // Diğer tüm istekler için "ADMIN" veya "USER" rolü gerekli
                )
                .authenticationProvider(authenticationProvider(passwordEncoder())) // Use the DaoAuthenticationProvider
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter
                .exceptionHandling(customizer -> customizer
                        .accessDeniedHandler((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN)) // Erişim reddedildiğinde
                        .authenticationEntryPoint((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED)) // Yetkisiz erişimde
                )
                .formLogin(customizer -> customizer
                        .loginProcessingUrl("/login")
                        .successHandler((req, resp, auth) -> resp.setStatus(HttpServletResponse.SC_OK)) // Giriş başarılı olursa
                        .failureHandler((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN)) // Giriş başarısız olursa
                )
                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) // Çıkış başarılı olduğunda
                )
                .csrf(AbstractHttpConfigurer::disable) // CSRF'yi devre dışı bırak
                .httpBasic(Customizer.withDefaults()); // Temel HTTP kimlik doğrulaması kullan

        return http.build();
    }

    @Bean
    public CommandLineRunner setupDefaultUser(UserService userService, RoleService roleService) {
        return args -> {
            // Admin rolünü ve kullanıcıyı oluştur
            if (roleService.findRoleByName("ADMIN") == null) {
                roleService.saveRole(new Role("ADMIN"));
            }
            if (roleService.findRoleByName("USER") == null) {
                roleService.saveRole(new Role("USER"));
            }

            // Eğer admin yoksa, bir admin oluştur
            /*if (userService.findByUsername("admin") == null) {
            }*/

            UserInputDto admin = new UserInputDto();
            admin.setUsername("admin");
            admin.setPassword("password123"); // Parolanın güçlü ve hash'lenmiş olduğundan emin olun
            admin.setName("Admin");
            admin.setSurname("User");
            UserDto savedAdmin = userService.saveUser(admin);
            userService.assignRoleToUser(savedAdmin.getUsername(), "ADMIN");

            // Eğer user yoksa, bir user oluştur
            /*if (userService.findByUsername("user") == null) {
            }*/
            UserInputDto user = new UserInputDto();
            user.setUsername("user");
            user.setPassword("password123"); // Parolanın güçlü ve hash'lenmiş olduğundan emin olun
            user.setName("Regular");
            user.setSurname("User");
            UserDto savedUser = userService.saveUser(user);
            userService.assignRoleToUser(savedUser.getUsername(), "USER");

            /*// Satıcıları oluştur
            Seller seller1 = new Seller();
            seller1.setName("adidas");
            sellerService.saveSeller(seller1);

            Seller seller2 = new Seller();
            seller1.setName("modesan");
            sellerService.saveSeller(seller2);

            // satıcılara ürün ekle
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
