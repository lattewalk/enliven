package com.enliven.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Boot attaches a special meaning to a WebSecurityConfigurer on the class
 * that carries the @EnableOAuth2Sso annotation: it uses it to configure
 * the security filter chain that carries the OAuth2 authentication processor.
 *
 * Login procedure
 * GET :http://localhost:8080/login/facebook
 * appServer redirect https://www.facebook.com/dialog/oauth?client_id=246083375774471&redirect_uri=http://localhost:8080/login/facebook&response_type=code&state=Aej9Ec
 * facebook redirect https://www.facebook.com/login.php?skip_api_login=1&api_key=246083375774471&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fv2.6%2Fdialog%2Foauth%3Fredirect_uri%3Dhttp%253A%252F%252Flocalhost%253A8080%252Flogin%252Ffacebook%26state%3DAej9Ec%26response_type%3Dcode%26client_id%3D246083375774471%26ret%3Dlogin%26logger_id%3Ded47037d-18c7-4a9f-8c23-fe3114d8de7f&cancel_url=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Ffacebook%3Ferror%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied%26state%3DAej9Ec%23_%3D_&display=page&locale=en_US&logger_id=ed47037d-18c7-4a9f-8c23-fe3114d8de7f
 * facebook redirect https://www.facebook.com/v2.6/dialog/oauth?redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Ffacebook&state=Aej9Ec&response_type=code&client_id=246083375774471&ret=login&logger_id=ed47037d-18c7-4a9f-8c23-fe3114d8de7f&ext=1467161649&hash=AeZ7N7le8jU54P4S
 * facebook redirect http://localhost:8080/login/facebook?code=AQBwGX3wWRCvtqIgJo74cLmga9LB0zpquxGseTPQhgLb3BjNMNdMvj4Qhy7IbeFb1FqFNoPu0EuKNFjedYAjV1qy8E3mQgYQScQp7SGN76TyvpcLMfONdaRezVUoyzMhcSUBj5WBtUH7Hg30oMQsoZfzJpg2GwG5OFM-zfOh40scX48cQTZLYxp466mKD_Ijg4UTPyx1nqxCN5ElXv6UJJSLN6ldh2A-_xDycTG8GUVwX_4xS93oGcJrBOEjF-jSuMQWNAAdGRNRZj3j_nHBWAEJBIyN2G3NfzZEXAnkYiuZPzAE8mE-r4XOR6fJHratmdAoznobP_4_LJoPTmRKQ24k&state=Aej9Ec#_=_
 * appServer oauth2client retrieving token use rest template, POST https://graph.facebook.com/oauth/access_token
 *   {grant_type=[authorization_code], code=[AQDpg0XP5erxzUru3pb7dDdcvqMEwjIZlaO8V2d0EAtMSNZmhIG1fLsLzXTg8hi3PCCd1rfGpTmNNJkf9d6ByQu9gw9y7_KCBWPAh8rXEbhnpcaUEtR2LPUpkpIoLIv8EB4O9jvBkf1db4PTqVfDexFIe-oavgaG_4xWA96vzgQQM1NCxDQ59MX88I9tsqUc1LKFdVCN6QFOoB03IsvN1725VY0dtAEjL2pxaGQ-cJA7o6aH-Hz5JVzqTeo1Auw_9xMkUvOoT5igfx-aiU-ZpBuK7jBTAi9F1cJpv_Upk7KxJh9HI11PowWvFtQ3SIC1czc_pGUXHXBJ0aFkf3NltVa2], redirect_uri=[http://localhost:8080/login/facebook], client_id=[246083375774471], client_secret=[a42ee306d220a07e4ecbb211c6e940de]}
 *   return access token
 * appServer set cookie with access token as JSESSIONID=E35D544F7B0C4A69439A633DA6772B35
 * appServer redirect load launch web page
 *
 * @author lattewalk
 */

@Configuration
@EnableOAuth2Client
@EnableWebSecurity  // Enable web security configuration
@EnableAuthorizationServer //Enable bunch of endpoints and setup security  OAuth2 as Spring MVC handlers
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2ClientConfig extends WebSecurityConfigurerAdapter {
    private static final String FACEBOOK_LOGIN = "/login/facebook";
    private static final String GITHUB_LOGIN = "/login/github";

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    /**
     * Configure web security to make login page and resources visible to authorizeRequest
     * @param security
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // @formatter:off
        security
            //all requests are protected by default
            .antMatcher("/**").authorizeRequests()
            //home page and login endpoints are explicitly excluded
            .antMatchers("/", "/login**", "webjars/**").permitAll()
            //all other endpoints require an authenticated user
            .anyRequest().authenticated()
            //unauthorized one redirect to login home page
            .and().exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
            .and().logout().logoutSuccessUrl("/").permitAll()
            .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class).addFilterBefore(compositeSsoFilter(), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    /**
     * Define defence token repository for cross-site-request-forgery
     * @return
     */
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
        repo.setHeaderName("X-XSRF-TOKEN");
        return repo;
    }

    /**
     * Handle and filter the csrf header, set csrf token from AngularJS CSRF protection
     * @return
     */
    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    // set csrf token into response cookie
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    /**
     * Filters to do oauth/socialmedia authentication
     * @return
     */
    private Filter compositeSsoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList();

        filters.add(ssoFilter(facebookResource(), FACEBOOK_LOGIN));
        filters.add(ssoFilter(githubResource(), GITHUB_LOGIN));
        filter.setFilters(filters);

        return filter;
    }

    private Filter ssoFilter(ClientResources clientRes, String path) {
        //Mapping given URL for OAuth2ClientProcess
        OAuth2ClientAuthenticationProcessingFilter oauth2ClientFilter =
            new OAuth2ClientAuthenticationProcessingFilter(path);

        //Set OAuth2 resource info to oauth2client context to filter
        OAuth2RestTemplate oauth2RestTemplate  = new OAuth2RestTemplate(clientRes.getClient(), oauth2ClientContext);
        oauth2ClientFilter.setRestTemplate(oauth2RestTemplate);

        //Set token service info (callback and clientId) as filter's redirect behavior
        oauth2ClientFilter.setTokenServices(new UserInfoTokenServices(
            clientRes.getResource().getUserInfoUri(),
            clientRes.getClient().getClientId()));

        return oauth2ClientFilter;
    }

    /**
     * Registrate filter to handle the OAuth2 client redirection signaled by exceptions
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100); //set low order for coming before main spring security filter
        return registration;
    }

    class ClientResources {
        @NestedConfigurationProperty
        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

        @NestedConfigurationProperty
        private ResourceServerProperties resource = new ResourceServerProperties();

        public AuthorizationCodeResourceDetails getClient() {
            return client;
        }

        public ResourceServerProperties getResource() {
            return resource;
        }
    }

    /**
     * Get facebook api client registration from configuration,
     * read from application.yml, with starting path of "facebook"
     * @return
     */
    @Bean
    @ConfigurationProperties("facebook")
    ClientResources facebookResource() {
        return new ClientResources();
    }

    /**
     * Get facebook api client registration from configuration,
     * read from application.yml, with starting path of "github"
     * @return
     */
    @Bean
    @ConfigurationProperties("github")
    ClientResources githubResource() {
        return new ClientResources();
    }
}
