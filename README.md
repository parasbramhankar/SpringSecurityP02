# In-Memory User data : using the CustomUserDetailsService class in service


Below is a **FULL OVERVIEW**, explained **step by step**, in **simple language**, covering:

1️⃣ What each class does
2️⃣ How user data is loaded
3️⃣ How authentication happens
4️⃣ How authorization with roles works
5️⃣ How this would look in a **real project**
6️⃣ What to improve for production use

---

## 🧩 1. BIG PICTURE — WHAT THIS PROJECT IS DOING

Your application uses:

* **Spring Security**
* **Form login + HTTP Basic**
* **CustomUserDetailsService**
* **BCrypt password encryption**
* **Role-based authorization using `@PreAuthorize`**

📌 **Meaning**:
Users must **log in**, and **only users with correct roles** can access specific endpoints.

---

# 🔐 2. SECURITY CONFIG CLASS (SecurityConfig)

This class is the **brain of Spring Security** in your project.

---

## 2.1 `@Configuration`

Tells Spring:

> “This class defines beans and configuration”

---

## 2.2 `@EnableWebSecurity`

Enables:

* Security filters
* Authentication system
* Authorization checks

Without this → **no security**

---

## 2.3 `@EnableMethodSecurity`

Enables:

* `@PreAuthorize`
* `@PostAuthorize`
* Method-level security

Without this → `@PreAuthorize` **will not work**

---

## 2.4 `SecurityFilterChain`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
```

This defines **URL-level security rules**.

### What happens here?

```java
.requestMatchers("/", "/security/contactUs", "/security/aboutUs").permitAll()
```

✔ Anyone can access these URLs
✔ No login required

```java
.anyRequest().authenticated()
```

✔ All other URLs require login

```java
.formLogin(Customizer.withDefaults())
```

✔ Default Spring login page

```java
.httpBasic(Customizer.withDefaults())
```

✔ Allows API testing via Postman

---

## 2.5 AuthenticationManager

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http)
```

This tells Spring **HOW to authenticate users**.

### Internally:

* Uses `CustomUserDetailsService`
* Uses `BCryptPasswordEncoder`

Spring will now:

1. Ask `CustomUserDetailsService` for user
2. Compare encrypted passwords
3. Decide success/failure

---

## 2.6 PasswordEncoder

```java
@Bean
public PasswordEncoder passwordEncoder()
```

✔ Encrypts passwords
✔ Protects stored credentials
✔ Mandatory in real projects

---

# 👤 3. USER DATA — CustomUserDetailsService

This class answers **one question**:

> “Given a username, who is this user?”

---

## 3.1 `loadUserByUsername`

```java
public UserDetails loadUserByUsername(String username)
```

Spring calls this method automatically during login.

---

## 3.2 How user data is loaded (your logic)

You currently simulate a database using **if-conditions**:

### Admin user

```java
if(username.equals("paras")){
    return User.withUsername("paras")
            .password(encodedPass1)
            .roles("ADMIN")
            .build();
}
```

### Normal user

```java
if(username.equals("prachi")){
    return User.withUsername("prachi")
            .password(encodedPass2)
            .roles("USER")
            .build();
}
```

### Invalid user

```java
throw new UsernameNotFoundException("User not found");
```

📌 **This behaves exactly like a database**, but hard-coded.

---

# 🔑 4. AUTHENTICATION FLOW (VERY IMPORTANT)

Here is the **exact runtime flow**:

```
User opens /security/transfer
        ↓
Spring Security FilterChain
        ↓
Checks if user is logged in
        ↓
If NOT logged in → redirect to login page
        ↓
User enters username & password
        ↓
AuthenticationManager
        ↓
CustomUserDetailsService.loadUserByUsername()
        ↓
BCryptPasswordEncoder matches password
        ↓
User authenticated
```

---

# 🛂 5. AUTHORIZATION FLOW (ROLE CHECK)

After authentication:

```
Controller method reached
        ↓
@PreAuthorize evaluated
        ↓
Role matches?
        ↓
YES → method executes
NO  → 403 Forbidden
```

### Example:

```java
@PreAuthorize("hasRole('ADMIN')")
```

✔ paras → allowed
❌ prachi → denied

---

# 🎯 6. CONTROLLER — ROLE-BASED ACCESS

Your controller uses **method-level security**, which is best practice.

### Example:

```java
@PreAuthorize("hasAnyRole('USER','ADMIN')")
```

✔ USER can access
✔ ADMIN can access

This is **cleaner and safer** than URL-based rules.

---

# 🏗️ 7. HOW THIS LOOKS IN A REAL PROJECT

### ❌ What you should NOT do in production

* Hard-code users in `CustomUserDetailsService`
* Create new `BCryptPasswordEncoder()` inside service
* Store plain passwords

---

## ✅ REAL-WORLD VERSION (DATABASE)

### 7.1 Database table

| id | username | password (BCrypt) | role  |
| -- | -------- | ----------------- | ----- |
| 1  | paras    | $2a$10$…          | ADMIN |
| 2  | prachi   | $2a$10$…          | USER  |

---

### 7.2 Entity

```java
@Entity
public class AppUser {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
}
```

---

### 7.3 Repository

```java
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
```

---

### 7.4 Real CustomUserDetailsService

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        AppUser user = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
```

---

# 🔒 8. SECURITY BEST PRACTICES

✅ Use BCrypt
✅ Store passwords encrypted
✅ Use method-level security
✅ Separate config, service, controller
❌ Never expose roles to clients
❌ Never store passwords in plain text

---

# 🏁 FINAL SUMMARY (ONE PARAGRAPH)

Your application uses Spring Security with a custom authentication flow where user credentials are loaded by `CustomUserDetailsService`, verified using BCrypt encryption, and authorized using role-based annotations (`@PreAuthorize`). While the current implementation simulates user storage using hard-coded logic, the same architecture directly maps to a production-ready database-driven system with minimal changes.


