package berry.tetra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class TetraAuthConfiguration {
  // 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後に / にリダイレクト
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(AntPathRequestMatcher.antMatcher("/admin"))
            .authenticated() // /admin以下は認証済みであること
            .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
            .permitAll())// 上記以外は全員アクセス可能
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));
    return http.build();
  }

  // 認証処理に関する設定（誰がどのようなロールでログインできるか）
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    UserDetails admin = User.withUsername("admin")
        .password("{bcrypt}$2y$05$0yY6OCJ67KYm7ScWMVYKJeyGQ/9Swc5eKaiq7foC6uxZ7bVnkJiRi").roles("ADMIN").build();

    // 生成したユーザをImMemoryUserDetailsManagerに渡す
    return new InMemoryUserDetailsManager(admin);
  }
}
