# 🧬 Mutant Detector - Examen Mercadolibre

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![Coverage](https://img.shields.io/badge/Coverage-Jacoco-red.svg)](https://github.com/jacoco/jacoco)

## 📝 Descripción del Proyecto

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este proyecto es una API REST que permite detectar si un humano es un mutante basándose en su secuencia de ADN.

El programa recibe como parámetro un array de Strings que representan cada fila de una tabla de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las cuales representa cada base nitrogenada.

**Regla de Negocio:**
Un humano es considerado **mutante** si se encuentra **más de una secuencia de cuatro letras iguales**, de forma oblicua, horizontal o vertical.

### 🚀 Funcionalidades Principales
1.  **Detección de Mutantes:** Algoritmo eficiente para verificar secuencias de ADN.
2.  **Persistencia Inteligente:** Guarda los ADNs verificados en una base de datos H2 (en memoria) para evitar recálculos y generar estadísticas.
3.  **Estadísticas:** Endpoint para consultar la cantidad de mutantes, humanos y el ratio.
4.  **Documentación:** API documentada con Swagger/OpenAPI.

---

## 🛠 Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.0
* **Base de Datos:** H2 Database (In-Memory para portabilidad)
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Gradle
* **Testing:** JUnit 5, Mockito, MockMvc
* **Code Coverage:** Jacoco
* **Containerization:** Docker
* **Documentación:** SpringDoc OpenApi (Swagger)

---

## 🏗 Arquitectura y Flujo de Ejecución

El proyecto sigue una arquitectura en capas (Controller, Service, Repository) para asegurar la separación de responsabilidades y la escalabilidad. A continuación, se detalla cómo se procesan las peticiones internamente.

### 1. Análisis de ADN (POST /mutant)
El proceso de verificación de un mutante sigue los siguientes pasos lógicos:

1.  **Recepción y Validación:**
    El `MutantController` recibe la petición. Antes de procesar nada, se validan los datos de entrada usando anotaciones (`@ValidDnaSequence`). Se asegura que la matriz sea NxN, no sea nula y solo contenga caracteres válidos (A, T, C, G).

2.  **Generación de Huella Única (Hashing):**
    Para optimizar las búsquedas, no se guarda la cadena de ADN completa como índice. En su lugar, el `MutantService` genera un **Hash SHA-256** único a partir del array de ADN. Este hash funciona como una "huella digital" del ADN.

3.  **Verificación en Caché (Base de Datos):**
    El sistema consulta la base de datos usando el hash generado.
    * **Si existe:** Se recupera el resultado previo (Mutante o Humano) y se devuelve inmediatamente, ahorrando tiempo de procesamiento.
    * **Si no existe:** Se procede al análisis.

4.  **Ejecución del Algoritmo (MutantDetector):**
    Si el ADN es nuevo, el componente `MutantDetector` recorre la matriz buscando secuencias de 4 letras iguales (horizontales, verticales y diagonales).
    * *Optimización:* El algoritmo se detiene ("short-circuit") tan pronto encuentra más de una secuencia, marcando al sujeto como mutante sin necesidad de recorrer el resto de la matriz.

5.  **Persistencia y Respuesta:**
    Se guarda el nuevo registro en la base de datos (Hash + Resultado) y se devuelve el código HTTP correspondiente (`200 OK` para mutantes, `403 Forbidden` para humanos).

### 2. Reporte de Estadísticas (GET /stats)
Este endpoint está diseñado para ser rápido y eficiente:

1.  El servicio `StatsService` delega la consulta al repositorio (`DnaRecordRepository`).
2.  Se ejecutan consultas agregadas (`COUNT`) directamente en la base de datos para obtener el número de mutantes y humanos.
3.  Se calcula el ratio matemático en tiempo real y se devuelve el objeto JSON con las estadísticas.

---

## ⚡ Optimizaciones Implementadas

Para soportar fluctuaciones agresivas de tráfico (100 a 1 millón de peticiones por segundo teóricas), se implementaron las siguientes mejoras:

1.  **Hashing SHA-256:**
    * Permite búsquedas `O(1)` (por índice) en la base de datos para verificar si un ADN ya fue analizado anteriormente, reduciendo drásticamente la latencia en peticiones repetidas.
2.  **Indexación en Base de Datos:**
    * Se crearon índices en la columna `dna_hash` y `is_mutant` para acelerar las consultas de búsqueda y conteo estadístico.
3.  **Algoritmo "Short-Circuit":**
    * Evita recorrer toda la matriz innecesariamente. Si ya se confirmó la condición de mutante (más de 1 secuencia), el proceso se detiene.
4.  **Validación Temprana:**
    * Se rechazan inputs inválidos (caracteres extraños, matrices no cuadradas) antes de entrar a la lógica de negocio, protegiendo los recursos del servidor.

---

## 🔌 API Endpoints

Documentación completa disponible en Swagger UI al ejecutar la app:
👉 `http://localhost:8080/swagger-ui.html`

### 1. Detectar Mutante
* **URL:** `/mutant`
* **Método:** `POST`
* **Códigos de Respuesta:**
    * `200 OK`: Es un Mutante.
    * `403 Forbidden`: Es un Humano.
    * `400 Bad Request`: ADN inválido (formato erróneo o caracteres no permitidos).

**Ejemplo Body:**
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
