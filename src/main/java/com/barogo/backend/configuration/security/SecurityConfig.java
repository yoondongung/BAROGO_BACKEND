package com.barogo.backend.configuration.security;

import com.barogo.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정 활성화
@Configuration
public class SecurityConfig {

    // 스웨거 권한 없이 접근 가능하도록 설정
    private static final String[] PERMIT_SWAGGER3 = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger"};
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/barogo/auth/**").permitAll() // 로그인 관련된 정보
                .antMatchers(PERMIT_SWAGGER3).permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userService),
                        UsernamePasswordAuthenticationFilter.class);
        /**
         * addFilterBefore() : 필터를 등록한다. 스프링 시큐리티 필터링에 등록해주어야 하기 때문에, 여기에 등록해주어야 한다.
         * 파라미터는 2가지가 들어간다.
         * 왼쪽은 커스텀한 필터링이 들어간다. 오른쪽에 등록한 필터전에 커스텀필터링이 수행된다.
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //  세션을 사용하지 않는다고 설정한다.

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 일반 텍스트 암호를 사용 
        //  추후 패스워드 암호화 알고리즘 적용시 변경
        return NoOpPasswordEncoder.getInstance();
    }
}
