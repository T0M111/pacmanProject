# PacmanProject

[![CI/CT](https://github.com/T0M111/pacmanProject/actions/workflows/CI-CT.yml/badge.svg)](https://github.com/T0M111/pacmanProject/actions/workflows/CI-CT.yml)
[![CD](https://github.com/T0M111/pacmanProject/actions/workflows/cd.yml/badge.svg)](https://github.com/T0M111/pacmanProject/actions/workflows/cd.yml)

Juego básico de Pac-Man desarrollado en Java utilizando Swing para la interfaz gráfica.

## Características del Juego

- **Laberinto interactivo**: Tablero configurable de 15x15 celdas con paredes y espacios para moverse
- **Control de Pac-Man**: Movimiento controlado por el usuario mediante las teclas de flecha del teclado
- **Fantasmas con IA**: 4 fantasmas con movimiento automático y detección de colisiones
- **Sistema de puntuación**: Recolección de puntos que aumentan el puntaje del jugador
- **Condiciones de victoria y derrota**: 
  - Victoria: Comer todos los puntos disponibles en el laberinto
  - Derrota: Ser atrapado por un fantasma
- **Efecto Wrap-around**: Pac-Man puede atravesar los bordes del tablero y aparecer en el lado opuesto
- **Interfaz gráfica**: Desarrollada con Java Swing, sencilla y modular
- **Arquitectura modular**: Código bien organizado con separación de responsabilidades
- **Cobertura de tests**: Incluye tests unitarios con JUnit 5 y cobertura con JaCoCo

## Estructura de Archivos

```
pacmanProject/
├── .github/
│   └── workflows/
│       ├── CI-CT.yml          # Workflow de Integración y Prueba Continua
│       └── cd.yml             # Workflow de Despliegue Continuo
├── public/                    # Carpeta para despliegue en GitHub Pages
│   ├── index.html             # Landing page de descarga del juego
│   └── pacman-game.jar        # Artefacto ejecutable actualizado automáticamente
├── src/
│   ├── Game.java              # Clase principal: inicializa la ventana y el juego
│   ├── Board.java             # Lógica y renderizado del tablero
│   ├── Pacman.java            # Lógica y renderizado de Pac-Man
│   ├── Ghost.java             # Lógica y renderizado de los fantasmas
│   ├── Direction.java         # Enum para las direcciones de movimiento
│   ├── BoardTest.java         # Tests unitarios para Board
│   ├── PacmanTest.java        # Tests unitarios para Pacman
│   ├── GhostTest.java         # Tests unitarios para Ghost
│   ├── DirectionTest.java     # Tests unitarios para Direction
│   └── GameTest.java          # Tests unitarios para Game
├── pom.xml                    # Configuración de Maven con JUnit 5 y JaCoCo
└── README.md                  # Este archivo
```

## Requisitos

### Para Ejecutar el Juego
- **Java Runtime Environment (JRE) 8 o superior**

### Para Desarrollo
- **Java Development Kit (JDK) 8 o superior** (recomendado: JDK 17)
- **Maven 3.6 o superior** para compilar y gestionar dependencias
- **IDE recomendado** (opcional): IntelliJ IDEA, Eclipse, Visual Studio Code, o NetBeans

### Dependencias del Proyecto
El proyecto utiliza las siguientes dependencias (gestionadas automáticamente por Maven):
- **JUnit 5.9.3**: Framework de tests unitarios
- **JaCoCo 0.8.10**: Herramienta de cobertura de código

## Compilación y Ejecución

### Con Maven (recomendado)

```bash
# Compilar el proyecto
mvn compile

# Ejecutar el juego
mvn exec:java -Dexec.mainClass="Game"

# O compilar manualmente y ejecutar
mvn compile
java -cp target/classes Game
```

### Sin Maven (método tradicional)

Desde la terminal, navega al directorio raíz y ejecuta:

```bash
javac -d bin src/*.java
java -cp bin Game
```

O si usas un IDE, simplemente importa el proyecto y ejecuta la clase `Game`.

## Tests Unitarios

El proyecto incluye tests unitarios con JUnit 5 y cobertura de código con JaCoCo.

### Ejecutar los tests

```bash
# Ejecutar todos los tests
mvn test

# Ver el reporte de cobertura
mvn test
# El reporte se genera en: target/site/jacoco/index.html
```

### Cobertura de Código

El proyecto tiene una cobertura de **57.8%** de las líneas de código:

- **Direction.java**: 100% - Todas las direcciones y ángulos probados
- **Board.java**: 56.5% - Detección de paredes, colisiones, wrapping
- **Pacman.java**: 41% - Sistema de puntuación y posición
- **Ghost.java**: 29.5% - Creación y posicionamiento de fantasmas
- **Game.java**: Métodos de reflexión probados

Los tests incluyen:
- 29 tests unitarios en total
- Cobertura de lógica de negocio principal
- Tests de métodos públicos y getters
- Validación de constantes y configuración

## Controles

- **Flechas del teclado** para mover a Pac-Man: izquierda, derecha, arriba, abajo.

## GitHub Actions - CI/CD

El proyecto incluye un sistema completo de Integración y Despliegue Continuo (CI/CD) mediante GitHub Actions:

### 🔄 CI/CT - Integración y Prueba Continua
**Archivo**: `.github/workflows/CI-CT.yml`

Este workflow se ejecuta automáticamente en cada push o pull request a las ramas `main`, `master` o `develop`.

**Pasos del workflow**:
1. **Checkout del código**: Descarga el código del repositorio
2. **Configuración de JDK 17**: Prepara el entorno de Java
3. **Compilación**: Compila el código fuente con `javac`
4. **Verificación de clases**: Valida que todas las clases compilaron correctamente
5. **Ejecución de tests**: Ejecuta todos los tests unitarios con Maven
6. **Construcción del JAR**: Genera el archivo ejecutable del juego
7. **Upload de artefactos**: Guarda el JAR generado como artefacto de GitHub (90 días de retención)

### 🚀 CD - Despliegue Continuo
**Archivo**: `.github/workflows/cd.yml`

Este workflow se ejecuta automáticamente después de que el workflow CI/CT finaliza exitosamente en las ramas `main` o `master`.

**Incluye dos jobs principales**:

#### 1. Deploy a GitHub Pages
- Descarga el JAR generado por CI/CT
- Crea una landing page HTML atractiva para descargar el juego
- Despliega automáticamente en GitHub Pages
- **URL del sitio**: Se puede acceder al juego desde la página de GitHub Pages del repositorio

#### 2. Actualización de carpeta public/
- Descarga el JAR más reciente
- Actualiza la carpeta `public/` en el repositorio con el nuevo JAR
- Hace commit automático de los cambios (con flag `[skip ci]` para evitar bucles infinitos)
- Mantiene sincronizada la carpeta de despliegue en el repositorio

**Beneficios del pipeline CI/CD**:
- ✅ Compilación y tests automáticos en cada cambio
- ✅ Despliegue automático del juego en GitHub Pages
- ✅ JAR siempre actualizado y disponible para descarga
- ✅ Artefactos versionados con retención de 90 días
- ✅ Prevención de errores antes de llegar a producción

## Extensiones Futuras

- Mejorar la IA de los fantasmas.
- Añadir niveles y nuevos mapas.
- Implementar efectos de sonido.
- Añadir “power-ups” y más funcionalidades clásicas del juego.


## Despliegue

El proyecto incluye una carpeta `public/` que contiene:
- **pacman-game.jar**: El artefacto ejecutable del juego
- **index.html**: Página de descarga del juego

Esta carpeta está lista para ser desplegada en servicios como Render, Netlify, o cualquier servicio de hosting estático. El workflow de CD actualiza automáticamente esta carpeta después de cada build exitoso.

### Desplegar en Render

1. Conecta tu repositorio de GitHub con Render
2. Configura un nuevo Static Site
3. Establece el directorio de publicación como `public`
4. Render desplegará automáticamente los archivos y servirá la página de descarga

El juego también se despliega automáticamente en GitHub Pages en cada push a la rama principal.

## Autor

Proyecto desarrollado por [T0M111].

---

¡Disfruta programando y jugando Pac-Man!
