# Mutant Detector – Examen Integrador Desarrollo de Software (Detección de Mutantes)

## Descripción del proyecto

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este proyecto es una API REST que permite detectar si un humano es un mutante basándose en su secuencia de ADN.  

El programa recibe como parámetro un array de *Strings* que representan cada fila de una matriz cuadrada (NxN) con la secuencia de ADN. Las letras de los Strings solo pueden ser: `A`, `T`, `C`, `G`, que representan cada base nitrogenada. La regla de negocio define que un humano es considerado mutante si se encuentra **más de una secuencia de cuatro letras iguales**, de forma **horizontal, vertical o diagonal**.  

Además, el sistema guarda los ADN ya procesados en una base de datos en memoria para evitar recálculos, y provee estadísticas sobre la cantidad de mutantes y humanos analizados.  

## Funcionalidades principales

- Detección de mutantes a partir de una secuencia de ADN.  
- Persistencia inteligente: evita re-analizar ADN previamente verificados gracias a hashing + base en memoria.  
- Endpoint de estadísticas: permite consultar el total de mutantes, humanos y el ratio entre ambos.  
- Documentación automática de la API mediante Swagger/OpenAPI.  

---

## Tecnologías utilizadas

- **Lenguaje**: Java 21 :contentReference[oaicite:0]{index=0}  
- **Framework**: Spring Boot 3.2.0 :contentReference[oaicite:1]{index=1}  
- **Base de datos**: H2 (in-memory) para portabilidad y simplicidad. :contentReference[oaicite:2]{index=2}  
- **ORM / Persistencia**: Spring Data JPA / Hibernate :contentReference[oaicite:3]{index=3}  
- **Herramienta de construcción / build**: Gradle :contentReference[oaicite:4]{index=4}  
- **Testing**: JUnit 5, Mockito, MockMvc :contentReference[oaicite:5]{index=5}  
- **Cobertura de código**: JaCoCo :contentReference[oaicite:6]{index=6}  
- **Containerización**: Docker (con Dockerfile incluido) :contentReference[oaicite:7]{index=7}  
- **Documentación de API**: SpringDoc OpenAPI / Swagger :contentReference[oaicite:8]{index=8}  

---

## Arquitectura y flujo de ejecución

El proyecto sigue una arquitectura en capas (Controller → Service → Repository) para asegurar separación de responsabilidades, modularidad y escalabilidad. :contentReference[oaicite:9]{index=9}  

### Flujo de la petición de detección de mutante (POST /mutant)

1. **Recepción y validación**: el controlador (por ejemplo `MutantController`) recibe la petición con la secuencia de ADN. Se valida que la matriz sea NxN, no sea nula y que cada cadena contenga sólo los caracteres permitidos (`A`, `T`, `C`, `G`). :contentReference[oaicite:10]{index=10}  
2. **Generación de huella única (hashing)**: se genera un hash (SHA-256) a partir del array de ADN, para identificar la secuencia de forma única. Este hash sirve como “huella digital” del ADN. :contentReference[oaicite:11]{index=11}  
3. **Chequeo en caché (base de datos)**: se consulta la base de datos con ese hash:  
   - Si ya existe: se devuelve el resultado previamente calculado (mutante / humano), evitando re-cálculo.  
   - Si no existe: se procede al análisis. :contentReference[oaicite:12]{index=12}  
4. **Ejecución del algoritmo de detección**: si el ADN es nuevo, un componente (`MutantDetector`) recorre la matriz buscando secuencias de 4 letras idénticas — horizontal, vertical y diagonal. Si encuentra más de una secuencia, marca como mutante. El algoritmo está optimizado para detener la búsqueda apenas confirma la condición (“short-circuit”). :contentReference[oaicite:13]{index=13}  
5. **Persistencia y respuesta**: guarda el registro (hash + resultado) en la base de datos, y responde con el código HTTP correspondiente (`200 OK` si mutante, `403 Forbidden` si humano). :contentReference[oaicite:14]{index=14}  

### Estadísticas (GET /stats)

Para el endpoint de estadísticas: el servicio consulta el repositorio, cuenta cuántos mutantes y cuántos humanos se han registrado, calcula el ratio y devuelve un objeto JSON con la información. :contentReference[oaicite:15]{index=15}  

---

## API Endpoints

La documentación está disponible mediante Swagger UI al ejecutar la aplicación. :contentReference[oaicite:16]{index=16}  

- **POST** `/mutant` — Detectar si el ADN corresponde a un mutante.  
  - Respuestas:  
    - `200 OK`: es un mutante (cumple la regla) :contentReference[oaicite:17]{index=17}  
    - `403 Forbidden`: es un humano (no cumple la regla) :contentReference[oaicite:18]{index=18}  
    - `400 Bad Request`: solicitud inválida (formato incorrecto, matriz no cuadrada, caracteres inválidos, etc.) :contentReference[oaicite:19]{index=19}  
  - Ejemplo de body JSON:
    ```json
    {
      "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
      ]
    }
    ```

- **GET** `/stats` — Obtener estadísticas: total de mutantes, total de humanos, ratio. :contentReference[oaicite:20]{index=20}  

> **Documentación adicional**: al levantar la aplicación, podés acceder a la interfaz de documentación generada automáticamente (Swagger UI / OpenAPI) para ver en detalle los esquemas, parámetros y ejemplos. :contentReference[oaicite:21]{index=21}  

---

## Instalación y ejecución

### Requisitos previos

- Java 21 (o compatible) :contentReference[oaicite:22]{index=22}  
- Maven/Gradle (aunque se provee wrapper, así que no es obligatorio que lo tengas instalado globalmente) :contentReference[oaicite:23]{index=23}  
- Docker (opcional, si querés correr en contenedor) :contentReference[oaicite:24]{index=24}  

### Pasos para levantar localmente

1. Clonar el repositorio:  
   ```bash
   git clone https://github.com/MateoDLM/examenDSWMutantesMercadoLibre.git
   cd examenDSWMutantesMercadoLibre
   
2. Construir el proyecto con Gradle: 
   ```bash
   ./gradlew build

2. Ejecutar la aplicación:
   ```bash
   ./gradlew bootRun

4. Ejecutar la aplicación:
   Una vez levantada, la API estará disponible (por defecto) en `http://localhost:8080` (o el puerto configurado).

   Podés acceder a documentación Swagger en `http://localhost:8080/swagger-ui.html`

   La aplicación expone el panel de administración de la base de datos en memoria H2: `http://localhost:8080/h2-console`
   -Credenciales por defecto (si tu aplicación no las cambió):
       -JDBC URL: jdbc:h2:mem:testdb
       -Usuario: sa
       -Password: (vacío)

---

## Testing y Cobertura

El proyecto incluye tests unitarios e integración utilizando:

-JUnit 5
-Mockito
-MockMvc

La cobertura de código se genera con:
-JaCoCo

Ejecutar los tests

    ```bash
        ./gradlew test

El reporte de cobertura se genera automáticamente y puede consultarse en:

`/build/reports/jacoco/test/html/index.html`

---

## Optimizaciones y Rendimiento

Para soportar cargas altas, el proyecto implementa múltiples optimizaciones:

🔐 1. Hashing SHA-256 + persistencia indexada

Evita re-analizar ADN ya procesados, logrando búsquedas O(1) y reduciendo la latencia.

⚡ 2. Índices sobre la base de datos

Índices en:

`dna_hash`

`is_mutant`

Aceleran consultas y estadísticas.

🚀 3. Algoritmo “short-circuit”

El detector se detiene al confirmar la condición de mutante, evitando recorrer la matriz completa.

🛡️ 4. Validación temprana

Se rechazan:

-Matrices no NxN
-Caracteres inválidos
-Requests mal formados

Esto reduce consumo innecesario de recursos.

---

## Datos del Autor 

Autor: Mateo De Luca Montanaro
Email / Contacto: mateodelucamontanaro@gmail.com
Repositorio original: https://github.com/MateoDLM/examenDSWMutantesMercadoLibre




