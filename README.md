# 🧬 Mutant Detector – Examen Integrador Desarrollo de Software

## 📝 Descripción del proyecto

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este proyecto es una API REST que permite detectar si un humano es un mutante basándose en su secuencia de ADN.

El programa recibe como parámetro un array de Strings que representan cada fila de una matriz cuadrada (NxN) con la secuencia de ADN. Las letras de los Strings solo pueden ser: A, T, C, G, que representan cada base nitrogenada. La regla de negocio define que un humano es considerado mutante si se encuentra más de una secuencia de cuatro letras iguales, de forma horizontal, vertical o diagonal.

Además, el sistema guarda los ADN ya procesados en una base de datos en memoria para evitar recálculos, y provee estadísticas sobre la cantidad de mutantes y humanos analizados.


## 🚀 Funcionalidades principales

-Detección de mutantes: Algoritmo optimizado para verificar secuencias de ADN.

-Persistencia inteligente: Evita re-analizar ADN previamente verificados gracias a hashing + base de datos en memoria.

-Estadísticas: Endpoint para consultar el total de mutantes, humanos y el ratio entre ambos.

-Documentación: API documentada automáticamente mediante Swagger/OpenAPI.

# 🛠 Tecnologías utilizadas

-Lenguaje: Java 21

-Framework: Spring Boot 3.2.0

-Base de datos: H2 (in-memory) para portabilidad y simplicidad.

-ORM / Persistencia: Spring Data JPA / Hibernate

-Build Tool: Gradle

-Testing: JUnit 5, Mockito, MockMvc

-Cobertura de código: JaCoCo

-Containerización: Docker (con Dockerfile incluido)

-Documentación de API: SpringDoc OpenAPI / Swagger

# 🏗 Arquitectura y flujo de ejecución

El proyecto sigue una arquitectura en capas (Controller → Service → Repository) para asegurar separación de responsabilidades, modularidad y escalabilidad.

### 1. Flujo de detección (POST /mutant)

Recepción y validación: El controlador recibe la petición. Se valida que la matriz sea NxN, no sea nula y contenga solo caracteres permitidos (A, T, C, G).

Hashing (SHA-256): Se genera un hash único del array de ADN. Este hash funciona como "huella digital".

Chequeo en caché: Se consulta la base de datos por el hash.

Si existe: Se devuelve el resultado previo, evitando re-cálculo.

Si no existe: Se procede al análisis.

Ejecución del algoritmo: El MutantDetector recorre la matriz buscando secuencias. Se detiene apenas confirma la condición ("short-circuit").

Persistencia: Guarda el registro (hash + resultado) y responde (200 OK mutante / 403 Forbidden humano).

### 2. Estadísticas (GET /stats)

El servicio consulta el repositorio, cuenta cuántos mutantes y humanos hay registrados, calcula el ratio y devuelve el objeto JSON.

# 🔌 API Endpoints

Documentación interactiva disponible en Swagger UI al ejecutar la aplicación:
👉 http://localhost:8080/swagger-ui.html

### *Detectar Mutante*

-URL: /mutant

-Método: POST

-Respuestas:

`200 OK: Es un mutante.`

`403 Forbidden: Es un humano.`

`400 Bad Request: Datos inválidos (matriz no cuadrada, caracteres extraños).`

-Ejemplo Body:

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


### *Obtener Estadísticas*

-URL: /stats

-Método: GET

-Respuesta: Total de mutantes, humanos y ratio.

## ⚙️ Instalación y ejecución

### Requisitos previos

-Java 21

-Docker (opcional)

.Pasos para levantar localmente

1. Clonar el repositorio:
    ```bash
    git clone https://github.com/MateoDLM/examenDSWMutantesMercadoLibre.git
    cd examenDSWMutantesMercadoLibre
    ```

2. Ejecutar la aplicación 

(Linux/Mac):

    ./gradlew bootRun

En Windows:

    gradlew.bat bootRun

La API estará disponible en `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

Consola H2: `http://localhost:8080/h2-console` (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: vacío).

## 🧪 Testing y Cobertura

El proyecto incluye tests unitarios e integración utilizando JUnit 5, Mockito y MockMvc.

Ejecutar los tests:
    
    ./gradlew test

Generar reporte de cobertura (JaCoCo):

    ./gradlew jacocoTestReport


El reporte HTML estará disponible en: `build/reports/jacoco/test/html/index.html`

## ⚡ Optimizaciones y Rendimiento

Para soportar cargas altas, el proyecto implementa múltiples optimizaciones:

🔐 Hashing SHA-256: Evita re-analizar ADN ya procesados, logrando búsquedas O(1) y reduciendo la latencia.

⚡ Índices en BD: Índices en dna_hash e is_mutant para acelerar consultas.

🚀 Algoritmo "Short-Circuit": El detector se detiene al confirmar la condición de mutante, evitando recorrer la matriz completa.

🛡️ Validación temprana: Rechazo inmediato de matrices inválidas o caracteres erróneos.

## 👤 Datos del Autor

Mateo De Luca Montanaro

Email: mateodelucamontanaro@gmail.com

Repositorio: GitHub - examenDSWMutantesMercadoLibre




