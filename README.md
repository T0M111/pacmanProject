# PacmanProject

Juego básico de Pac-Man desarrollado en Java utilizando Swing para la interfaz gráfica.

## Características

- Laberinto donde Pac-Man y los fantasmas se mueven.
- Movimiento de Pac-Man controlado por el usuario (teclas de flecha).
- Fantasmas con movimiento automático.
- Puntos coleccionables que aumentan el puntaje.
- Condiciones de victoria (comer todos los puntos) y derrota (ser atrapado por un fantasma).
- Interfaz gráfica sencilla y modular.

## Estructura de Archivos

```
src/
├── Game.java         # Clase principal: inicializa la ventana y el juego
├── Board.java        # Lógica y renderizado del tablero
├── Pacman.java       # Lógica y renderizado de Pac-Man
├── Ghost.java        # Lógica y renderizado de los fantasmas
└── Direction.java    # Enum para las direcciones de movimiento
```

## Requisitos

- Java 8 o superior
- Maven 3.6 o superior (para compilar y ejecutar tests)
- (Opcional) IDE como IntelliJ IDEA, Eclipse, VSCode, etc.

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

Proyecto desarrollado por [paberlo].

---

¡Disfruta programando y jugando Pac-Man!