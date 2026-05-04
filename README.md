# AI Service Integration - Gemini & Hugging Face con Spring Boot
Servicios de Inteligencia Artificial (Google & Hugging Face), este proyecto integra dos potentes APIs de IA para la generación de contenido y clasificación de texto de manera dinámica. Utilizando un enfoque reactivo, el sistema permite realizar consultas a modelos de lenguaje avanzados, capturar los resultados y persistirlos en una base de datos PostgreSQL en la nube para su posterior análisis.

**1. Cognitive Services**
<img src ="https://community.pepperdine.edu/it/images/google-gemini-logo-2025-1440x430.png" align="right" style="width: 200px"/>
- Google Gemini 2.5 Flash: Modelo de lenguaje de última generación optimizado para velocidad y eficiencia. Se utiliza para la generación de respuestas inteligentes y procesamiento de lenguaje natural a partir de prompts del usuario.

<img src ="https://adictosaltrabajo.com/wp-content/uploads/2023/05/hf-logo-with-title-1920x510.png" align="right" style="width: 200px"/>
- Hugging Face - Zero-Shot Classification: Utiliza el router de inferencia para categorizar texto en tiempo real sin entrenamiento previo. Ideal para detectar intenciones, sentimientos o etiquetas personalizadas en mensajes.

**2. Spring Boot**
<img src ="https://miro.medium.com/v2/resize:fit:720/format:webp/1*NRBC0wt4t_ThDKYeu4AW2Q.png" align="right" style="height:60px; width: 200px"/>
- Java: JDK 17
- IDE: Visual Studio Code
- Maven: Apache Maven
- Frameworks: Spring Boot 3.5

**3. Maven Dependencias:**
<img src ="https://keepcoding.io/wp-content/uploads/2024/10/imagen-5.png" align="right" style="width: 200px"/>
* spring-boot-starter-webflux
* spring-boot-starter-data-r2dbc
* lombok
* reactor-test
* r2dbc-postgresql

## **Dependencias Spring WebFlux + Postgre (SQL)**

Spring WebFlux | Data R2DBC | Project Reactor | R2DBC PostgreSQL
```
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
      <groupId>io.projectreactor</groupId>
      <artifactId>reactor-test</artifactId>
      <scope>test</scope>
</dependency>
<dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>r2dbc-postgresql</artifactId>
      <scope>runtime</scope>
</dependency>
```

## **Configuración de Credenciales (application.yml)**

Siguiendo los requerimientos del proyecto, todas las credenciales de la base de datos Neon (PostgreSQL) y las API Keys de Gemini y Hugging Face se encuentran centralizadas en el archivo de configuración:

```
server:
  port: 8082

spring:
  r2dbc:
    url: r2dbc:postgresql://ep-steep-field...
    username: ${DB_USER}
    password: ${DB_PASS}

ai:
  gemini:
    api-key: ${GEMINI_KEY}
    base-url: https://generativelanguage.googleapis.com/v1beta
    model: gemini-2.5-flash
  huggingface:
    api-key: ${HF_KEY}
    base-url: https://router.huggingface.co/hf-inference/models
    model: MoritzLaurer/DeBERTa-v3-base-mnli-xnli
```
