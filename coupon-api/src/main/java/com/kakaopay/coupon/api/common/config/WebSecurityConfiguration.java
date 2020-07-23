package com.kakaopay.coupon.api.common.config;

import com.kakaopay.coupon.api.common.jwt.JwtAuthenticationFilter;
import com.kakaopay.coupon.api.common.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtProvider jwtProvider;

    protected void configure(HttpSecurity security) throws Exception {
        security.httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성하지 않기
                .and()
                    .authorizeRequests()
                        .antMatchers("/coupon/{code}/status/{status}")
                            .hasRole("USER")
                        .antMatchers("/auth/**", "/h2-console/**", "/health",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs")
                            .permitAll()
                        .anyRequest()
                            .hasRole("ADMIN")
                .and()
                    .headers()
                        .frameOptions().disable()   // X-Frame-Options in Spring Security 중지(h2-console 때문)
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
