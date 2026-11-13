[README.md](https://github.com/user-attachments/files/34817606/README.md)
# Hunter Awakening

Videojuego de aventura, exploración y combates por turnos en consola, desarrollado en Java para **Programación de 1.º DAW** (primera evaluación). Inspirado en el universo de *Hunter x Hunter*.

**Autor:** Mohammed Lhamri · 1.º DAW · Módulo de Programación

## Descripción

Naces en un mundo donde la fuerza lo es todo. Tu objetivo es **encontrar la entrada del Examen de Cazador antes de que se agoten los días** y superar todas sus pruebas para conseguir la Licencia de Cazador.

## Características

- Creación aleatoria del personaje: familia (con su habilidad única) y talento.
- Estadísticas, niveles y experiencia (`experiencia >= nivel * EXP_POR_NIVEL`).
- Mapa de 12 localizaciones con nivel mínimo y licencia requeridos.
- Exploración con sucesos aleatorios: enemigo, objeto o nada.
- Combates por turnos.
- Inventario con huecos, objetos apilables y objetos consumibles.
- Sistema de pistas para localizar la entrada del examen.
- Examen de Cazador con varias pruebas: carrera de Satotz, cocina de Buhara y Menchi, torre de las trampas, isla Zevil, entrevista y torneo final.
- Condiciones de victoria y de derrota.

## Requisitos

- **Java 21** (JDK 21 o superior). Comprueba tu versión con:

```bash
java -version
```

## Ubicación del archivo principal

El código fuente está en:

```
HunterAwakening.java
```

(clase principal: `HunterAwakening`, método `main`)

## Compilar y ejecutar

Desde la carpeta donde esté `HunterAwakening.java`:

```bash
javac HunterAwakening.java
java HunterAwakening
```

## Cómo iniciar una partida

1. Ejecuta el programa e introduce el nombre de tu personaje.
2. Pulsa **ENTER** para avanzar por los capítulos de la historia (familia, talento y resumen del personaje).
3. En el **menú principal** elige una opción escribiendo su número y pulsando ENTER:

   | Opción | Acción |
   |---|---|
   | 1 | Ver personaje |
   | 2 | Ver estadísticas |
   | 3 | Ver inventario / usar objetos |
   | 4 | Viajar |
   | 5 | Salir |

4. En **Viajar** elige un destino (1–12) o `13` para volver. Cambiar de zona cuesta un día.
5. Dentro de una zona puedes explorar, entrenar, buscar pistas del examen, descansar o volver al menú de viajes.
6. Cuando encuentres la entrada del examen, elige **«Entrar al examen»** en la zona correcta.

## Guía paso a paso: de la primera pantalla a la Licencia de Cazador

Sigue estos pasos en orden. Todas las opciones se eligen escribiendo el número y pulsando ENTER.

### Paso 1. Crear tu personaje
1. Escribe tu nombre.
2. Pulsa ENTER en los tres capítulos (el despertar, tu sangre y tu talento).
3. Lee tu resumen: familia, habilidad, talento y estadísticas. Empiezas en **Isla Ballena** con 20 días por delante.

### Paso 2. Comprueba tu estado
En el menú principal elige `2` para ver tu nivel, vida, stamina y estadísticas. Elige `3` para ver el inventario (empieza vacío).

### Paso 3. Entra en una zona
1. En el menú principal elige `4` (Viajar).
2. Elige un destino del **1 al 6** (Kukuroo Mountain, Isla Shingen, Isla Ballena, Bosque Carmesí, Imperio de Kakin, Continente Yorbian). Las zonas 7 a 12 todavía no tienen contenido.
3. Cada vez que cambias de zona pasa **1 día**. Quedarte en la misma zona no cuesta nada.

### Paso 4. Prepara a tu personaje (sube de nivel)
En el menú de la zona:
- `1` **Explorar** (−20 stamina): puede salir un enemigo, un objeto o nada. Si aparece un enemigo, elige `1` Atacar o `2` Huir hasta ganar.
- `2` **Entrenar** (−30 stamina): te da experiencia sin combatir.
- Con la experiencia suficiente subes de nivel y recuperas vida y stamina.
- Si te quedas sin stamina, elige `4` **Descansar** (pasa 1 día). Con 10 jenny duermes en posada y recuperas todo.

Recoge **hierbas** (+40 vida) y **frutas** (+40 stamina) cuando salgan. Las usas desde el menú principal, opción `3`. Un nivel 3 o 4 es un buen objetivo antes de entrar al examen.

### Paso 5. Busca dónde se celebra el examen
1. En cada zona elige `3` **Buscar pistas** (−15 stamina).
2. En una zona equivocada, un personaje te dará **rumores** que describen el lugar correcto:

| Si el rumor habla de… | Ve a… |
|---|---|
| Una zona montañosa con puertas enormes como una fortaleza | Kukuroo Mountain |
| Un lugar rodeado de agua con gente disciplinada y habilidades de combate | Isla Shingen |
| Un pequeño territorio rodeado de mar con una aldea de pescadores | Isla Ballena |
| Un lugar lleno de árboles enormes donde es fácil perderse | Bosque Carmesí |
| Un territorio gobernado por una familia poderosa con muchos soldados | Imperio de Kakin |
| Una región enorme con ciudades muy diferentes | Continente Yorbian |

3. Viaja a esa zona (1 día).

### Paso 6. Encuentra la entrada
1. En la zona correcta, elige `3` **Buscar pistas** tres veces. Cada búsqueda te da una pista nueva.
2. Con la tercera pista aparece el mensaje **«HAS ENCONTRADO LA ENTRADA DEL EXAMEN»**.
3. Comprueba tus estadísticas (opción `2` del menú principal) y ve a ese menú de zona para recuperarte si hace falta. Recuerda que descansar cuesta un día.

### Paso 7. Entra al examen
En el menú de esa zona aparece ahora la opción `5` **ENTRAR AL EXAMEN DE CAZADOR**. Elígela. Entras con la vida y la stamina al máximo. Durante el examen no puedes usar objetos.

### Paso 8. Supera las cinco pruebas
1. **Carrera de Satotz:** corres cinco tramos y gastas stamina en cada uno. En la niebla aparecen dos Satotz: elige `1` o `2` (uno es falso y te quita vida y stamina). Si te quedas sin stamina o sin vida, quedas eliminado.
2. **Cocina de Buhara y Menchi:** primero cazas un cerdo. Después Menchi pide sushi: tienes **2 intentos** y el plato correcto es **«Arroz con pescado crudo»**.
3. **Torre de las trampas:** 3 plantas. En cada una eliges una puerta (`1`, `2` o `3`). Puede haber una trampa, un prisionero con el que luchar o un pasillo tranquilo.
4. **Isla Zevil:** tienes 7 días y necesitas **6 puntos**. Empiezas con 3. Cada día eliges `1` cazar a tu objetivo (+3, difícil), `2` cazar a otro aspirante (+1, más fácil) o `3` esconderte y recuperar vida.
5. **Entrevista y torneo final:** contestas dos preguntas y luchas contra tres rivales. Debes **ganar al menos 2 combates**. Perder un combate no te elimina todavía.

### Paso 9. Recibe la licencia
Si superas la quinta prueba aparece **«¡HAS APROBADO EL EXAMEN DE CAZADOR!»**. Netero te entrega la **Licencia de Cazador**, la partida termina con **VICTORIA** y se muestra tu nivel final y los enemigos derrotados.

### Ejemplo de ruta rápida (unos 6 a 9 días)
1. Isla Ballena: explorar 2 veces y buscar pistas (rumor sobre el lugar correcto).
2. Viajar a la zona que indique el rumor (día 1).
3. Entrenar o explorar y buscar pistas hasta tener las 3 (cuesta stamina: descansa solo si hace falta).
4. Entrar al examen.

Si no aciertas la zona a la primera, viaja a la siguiente. Cada viaje y cada descanso restan un día. Si los días llegan a 0 sin haber entrado al examen, pierdes.

## Reglas principales

- Tienes **20 días** para encontrar el examen. Viajar y descansar cuestan 1 día.
- Necesitas **3 pistas**, que solo se consiguen buscando en la zona donde se celebra el examen. Las otras zonas solo dan rumores.
- Algunas zonas exigen un **nivel mínimo** y/o la **Licencia de Cazador**.
- Con la stamina a 0, tu daño se reduce a la mitad.
- Ganas si superas todas las pruebas del examen. Pierdes si te quedas sin vida, eres eliminado en el examen o se agotan los días.
