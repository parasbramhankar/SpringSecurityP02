package com.example.SpringSecurityP02.config;

import com.example.SpringSecurityP02.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/", "/security/contactUs", "/security/aboutUs").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
================================================================================
 AUTHENTICATION MANAGER & PASSWORD ENCODER — COMPLETE EXPLANATION
================================================================================

1️⃣ WHAT IS AuthenticationManager?
---------------------------------
AuthenticationManager is the CORE interface in Spring Security that is responsible
for AUTHENTICATING a user.

➡ Authentication = "Who are you?"
➡ Authorization  = "What are you allowed to do?"

AuthenticationManager checks:
- Username exists or not
- Password is correct or not
- User is valid or not

Spring Security ALWAYS uses AuthenticationManager during login.

--------------------------------------------------------------------------------

2️⃣ WHY ARE WE DEFINING AuthenticationManager MANUALLY?
-------------------------------------------------------
By default, Spring Security auto-creates AuthenticationManager.
BUT when we use:
- CustomUserDetailsService
- Custom PasswordEncoder (BCrypt)

👉 We MUST tell Spring explicitly:
   - Which UserDetailsService to use
   - Which PasswordEncoder to use

That is why we define this @Bean manually.

--------------------------------------------------------------------------------

3️⃣ WHAT IS HttpSecurity?
------------------------
HttpSecurity is a configuration object provided by Spring Security.

It stores:
- Security filters
- Authentication configuration
- Authorization configuration

Think of HttpSecurity as:
"Security settings container for the current application"

--------------------------------------------------------------------------------

4️⃣ WHAT IS getSharedObject(AuthenticationManagerBuilder.class)?
----------------------------------------------------------------
AuthenticationManagerBuilder is a BUILDER class used to CREATE AuthenticationManager.

Spring internally stores it inside HttpSecurity.

getSharedObject(...) means:
➡ "Give me the already-created AuthenticationManagerBuilder object
    that Spring is using internally"

We DO NOT create it using new keyword.
Spring manages its lifecycle.

--------------------------------------------------------------------------------

5️⃣ WHAT IS AuthenticationManagerBuilder?
-----------------------------------------
AuthenticationManagerBuilder is used to configure:
- How users are loaded (UserDetailsService)
- How passwords are verified (PasswordEncoder)

It builds the final AuthenticationManager object.

--------------------------------------------------------------------------------

6️⃣ builder.userDetailsService(customUserDetailsService)
--------------------------------------------------------
This line tells Spring Security:

➡ "When a user tries to login,
   use THIS service to load user data"

customUserDetailsService implements UserDetailsService
and provides this method:

    loadUserByUsername(String username)

Spring will call this method automatically during login.

--------------------------------------------------------------------------------

7️⃣ builder.passwordEncoder(passwordEncoder())
------------------------------------------------
This line tells Spring Security:

➡ "Passwords are stored in ENCRYPTED form
   and should be matched using BCrypt algorithm"

Without PasswordEncoder:
❌ Login will always fail
❌ Plain text passwords are insecure

--------------------------------------------------------------------------------

8️⃣ return builder.build()
--------------------------
This line FINALIZES the configuration and CREATES AuthenticationManager.

After this:
✔ Spring knows how to authenticate users
✔ Spring knows where user data comes from
✔ Spring knows how to match passwords

This AuthenticationManager is used internally by:
- formLogin()
- httpBasic()
- JWT authentication (if added later)

--------------------------------------------------------------------------------

9️⃣ WHY IS @Bean USED HERE?
---------------------------
@Bean tells Spring:

➡ "Manage this object in Spring Container"

So Spring can:
- Inject AuthenticationManager where needed
- Use it internally during security checks

--------------------------------------------------------------------------------

10️⃣ PASSWORD ENCODER — BCryptPasswordEncoder
---------------------------------------------
BCryptPasswordEncoder is a STRONG hashing algorithm.

Features:
✔ One-way encryption (cannot be reversed)
✔ Uses random SALT
✔ Protects against rainbow table attacks

This is INDUSTRY STANDARD for password storage.

--------------------------------------------------------------------------------

11️⃣ WHY PASSWORD ENCODER IS A SEPARATE BEAN?
---------------------------------------------
Spring Security requires PasswordEncoder as a BEAN so that:
- Same encoder is used everywhere
- Consistent password matching
- Easy to switch algorithms later

--------------------------------------------------------------------------------

12️⃣ LOGIN FLOW USING THIS CONFIGURATION
----------------------------------------
1. User submits username + password
2. AuthenticationManager is triggered
3. CustomUserDetailsService loads user
4. BCryptPasswordEncoder compares passwords
5. If matched → Authentication SUCCESS
6. Else → Authentication FAILURE

--------------------------------------------------------------------------------

13️⃣ WHAT HAPPENS IF THIS CODE IS REMOVED?
------------------------------------------
❌ CustomUserDetailsService will not be used
❌ BCrypt password matching will fail
❌ Login will always fail
❌ Security configuration breaks

--------------------------------------------------------------------------------

14️⃣ REAL-WORLD PROJECT USAGE
-----------------------------
In production:
- UserDetailsService loads user from DB
- Passwords are stored in BCrypt form
- AuthenticationManager remains SAME

Only data source changes — logic stays same.

================================================================================
*/
}
