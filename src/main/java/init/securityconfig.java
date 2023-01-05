package init;

import userloginmanage.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.*;
import org.springframework.security.web.session.*;
import jwt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.web.servlet.config.annotation.*;
import Controller.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.*;

@EnableWebMvc
// @Import({ securityconfig2.class })
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "Controller")
@ComponentScan(basePackages = "userloginmanage")
public class securityconfig {

    @Autowired
    private jwtauthenticationentrypoint authenticationEntryPoint;

    @Bean
    public WebMvcConfigurer webMvcConfigurerInterceptors() {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {

                registry.addInterceptor(new Interceptor()).addPathPatterns("/verification", "/resetpasswordform",
                        "/resetpassword");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public Tokenmanager Tokenmanager() {
        return new Tokenmanager();
    }

    @Bean
    public jwtfilter jwtfilter() {
        return new jwtfilter();
    }

    @Bean
    public ManageUser manageuser() {
        return new ManageUser();
    }

    @Bean
    UserDetailImpl UserDetailImpl() {
        return new UserDetailImpl();
    }

    @Bean
    public jwtauthenticationentrypoint jwtauthenticationentrypoint() {
        return new jwtauthenticationentrypoint();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailImpl UserDetailImpl) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(UserDetailImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChainjwt(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().antMatchers(
                "//reverification", "//registerform", "//register", "//forgetpasswordform")
                .anonymous().and()
                .authorizeRequests()
                .antMatchers("//changepasswordform", "//changepassword", "//resetpasswordform", "//resetpassword")
                .hasAnyAuthority("student", "teacher")
                .and()
                .formLogin().and().authorizeRequests().antMatchers("//verification").authenticated().and()
                .authorizeRequests().antMatchers("//registerteacher").hasAnyAuthority("ADMIN").and().formLogin()
                .and().authorizeRequests()
                .antMatchers("//openclass", "//openclassform", "//deleteclass", "//teacherindex")
                .hasAnyAuthority("teacher").and().authorizeRequests()
                .antMatchers("//registerclassform", "//registerclass", "//deleteclasstudent", "//studentindex")
                .hasAnyAuthority("student").and()
                .formLogin();
        // .exceptionHandling().authenticationEntryPoint(ne
        // LoginUrlAuthenticationEntryPoint("/login"));// .and()
        // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(jwtfilter(),
                        UsernamePasswordAuthenticationFilter.class);
        // .addFilterBefore(new verifyfilter(), jwtfilter.class);
        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "UserValidator")
    public UserValidator validator() {
        return new UserValidator();
    }
}
