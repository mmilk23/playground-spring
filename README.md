# Playground-Spring 🧪

[![Build Status](https://github.com/mmilk23/playground-spring/actions/workflows/maven.yml/badge.svg)](https://github.com/mmilk23/playground-spring/actions)
[![codecov](https://codecov.io/gh/mmilk23/playground-spring/branch/main/graph/badge.svg)](https://codecov.io/gh/mmilk23/playground-spring)
[![Coverage Status](https://coveralls.io/repos/github/mmilk23/playground-spring/badge.svg)](https://coveralls.io/github/mmilk23/playground-spring)
![Dependabot](https://img.shields.io/badge/Dependabot-enabled-brightgreen)
[![Known Vulnerabilities](https://snyk.io/test/github/mmilk23/playground-spring/badge.svg)](https://snyk.io/test/github/mmilk23/playground-spring)
[![Last Updated](https://img.shields.io/github/commit-activity/w/mmilk23/playground-spring)](https://github.com/mmilk23/playground-spring/commits/main)


Playground-Spring is a small study/playground application built with **Spring Boot**, **Spring Data JPA**, **Spring Security**, and **Vaadin** (UI).

> **Notice:** This project was created as an architecture playground. Do not use it in production without a proper review (security, dependencies, configuration, logging/monitoring, etc.).

---

## ✨ Tech Stack

- Java **21**
- Spring Boot **4.0.1**
- Vaadin **25.x** (Theme: **Aura**)
- Spring Security (form login)
- JPA + Apache Derby (embedded DB)

---

## ✅ Prerequisites

- **Java 21** (LTS recommended)
- **Maven 3.9.x** (or Maven Wrapper `./mvnw` if you add it to the project)

> Tip (Windows): verify your environment with  
> `mvn -v` and `java -version`.

---

## ▶️ Running in Development

1. Go to the module directory:

   ```bash
   cd playground-spring
   ```

2. Start the app:

   ```bash
   mvn clean spring-boot:run
   ```

3. Open your browser:

   - http://localhost:8080/

4. Example credentials:

   - `admin / admin`

---

## 🚀 Running in Production

Production mode builds the optimized Vaadin frontend bundle and packages the application.

### 1) Build the artifact

From the module directory:

```bash
mvn -Pproduction clean package
```

This produces a runnable JAR at:

```text
target/playground-spring-0.0.2-SNAPSHOT.jar
```

### 2) Run the JAR

```bash
java -jar target/playground-spring-0.0.2-SNAPSHOT.jar
```

Default URL:

- http://localhost:8080/

### 3) Useful runtime options (optional)

#### Custom port

```bash
java -jar target/playground-spring-0.0.2-SNAPSHOT.jar --server.port=8081
```

#### Spring profile

```bash
java -jar target/playground-spring-0.0.2-SNAPSHOT.jar --spring.profiles.active=prod
```

> If you want environment-specific settings, create `application-prod.properties` / `application-prod.yml`.

---

## 🔐 Security & Login

- Login view route: `/login`
- The Vaadin `LoginForm` posts to `login` (`login.setAction("login")`) and integrates with Spring Security form login.
- When using **Aura** via `@StyleSheet(Aura.STYLESHEET)`, make sure static theme resources are permitted in your `SecurityFilterChain` (e.g., `/aura/**`).

---

## 🖼️ Screenshots

Screenshots are located under `doc/screenshots`:

### Playground Login
![Playground Login](doc/screenshots/playground_login.png?raw=true&width=420)

### Playground Welcome
![Playground Welcome](doc/screenshots/playground_welcome.png?raw=true&width=420)

### Playground Edit
![Playground Edit](doc/screenshots/playground_edit.png?raw=true&width=420)

### Playground List
![Playground List](doc/screenshots/playground_list.png?raw=true&width=420)

---

## 📄 License

This project is licensed under the terms of the **MIT license**. See the `LICENSE` file for details.

---

## ⭐️ Final Note

If you found this project helpful, please consider giving it a star ⭐️
