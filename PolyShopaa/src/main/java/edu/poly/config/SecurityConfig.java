package edu.poly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import edu.poly.domain.ShoppingCart;
import edu.poly.domain.ShoppingCartItem;
import edu.poly.domain.User;
import edu.poly.repository.ShoppingCartRepository;
import edu.poly.repository.UserRepository;
import edu.poly.service.ShoppingCartService;
import edu.poly.utils.CurrencyFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private DataSource dataSource;

        @Autowired
        UserRepository userRepository;

        @Autowired
        ShoppingCartRepository shoppingCartRepository;

        @Autowired
        private ShoppingCartService shoppingCartService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/shopaa/sites/home", "/shopaa/sites/product-detail/**",
                                                "/shopaa/sites/register")
                                .permitAll()
                                .requestMatchers("/shopaa/sites/cart/**").authenticated()
                                .requestMatchers("/shopaa/admin/**").hasRole("ADMIN")
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/shopaa/sites/login")
                                                .successHandler(customAuthenticationSuccessHandler())
                                                .failureHandler(customAuthenticationFailureHandler()) // Điều hướng khi
                                                                                                      // đăng
                                                // nhập thất bại
                                                .permitAll())
                                .rememberMe(rememberMe -> rememberMe
                                                .tokenRepository(persistentTokenRepository())
                                                .tokenValiditySeconds(7 * 24 * 60 * 60)) // 1 tuần
                                .logout(logout -> logout.permitAll())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                return tokenRepository;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
                return new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
                                // Lấy tên người dùng từ đối tượng Authentication
                                String username = authentication.getName();

                                // Tìm người dùng từ cơ sở dữ liệu
                                User authenticatedUser = userRepository.findByUsername(username);
                                request.getSession().setAttribute("loggedInUser", authenticatedUser);

                                // Lấy giỏ hàng tương ứng
                                ShoppingCart shoppingCart = shoppingCartRepository.findByUser(authenticatedUser);
                                request.getSession().setAttribute("shoppingCart", shoppingCart);

                                // Lấy tổng số lượng sản phẩm trong giỏ
                                getTotalCartItem(shoppingCart, request.getSession());

                                // Kiểm tra nếu người dùng là Admin
                                if (authentication.getAuthorities()
                                                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                                        response.sendRedirect("/shopaa/admin/home");
                                } else {
                                        response.sendRedirect("/shopaa/sites/home");
                                }

                        }
                };
        }

        @Bean
        public AuthenticationFailureHandler customAuthenticationFailureHandler() {
                return (request, response, exception) -> {
                        request.getSession().setAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác!");
                        response.sendRedirect("/shopaa/sites/login?error");
                };
        }

        public void getTotalCartItem(ShoppingCart shoppingCart, HttpSession session) {
                int totalQuantity = 0;
                List<ShoppingCartItem> shoppingCartItems = shoppingCartService
                                .getShoppingCartItems(shoppingCart);
                if (!shoppingCartItems.isEmpty()) {
                        totalQuantity = shoppingCartItems.size();
                }
                session.setAttribute("totalCartItem", totalQuantity);
        }

        @Bean
        public CurrencyFormatter currencyFormatter() {
                return new CurrencyFormatter();
        }
}
