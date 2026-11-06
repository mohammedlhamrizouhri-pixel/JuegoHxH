import java.util.Random;
import java.util.Scanner;

public class HunterAwakening {

    public static void main(String[] args) {

        // =========================
        // CONFIGURACIÓN INICIAL
        // =========================

        Scanner teclado = new Scanner(System.in);
        Random aleatorio = new Random();

        // =========================
        // CONSTANTES
        // =========================

        // Progresión
        final int NIVEL_MAXIMO = 200;
        final int EXP_POR_NIVEL = 20;               // para subir de nivel hacen falta nivel * EXP_POR_NIVEL
        final int VIDA_POR_NIVEL = 15;
        final int STAMINA_POR_NIVEL = 10;
        final int NIVELES_POR_HUECO = 3;            // cada 3 niveles el inventario gana un hueco

        // Costes de las acciones
        final int COSTE_STAMINA_EXPLORAR = 20;
        final int COSTE_STAMINA_ENTRENAR = 30;
        final int COSTE_STAMINA_COMBATE = 25;
        final int COSTE_STAMINA_PISTAS = 15;
        final int EXP_ENTRENAR = 15;                // más el nivel actual
        final int COSTE_POSADA = 10;                // en jenny

        // Sucesos al explorar (en %)
        final int PROBABILIDAD_ENEMIGO = 50;
        final int PROBABILIDAD_OBJETO = 25;         // el resto: no pasa nada
        final int PROBABILIDAD_HUIDA_BASE = 40;
        final double MULTIPLICADOR_AGOTADO = 0.5;   // sin stamina, el daño se reduce a la mitad

        // Inventario
        final int HUECOS_INVENTARIO_MAXIMOS = 10;
        final int MAX_POR_HUECOS = 99;

        // Tiempo y búsqueda del examen
        final int DIAS_HASTA_EXAMEN = 20;
        final int DIAS_VIAJE = 1;
        final int DIAS_DESCANSO = 1;
        final int PISTAS_NECESARIAS = 3;
        final int ZONAS_JUGABLES = 6;               // las 6 primeras zonas del mapa son sede posible del examen

        // Examen de Cazador
        final int COSTE_TRAMO = 18;                 // stamina por tramo de la carrera (se reduce con la velocidad)
        final int COSTE_TRAMO_MINIMO = 6;
        final int TRAMO_NIEBLA = 3;                 // posición del tramo donde aparecen los dos Satotz
        final int STAMINA_FALSO_SATOTZ = 20;
        final int DANO_FALSO_SATOTZ = 25;
        final int DIFICULTAD_CERDO = 70;
        final int DANO_CERDO = 40;
        final int INTENTOS_SUSHI = 2;
        final int PLANTAS_TORRE = 3;
        final int DANO_TRAMPA = 25;
        final int DIAS_ZEVIL = 7;
        final int PUNTOS_NECESARIOS = 6;
        final int PUNTOS_PLACA_PROPIA = 3;
        final int PUNTOS_PLACA_OBJETIVO = 3;
        final int PUNTOS_PLACA_OTRO = 1;
        final int DANO_EMBOSCADA = 20;
        final int VICTORIAS_FINAL_NECESARIAS = 2;

        // =========================
        // VARIABLES DEL PERSONAJE
        // =========================

        String nombre;
        String familia;
        String habilidad;
        String mensajeTalento;
        String historiaFamilia;
        String historiaTalento;

        int fuerza = 10;
        int velocidad = 10;
        int sigilo = 0;
        int aura = 10;
        int controlNen = 10;
        int potencial = 10;

        int vidaMaxima = 100;
        int vida = vidaMaxima;

        int staminaMaxima = 100;
        int stamina = staminaMaxima;

        // =========================
        // PROGRESIÓN
        // =========================

        int jenny = 0;
        int experiencia = 0;
        int nivel = 1;
        int enemigosDerrotados = 0;

        // =========================
        // CONTROL DE PARTIDA
        // =========================

        boolean partidaEnCurso = true;
        boolean victoria = false;
        String motivoFin = "";
        String opcion = "";

        int zonaActual = 2;                                         // empiezas en Isla Ballena
        int diasRestantes = DIAS_HASTA_EXAMEN;
        int zonaExamen = aleatorio.nextInt(ZONAS_JUGABLES);         // lugar del examen (0 a 5), distinto en cada partida
        int pistasEncontradas = 0;
        boolean entradaEncontrada = false;
        boolean examenIniciado = false;
        boolean tieneCarnet = false;

        // =========================
        // INVENTARIO
        // =========================

        int huecosInventario = 6;       // crece con el nivel (máximo: HUECOS_INVENTARIO_MAXIMOS)

        String[] objetosInventario = new String[HUECOS_INVENTARIO_MAXIMOS];
        int[] cantidadesInventario = new int[HUECOS_INVENTARIO_MAXIMOS];
        for (int i = 0; i < objetosInventario.length; i++) {
            objetosInventario[i] = "";      // "" significa hueco libre
            cantidadesInventario[i] = 0;
        }

        // Catálogo de objetos (arrays paralelos: la posición 0 de cada array habla del mismo objeto)
        // Los objetos 0 y 1 se pueden usar; del 2 al 7 son materiales, uno por cada zona jugable (posición = 2 + zona)
        final String[] OBJETO_NOMBRE = {
            "Hierba medicinal", "Fruta energética",
            "Piedra de montaña", "Hierro antiguo", "Concha marina",
            "Savia carmesí", "Seda imperial", "Cuero curtido"};
        final int[] OBJETO_CURA_VIDA = {40, 0, 0, 0, 0, 0, 0, 0};
        final int[] OBJETO_CURA_STAMINA = {0, 40, 0, 0, 0, 0, 0, 0};

        // =========================
        // MAPA
        // =========================

        final String[] LUGARES_MAPA = {
            "Kukuroo Mountain",
            "Isla Shingen",
            "Isla Ballena",
            "Bosque Carmesí",
            "Imperio de Kakin",
            "Continente Yorbian",
            "Torre Celestial",
            "Yorknew City",
            "Greed Island",
            "República de East Gorteau",
            "Ciudad de Meteor",
            "Continente Oscuro"};
        final int[] NIVEL_REQUERIDO = {1, 1, 1, 1, 1, 1, 10, 25, 50, 75, 100, 150};
        final boolean[] LICENCIA_REQUERIDA = {false, false, false, false, false, false,
                                              true, false, true, true, true, true};

        // Datos propios de cada una de las 6 zonas jugables (posición 0 = Kukuroo Mountain ... 5 = Continente Yorbian)
        final String[] ACCION_EXPLORAR = {
            "Explorar la montaña", "Explorar la isla", "Explorar la isla",
            "Explorar el bosque", "Explorar el imperio", "Explorar el continente"};
        final String[] PERSONA_ZONA = {
            "Un viajero", "Un maestro de dojo", "Un marinero",
            "Un explorador cubierto de barro", "Un comerciante elegante", "Un cartógrafo cansado"};
        final String[] TEXTO_NADA = {
            "Recorres la montaña, pero solo oyes el eco de las enormes puertas a lo lejos.",
            "Paseas por las calles de la isla y ves a los vecinos entrenar al amanecer.",
            "Caminas por la playa, pero solo encuentras redes de pesca y gaviotas.",
            "Te pierdes entre los árboles gigantes y regresas sin nada.",
            "Recorres los salones del imperio, vigilado por soldados desconfiados.",
            "Cruzas llanuras interminables sin ver a nadie."};

        // Enemigos (arrays paralelos, uno por zona)
        final String[] ENEMIGO_NOMBRE = {
            "perro guardián", "guerrero errante", "cangrejo gigante",
            "lobo carmesí", "soldado imperial", "bandido del camino"};
        final int[] ENEMIGO_VIDA = {80, 70, 55, 85, 100, 90};
        final int[] ENEMIGO_ATAQUE = {16, 17, 12, 16, 19, 17};
        final int[] ENEMIGO_EXP = {25, 28, 18, 30, 38, 34};
        final int[] ENEMIGO_JENNY = {12, 14, 8, 12, 20, 18};

        // Pistas del examen: lo que se cuenta en cada zona depende de dónde se celebra el examen
        final String[] RUMOR_LINEA1 = {
            "He oído que los examinadores han visitado una zona montañosa donde nadie puede entrar sin permiso.",
            "Parece que los examinadores buscan un lugar rodeado de agua, donde sus habitantes viven siguiendo antiguas costumbres.",
            "He oído hablar de un pequeño territorio rodeado de mar, donde existe una tranquila aldea de pescadores.",
            "Un comerciante asegura que los examinadores han estado buscando un lugar lleno de árboles enormes.",
            "Hay rumores sobre un territorio gobernado por una familia muy poderosa.",
            "Un explorador asegura que los examinadores han viajado a una región enorme, llena de ciudades muy diferentes."};
        final String[] RUMOR_LINEA2 = {
            "Dicen que las puertas de aquel lugar son tan grandes que parecen las de una fortaleza.",
            "Allí la gente es conocida por su disciplina y sus habilidades de combate.",
            "Dicen que sus habitantes llevan una vida sencilla, lejos de las grandes ciudades.",
            "Parece que allí la vegetación es tan espesa que resulta fácil perderse.",
            "Al parecer, sus miembros tienen mucha influencia y cuentan con numerosos soldados a su servicio.",
            "Dicen que recorrerla de un extremo a otro puede llevar muchísimo tiempo."};
        final String[] PISTA_PROGRESO = {
            "Últimamente han pasado por aquí personas que no parecen turistas.",
            "Si buscas el examen, sigue investigando: algo se esconde muy cerca de aquí.",
            "Sígueme. Creo que sé dónde está lo que buscas..."};
        final String[] ENTRADA_EXAMEN = {
            "Tras las enormes puertas de la montaña, un mayordomo te señala una escalera oculta que baja bajo la roca.",
            "En el dojo del maestro, detrás de un panel de bambú, se abre una sala secreta con una gran puerta de hierro.",
            "En la taberna del puerto, bajo una mesa marcada como 'Reservado', hay una trampilla que lleva a un sótano enorme.",
            "En el interior de un árbol gigante y hueco descubres un ascensor de piedra que desciende hacia las raíces.",
            "En un salón del palacio, tras un tapiz, una puerta discreta da a una sala llena de aspirantes silenciosos.",
            "En una posada de la ciudad, la habitación 405 oculta un ascensor que desciende a un enorme salón subterráneo."};

        // Datos del examen
        final String[] TRAMOS = {
            "Túnel de la Victoria", "Escaleras interminables", "Llanura de Numeri",
            "Niebla del pantano", "Última colina"};
        final String SUSHI_CORRECTO = "Arroz con pescado crudo";
        final String[] SUSHI_INCORRECTOS = {"Pasta con salsa de tomate", "Carne asada con patatas"};
        final String[] TRAMPAS = {
            "Un suelo falso se abre bajo tus pies y caes sobre unas puntas de madera.",
            "Una lluvia de flechas sale de las paredes.",
            "El techo desciende y apenas logras escapar a tiempo."};
        final String[] PRISIONERO_NOMBRE = {"Brokk", "Ivor", "Sable"};
        final int[] PRISIONERO_VIDA = {60, 80, 100};
        final int[] PRISIONERO_ATAQUE = {13, 16, 20};
        final String[] MOTIVOS = {
            "Quiero encontrar a mi familia.",
            "Quiero ser el más fuerte.",
            "Quiero conocer el mundo entero."};
        final String[] PELIGROSOS = {"Hisoka", "Ninguno en particular: todos pueden serlo"};
        final String[] RIVALES_FINAL = {"Pokkle", "Hanzo", "Hisoka"};
        final int[] RIVAL_VIDA = {100, 140, 200};
        final int[] RIVAL_ATAQUE = {15, 24, 40};

        // =========================
        // INTRODUCCIÓN
        // =========================

        System.out.println("---- HUNTER: AWAKENING ----");
        System.out.print("Introduce tu nombre: ");
        nombre = teclado.nextLine();

        System.out.println("\n=== CAPITULO 1: EL DESPERTAR ===");
        System.out.println("Ese bebé eres tú, " + nombre
                + ". Has nacido en un mundo donde la fuerza y el poder son lo que te mantiene vivo. ¡Prepárate para tu despertar!");

        System.out.println("\n(Pulsa ENTER para continuar...)");
        teclado.nextLine();

        // =========================
        // GENERACIÓN DE FAMILIA
        // =========================

        int numeroFamilia = aleatorio.nextInt(100);

        if (numeroFamilia < 5) {

            familia = "Kurta";
            fuerza+=25;
            velocidad+=10;
            aura += 25;
            controlNen += 25;
            potencial += 20;

            habilidad = "Ojos Escarlata: Aumenta drásticamente el poder de todas las categorías de Nen al entrar en ira.";

            historiaFamilia =
                    "Naces en mitad de una noche tranquila. Cuando abres los ojos por primera vez, la comadrona se queda helada: tus ojos brillan rojos como el fuego.\n"
                    + "\"Es un Kurta... Creíamos que ya no quedaba nadie de su pueblo.\"\n"
                    + "Tu sangre será codiciada por muchos y tu destino nunca será tranquilo.";

        } else if (numeroFamilia < 10) {

            familia = "Netero";
            velocidad += 25;
            controlNen += 20;
            fuerza += 15;
            aura+=20;
            potencial+=10;

            habilidad = "Guardián de los 100 Guantes: Permite desatar una ráfaga de golpes ineludibles a velocidad divina.";

            historiaFamilia =
                    "Naces con una fuerza extraña: tu primer llanto hace temblar la ventana y tus puños se aferran al dedo del médico sin soltarlo.\n"
                    + "\"Ese agarre... Este niño lleva la sangre de los Netero.\"\n"
                    + "Una familia que lleva generaciones entrenando sin descanso. Ahora te toca a ti.";

        } else if (numeroFamilia < 15) {

            familia = "Zoldyck";

            velocidad += 30;
            sigilo += 30;
            fuerza += 20;
            aura+=15;
            potencial+=20;
            controlNen+=25;

            habilidad = "Asesinato Silencioso: Permite moverse sin emitir la más mínima presencia ni rastro de Nen.";

            historiaFamilia =
                    "Naces en una mansión silenciosa. No lloras: abres los ojos y observas la oscuridad con calma, y nadie te oye respirar.\n"
                    + "Un mayordomo vestido de negro se inclina ante tu cuna: \"Bienvenido, joven amo. Es un Zoldyck.\"\n"
                    + "Una familia de asesinos famosos en todo el mundo. Tu vida no será corriente.";

        } else if (numeroFamilia < 20) {

            familia = "Freecss";

            fuerza += 30;
            potencial += 20;
            aura += 20;
            controlNen+=10;
            velocidad+=10;

            habilidad = "Instinto Salvaje: Capacidad de adaptarse y volverse más fuerte en medio de un combate extremo.";

            historiaFamilia =
                    "Naces durante un viaje, lejos de casa, en una cabaña improvisada. Tu padre deja a tu lado una brújula con un apellido grabado: Freecss.\n"
                    + "\"Nuestra gente nunca se ha quedado quieta. Siempre busca algo más allá del horizonte.\"\n"
                    + "Aunque todavía eres un bebé, no paras de patalear, como si ya quisieras salir a explorar.";

        } else if (numeroFamilia < 23) {

            familia = "Hui Guo Rou";

            aura += 20;
            controlNen += 15;
            potencial += 25;

            habilidad = "Bestia de Nen Hereditaria: Otorga un parásito espiritual defensivo que protege al usuario automáticamente.";

            historiaFamilia =
                    "Naces envuelto en una calma extraña. Algo enorme y sin forma se enrosca alrededor de tu cuna sin hacerte daño.\n"
                    + "Un desconocido te observa desde la puerta y susurra: \"Hui Guo Rou...\"\n"
                    + "Una familia poderosa y llena de secretos. Desde el primer día formas parte de ella.";

        } else {

            familia = "Común";

            habilidad = "Deberas descubrirla durante tu recorrido.";
            fuerza += 30;
            potencial += 20;
            aura += 20;
            controlNen+=10;
            velocidad+=10;

            historiaFamilia =
                    "Naces en una casa humilde y sin ceremonias. Nadie reconoce ningún apellido famoso ni ningún secreto de sangre en tu llegada.\n"
                    + "Eres un bebé corriente, y en un mundo de linajes legendarios eso también tiene su fuerza.\n"
                    + "Lo que consigas será porque lo has ganado tú.";
        }

        // =========================
        // HISTORIA DE LA FAMILIA
        // =========================

        System.out.println("\n=== CAPITULO 2: TU SANGRE ===");
        System.out.println(historiaFamilia);

        System.out.println("\n(Pulsa ENTER para continuar...)");
        teclado.nextLine();

        // =========================
        // GENERACIÓN DE TALENTO
        // =========================

        int numeroTalento = aleatorio.nextInt(1000000);

        int multiplicador = 1;
        int bonus = 0;

        if (numeroTalento < 1) {

            mensajeTalento = "¡1 entre 1.000.000! Un monstruo absoluto";
            multiplicador = 6;

            historiaTalento =
                    "El suelo tiembla y el aire se vuelve pesado. Nadie en el mundo ha sentido una energía así.\n"
                    + "Hasta los más fuertes retroceden un paso al verte.";

        } else if (numeroTalento < 10) {

            mensajeTalento = "¡1 entre 100.000! (Genio Milenario)";
            multiplicador = 5;

            historiaTalento =
                    "Los maestros se quedan callados al ver tu primera demostración.\n"
                    + "\"Algo así solo pasa una vez cada mil años\", murmura uno de ellos.";

        } else if (numeroTalento < 100) {

            mensajeTalento = "¡1 entre 10.000! (Prodigio Legendario)";
            multiplicador = 4;

            historiaTalento =
                    "Lo que a otros les cuesta años, a ti te sale en una tarde.\n"
                    + "Empiezan a contarse historias sobre ti en pueblos que ni conoces.";

        } else if (numeroTalento < 1000) {

            mensajeTalento = "¡1 entre 1.000! (Talento excepcional)";
            multiplicador = 3;

            historiaTalento =
                    "Aprendes más rápido que todos a tu alrededor.\n"
                    + "Alguien te mira con envidia, y otra persona con esperanza.";

        } else if (numeroTalento < 10000) {

            mensajeTalento = "¡1 entre 100! (Prodigio)";
            multiplicador = 2;

            historiaTalento =
                    "Notas que se te dan bien cosas que a los demás les cuestan.\n"
                    + "Todavía no sabes hasta dónde puedes llegar, pero se nota que tienes futuro.";

        } else if (numeroTalento < 100000) {

            mensajeTalento = "¡1 entre 10! (Buen talento)";
            bonus = 10;

            historiaTalento =
                    "No eres un genio, pero tienes algo especial.\n"
                    + "Con algo de práctica puedes llegar bastante lejos.";

        } else {

            mensajeTalento = "Sin talento: deberás esforzarte mucho para mejorar. Sé que puedes hacerlo, demuestra tu determinación y supera tus límites";

            historiaTalento =
                    "Pruebas una y otra vez y los resultados no llegan.\n"
                    + "Te sientes por detrás de los demás, pero decides no rendirte: lo vas a conseguir a base de esfuerzo.";
        }

        // =========================
        // APLICAR BONIFICADORES
        // =========================

        fuerza = fuerza * multiplicador + bonus;
        velocidad = velocidad * multiplicador + bonus;
        sigilo = sigilo * multiplicador + bonus;
        aura = aura * multiplicador + bonus;
        controlNen = controlNen * multiplicador + bonus;
        potencial = potencial * multiplicador + bonus;

        // =========================
        // HISTORIA DEL TALENTO
        // =========================

        System.out.println("\n=== CAPITULO 3: TU TALENTO ===");
        System.out.println(historiaTalento);

        System.out.println("\n(Pulsa ENTER para continuar...)");
        teclado.nextLine();

        // =========================
        // RESUMEN DEL PERSONAJE
        // =========================

        System.out.println("\n---- TU PERSONAJE ----");
        System.out.println("Nombre: " + nombre);
        System.out.println("Familia: " + familia);
        System.out.println("Habilidad Única: " + habilidad);
        System.out.println("Talento: " + mensajeTalento);

        System.out.println("\n---- ESTADÍSTICAS ----");
        System.out.println("Fuerza: " + fuerza);
        System.out.println("Velocidad: " + velocidad);
        System.out.println("Sigilo: " + sigilo);
        System.out.println("Aura: " + aura);
        System.out.println("Control de Nen: " + controlNen);
        System.out.println("Potencial: " + potencial);

        System.out.println("\n¡Comienza tu aventura, " + nombre + "!");


        // =========================
        // MENSAJE EXAMEN HUNTER
        // =========================

        System.out.println("\n===MENSAJE IMPORTANTE===");
        System.out.println("====================================\r\n" +
                "\r\n" +
                "Se acerca una fecha importante.\r\n" +
                "\r\n" +
                "Dentro de poco comenzará el Examen de Cazador.\r\n" +
                "\r\n" +
                "Miles de aspirantes de todo el mundo viajarán para intentar conseguir la licencia, pero encontrar el lugar donde se celebra el examen no será sencillo.\r\n" + //
                "\r\n" +
                "Cada año la ubicación cambia y solo aquellos capaces de descubrirla podrán participar.\r\n" +
                "\r\n" +
                "Tu objetivo es encontrar el acceso al examen antes de que comience.\r\n" +
                "\r\n" +
                "====================================");



        // =========================
        // MENÚ PRINCIPAL
        // =========================

        while (partidaEnCurso) {

            System.out.println("\n==== MENU: HUNTER AWAKENING ====");
            System.out.println("Estás en: " + LUGARES_MAPA[zonaActual] + " · Faltan " + diasRestantes + " días para el examen");
            System.out.println("1. Ver personaje");
            System.out.println("2. Ver estadísticas");
            System.out.println("3. Ver inventario");
            System.out.println("4. Viajar");
            System.out.println("5. Salir");

            System.out.print("\nSelecciona una opción: ");
            opcion = teclado.nextLine();

            switch (opcion) {

                case "1": {
                    System.out.println("\n---- Tu Personaje ----");
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Familia: " + familia);
                    System.out.println("Talento: " + mensajeTalento);
                    System.out.println("Habilidad Única: " + habilidad);
                    if (tieneCarnet) {
                        System.out.println("Licencia de Cazador: SÍ");
                    } else {
                        System.out.println("Licencia de Cazador: todavía no");
                    }
                    break;
                }

                case "2": {
                    System.out.println("\n---- Estadísticas ----");
                    System.out.println("Nivel:          " + nivel);
                    System.out.println("Experiencia:    " + experiencia + "/" + (nivel * EXP_POR_NIVEL));
                    System.out.println("Vida:           " + vida + "/" + vidaMaxima);
                    System.out.println("Stamina:        " + stamina + "/" + staminaMaxima);
                    System.out.println("Fuerza:         " + fuerza);
                    System.out.println("Velocidad:      " + velocidad);
                    System.out.println("Sigilo:         " + sigilo);
                    System.out.println("Aura:           " + aura);
                    System.out.println("Control de Nen: " + controlNen);
                    System.out.println("Potencial:      " + potencial);
                    System.out.println("Enemigos derrotados: " + enemigosDerrotados);
                    System.out.println("Pistas del examen: " + pistasEncontradas + "/" + PISTAS_NECESARIAS);
                    break;
                }

                case "3": {
                    System.out.println("\n---- Inventario ----");
                    System.out.println("Jenny: " + jenny);

                    // Recorrer el inventario: mostrar los huecos usados y contarlos
                    int ocupados = 0;
                    for (int i = 0; i < huecosInventario; i++) {
                        if (compararTextos(objetosInventario[i], "") == false) {
                            ocupados++;
                            System.out.println((i + 1) + ". " + objetosInventario[i] + " x" + cantidadesInventario[i]);
                        }
                    }
                    System.out.println("Huecos ocupados: " + ocupados + "/" + huecosInventario
                            + " (máximo posible: " + HUECOS_INVENTARIO_MAXIMOS + ")");

                    if (ocupados > 0) {
                        System.out.print("Número del objeto a usar (0 para volver): ");
                        String eleccion = teclado.nextLine();

                        // Buscar en el array el hueco elegido
                        int hueco = -1;
                        for (int i = 0; i < huecosInventario; i++) {
                            if (compararTextos(eleccion, "" + (i + 1))) {
                                hueco = i;
                            }
                        }

                        if (compararTextos(eleccion, "0")) {
                            System.out.println("Cierras el inventario.");
                        } else if (hueco == -1) {
                            System.out.println("Opción no válida.");
                        } else if (compararTextos(objetosInventario[hueco], "")) {
                            System.out.println("Ese hueco está vacío.");
                        } else {
                            // Buscar el objeto en el catálogo para saber su efecto
                            int tipo = -1;
                            for (int i = 0; i < OBJETO_NOMBRE.length; i++) {
                                if (compararTextos(OBJETO_NOMBRE[i], objetosInventario[hueco])) {
                                    tipo = i;
                                }
                            }

                            if (OBJETO_CURA_VIDA[tipo] == 0 && OBJETO_CURA_STAMINA[tipo] == 0) {
                                System.out.println("Eso es un material: no se puede usar todavía.");
                            } else {
                                vida = vida + OBJETO_CURA_VIDA[tipo];
                                if (vida > vidaMaxima) {
                                    vida = vidaMaxima;
                                }
                                stamina = stamina + OBJETO_CURA_STAMINA[tipo];
                                if (stamina > staminaMaxima) {
                                    stamina = staminaMaxima;
                                }
                                cantidadesInventario[hueco]--;
                                if (cantidadesInventario[hueco] == 0) {
                                    objetosInventario[hueco] = "";      // el hueco queda libre
                                }
                                System.out.println("Usas " + OBJETO_NOMBRE[tipo] + ". Vida: " + vida + "/" + vidaMaxima
                                        + " · Stamina: " + stamina + "/" + staminaMaxima);
                            }
                        }
                    }
                    break;
                }

                // =============================================================
                //                           VIAJAR
                // =============================================================
                case "4": {
                    boolean viajando = true;

                    while (viajando && partidaEnCurso) {

                        System.out.println("\n---- Viajar ---- (ahora estás en " + LUGARES_MAPA[zonaActual] + ")");
                        for (int i = 0; i < LUGARES_MAPA.length; i++) {
                            String requisito = "";
                            if (LICENCIA_REQUERIDA[i]) {
                                requisito = ", requiere licencia";
                            }
                            System.out.println((i + 1) + ". " + LUGARES_MAPA[i] + " (nivel " + NIVEL_REQUERIDO[i] + requisito + ")");
                        }
                        System.out.println("13. Volver al menú principal");
                        System.out.print("\nSelecciona un destino: ");
                        String destinoTexto = teclado.nextLine();

                        if (compararTextos(destinoTexto, "13")) {
                            viajando = false;
                            continue;
                        }

                        // Convertir el texto en una posición del array (sin parseInt ni excepciones)
                        int destino = -1;
                        for (int i = 0; i < LUGARES_MAPA.length; i++) {
                            if (compararTextos(destinoTexto, "" + (i + 1))) {
                                destino = i;
                            }
                        }

                        if (destino == -1) {
                            System.out.println("Destino inválido.");
                            continue;
                        }
                        if (nivel < NIVEL_REQUERIDO[destino]) {
                            System.out.println("\n¡No tienes nivel suficiente!");
                            System.out.println("Lugar: " + LUGARES_MAPA[destino]);
                            System.out.println("Nivel requerido: " + NIVEL_REQUERIDO[destino] + " · Tu nivel: " + nivel);
                            continue;
                        }
                        if (LICENCIA_REQUERIDA[destino] && (tieneCarnet == false)) {
                            System.out.println("\nNecesitas la Licencia de Cazador para entrar en " + LUGARES_MAPA[destino] + ".");
                            continue;
                        }

                        // Viajar a otra zona cuesta un día; quedarse en la actual no cuesta nada
                        if (destino == zonaActual == false) {
                            zonaActual = destino;
                            diasRestantes = diasRestantes - DIAS_VIAJE;
                            System.out.println("Viajas a " + LUGARES_MAPA[zonaActual] + ". Pasa un día.");
                            if (diasRestantes <= 0 && (examenIniciado == false)) {
                                System.out.println("\nEl Examen de Cazador ha comenzado... y tú todavía no has encontrado la entrada.");
                                motivoFin = "Llegaste tarde: el examen empezó sin ti.";
                                partidaEnCurso = false;
                                continue;
                            }
                        }

                        // ---------------------------------------------------------
                        // ACCIONES EN LA ZONA
                        // ---------------------------------------------------------
                        boolean enZona = true;

                        while (enZona && partidaEnCurso) {

                            if (zonaActual >= ZONAS_JUGABLES) {
                                System.out.println("\nTodavía no hay nada que hacer en " + LUGARES_MAPA[zonaActual]
                                        + " (llegará en próximas versiones).");
                                enZona = false;
                                continue;
                            }

                            // --- Subida de nivel (se comprueba antes de mostrar cada menú) ---
                            while (experiencia >= nivel * EXP_POR_NIVEL && nivel < NIVEL_MAXIMO) {
                                experiencia = experiencia - nivel * EXP_POR_NIVEL;
                                nivel++;
                                fuerza += 2;
                                velocidad += 1;
                                vidaMaxima += VIDA_POR_NIVEL;
                                staminaMaxima += STAMINA_POR_NIVEL;
                                vida = vidaMaxima;
                                stamina = staminaMaxima;
                                System.out.println("\n*** ¡SUBES AL NIVEL " + nivel + "! Recuperas toda la vida y la stamina. ***");
                                if (nivel % NIVELES_POR_HUECO == 0 && huecosInventario < HUECOS_INVENTARIO_MAXIMOS) {
                                    huecosInventario++;
                                    System.out.println("Tu inventario gana un hueco: " + huecosInventario + " en total.");
                                }
                            }

                            // Cast: double -> int para calcular el porcentaje de vida
                            int porcentajeVida = (int) ((double) vida / vidaMaxima * 100);
                            System.out.println("\n---- " + LUGARES_MAPA[zonaActual] + " ----");
                            System.out.println("Vida " + vida + "/" + vidaMaxima + " (" + porcentajeVida + "%) · Stamina "
                                    + stamina + "/" + staminaMaxima + " · Jenny " + jenny + " · Días: " + diasRestantes);
                            System.out.println("1. " + ACCION_EXPLORAR[zonaActual] + " (-" + COSTE_STAMINA_EXPLORAR + " stamina)");
                            System.out.println("2. Entrenar habilidades (-" + COSTE_STAMINA_ENTRENAR + " stamina)");
                            System.out.println("3. Buscar pistas del examen de Hunter (-" + COSTE_STAMINA_PISTAS + " stamina)");
                            System.out.println("4. Descansar (pasa un día)");
                            if (entradaEncontrada && zonaActual == zonaExamen) {
                                System.out.println("5. ENTRAR AL EXAMEN DE CAZADOR");
                            }
                            System.out.println("0. Volver al menú de viajes");
                            System.out.print("\nSelecciona una acción: ");
                            String accion = teclado.nextLine();

                            switch (accion) {

                                // ------------------- EXPLORAR -------------------
                                case "1": {
                                    if (stamina < COSTE_STAMINA_EXPLORAR) {
                                        System.out.println("No tienes stamina suficiente. Descansa o usa una fruta energética.");
                                        break;
                                    }
                                    stamina -= COSTE_STAMINA_EXPLORAR;
                                    int tirada = aleatorio.nextInt(100);

                                    if (tirada < PROBABILIDAD_ENEMIGO) {

                                        // ===== COMBATE POR TURNOS =====
                                        int vidaEnemigo = ENEMIGO_VIDA[zonaActual];
                                        boolean combateActivo = true;

                                        stamina -= COSTE_STAMINA_COMBATE;
                                        if (stamina < 0) {
                                            stamina = 0;
                                        }
                                        System.out.println("\n¡Te ataca un " + ENEMIGO_NOMBRE[zonaActual] + "!");
                                        if (stamina == 0) {
                                            System.out.println("Estás agotado: tus golpes serán más débiles.");
                                        }

                                        while (combateActivo) {
                                            System.out.println("\n" + ENEMIGO_NOMBRE[zonaActual] + ": " + vidaEnemigo + "/"
                                                    + ENEMIGO_VIDA[zonaActual] + " PV  |  Tú: " + vida + "/" + vidaMaxima + " PV");
                                            System.out.println("1. Atacar");
                                            System.out.println("2. Huir");
                                            System.out.print("Combate: ");
                                            String accionCombate = teclado.nextLine();

                                            if (compararTextos(accionCombate, "1")) {
                                                int danoJugador = fuerza / 2 + aleatorio.nextInt(6);
                                                if (stamina == 0) {
                                                    // Cast: el resultado del producto es double y se convierte a int
                                                    danoJugador = (int) (danoJugador * MULTIPLICADOR_AGOTADO);
                                                }
                                                vidaEnemigo -= danoJugador;
                                                System.out.println("Atacas y causas " + danoJugador + " de daño.");

                                                if (vidaEnemigo <= 0) {
                                                    experiencia += ENEMIGO_EXP[zonaActual];
                                                    jenny += ENEMIGO_JENNY[zonaActual];
                                                    enemigosDerrotados++;
                                                    System.out.println("¡Has derrotado al " + ENEMIGO_NOMBRE[zonaActual] + "! +"
                                                            + ENEMIGO_EXP[zonaActual] + " EXP, +" + ENEMIGO_JENNY[zonaActual] + " jenny.");
                                                    combateActivo = false;
                                                    continue;
                                                }
                                            } else if (compararTextos(accionCombate, "2")) {
                                                int probabilidadHuida = PROBABILIDAD_HUIDA_BASE + velocidad / 2;
                                                if (probabilidadHuida > 90) {
                                                    probabilidadHuida = 90;
                                                }
                                                if (aleatorio.nextInt(100) < probabilidadHuida) {
                                                    System.out.println("Consigues huir del combate.");
                                                    combateActivo = false;
                                                    continue;
                                                }
                                                System.out.println("No consigues huir.");
                                            } else {
                                                System.out.println("Opción inválida.");
                                                continue;
                                            }

                                            // --- Turno del enemigo ---
                                            int danoEnemigo = ENEMIGO_ATAQUE[zonaActual] + aleatorio.nextInt(4) - velocidad / 4;
                                            if (danoEnemigo < 1) {
                                                danoEnemigo = 1;
                                            }
                                            vida -= danoEnemigo;
                                            System.out.println("El " + ENEMIGO_NOMBRE[zonaActual] + " te golpea y te causa "
                                                    + danoEnemigo + " de daño.");

                                            // --- Regla: la vida debe ser mayor que 0 ---
                                            if (vida <= 0) {
                                                vida = 0;
                                                System.out.println("\nCaes al suelo sin fuerzas... Tu aventura termina aquí.");
                                                motivoFin = "Moriste en combate contra un " + ENEMIGO_NOMBRE[zonaActual] + ".";
                                                combateActivo = false;
                                                partidaEnCurso = false;
                                            }
                                        }

                                    } else if (tirada < PROBABILIDAD_ENEMIGO + PROBABILIDAD_OBJETO) {

                                        // ===== HALLAZGO DE UN OBJETO =====
                                        int tipoObjeto;
                                        if (aleatorio.nextInt(100) < 40) {
                                            tipoObjeto = aleatorio.nextInt(2);          // hierba o fruta
                                        } else {
                                            tipoObjeto = 2 + zonaActual;                // material propio de la zona
                                        }
                                        String nombreObjeto = OBJETO_NOMBRE[tipoObjeto];
                                        System.out.println("Encuentras: " + nombreObjeto + ".");

                                        // 1) ¿Ya tengo este objeto? Si sí, se apila en el mismo hueco
                                        int posicion = -1;
                                        for (int i = 0; i < huecosInventario; i++) {
                                            if (compararTextos(objetosInventario[i], nombreObjeto)) {
                                                posicion = i;
                                                break;
                                            }
                                        }

                                        if (posicion == -1 == false) {
                                            if (cantidadesInventario[posicion] < MAX_POR_HUECOS) {
                                                cantidadesInventario[posicion]++;
                                                System.out.println("Lo guardas con los demás (x" + cantidadesInventario[posicion] + ").");
                                            } else {
                                                System.out.println("No te caben más " + nombreObjeto + ".");
                                            }
                                        } else {
                                            // 2) Si no lo tengo, busco un hueco libre
                                            for (int i = 0; i < huecosInventario; i++) {
                                                if (compararTextos(objetosInventario[i], "")) {
                                                    posicion = i;
                                                    break;
                                                }
                                            }
                                            if (posicion == -1) {
                                                System.out.println("Tu inventario está lleno. Tienes que dejarlo atrás.");
                                            } else {
                                                objetosInventario[posicion] = nombreObjeto;
                                                cantidadesInventario[posicion] = 1;
                                                System.out.println("Lo guardas en el hueco " + (posicion + 1) + ".");
                                            }
                                        }

                                    } else {
                                        System.out.println(TEXTO_NADA[zonaActual]);
                                    }
                                    break;
                                }

                                // ------------------- ENTRENAR -------------------
                                case "2": {
                                    if (stamina < COSTE_STAMINA_ENTRENAR) {
                                        System.out.println("No tienes stamina suficiente para entrenar.");
                                        break;
                                    }
                                    stamina -= COSTE_STAMINA_ENTRENAR;
                                    int expEntreno = EXP_ENTRENAR + nivel;
                                    experiencia += expEntreno;
                                    System.out.println("Entrenas duro en " + LUGARES_MAPA[zonaActual] + ". +" + expEntreno + " EXP.");
                                    break;
                                }

                                // ------------------- BUSCAR PISTAS -------------------
                                case "3": {
                                    if (stamina < COSTE_STAMINA_PISTAS) {
                                        System.out.println("No tienes stamina suficiente para investigar.");
                                        break;
                                    }
                                    stamina -= COSTE_STAMINA_PISTAS;
                                    System.out.println("Preguntas a la gente de la zona...");

                                    if (entradaEncontrada) {
                                        System.out.println("Ya conoces la entrada del examen. No necesitas más pistas.");
                                    } else if (zonaActual == zonaExamen) {
                                        // Estás en el lugar correcto: cada búsqueda te acerca a la entrada
                                        pistasEncontradas++;
                                        System.out.println(PERSONA_ZONA[zonaActual] + " te dice:");
                                        System.out.println("\"" + PISTA_PROGRESO[pistasEncontradas - 1] + "\"");
                                        if (pistasEncontradas >= PISTAS_NECESARIAS) {
                                            entradaEncontrada = true;
                                            System.out.println("\n*** ¡HAS ENCONTRADO LA ENTRADA DEL EXAMEN! ***");
                                            System.out.println(ENTRADA_EXAMEN[zonaExamen]);
                                            System.out.println("Cuando estés preparado, elige «Entrar al examen».");
                                        }
                                    } else {
                                        // Lugar equivocado: solo rumores sobre dónde está el examen
                                        System.out.println(PERSONA_ZONA[zonaActual] + " niega con la cabeza.");
                                        System.out.println("\"No creo que el examen esté por aquí, pero he escuchado algunos rumores.\"");
                                        System.out.println("\"" + RUMOR_LINEA1[zonaExamen] + "\"");
                                        System.out.println("\"" + RUMOR_LINEA2[zonaExamen] + "\"");
                                    }
                                    break;
                                }

                                // ------------------- DESCANSAR -------------------
                                case "4": {
                                    if (jenny >= COSTE_POSADA) {
                                        jenny -= COSTE_POSADA;
                                        vida = vidaMaxima;
                                        stamina = staminaMaxima;
                                        System.out.println("Pagas " + COSTE_POSADA + " jenny por una posada. Vida y stamina al máximo.");
                                    } else {
                                        // Sin dinero: duermes al raso y recuperas solo una parte
                                        vida = vida + vidaMaxima / 4;
                                        if (vida > vidaMaxima) {
                                            vida = vidaMaxima;
                                        }
                                        stamina = stamina + staminaMaxima / 2;
                                        if (stamina > staminaMaxima) {
                                            stamina = staminaMaxima;
                                        }
                                        System.out.println("No tienes dinero para la posada. Duermes al raso y recuperas solo una parte.");
                                    }
                                    diasRestantes = diasRestantes - DIAS_DESCANSO;
                                    System.out.println("Pasa un día. Faltan " + diasRestantes + " días para el examen.");
                                    if (diasRestantes <= 0 && (examenIniciado == false)) {
                                        System.out.println("\nEl Examen de Cazador ha comenzado... y tú todavía no has entrado.");
                                        motivoFin = "Llegaste tarde: el examen empezó sin ti.";
                                        partidaEnCurso = false;
                                    }
                                    break;
                                }

                                // =================================================
                                //             EXAMEN DE CAZADOR (287.º)
                                // =================================================
                                case "5": {
                                    boolean condicionExamen = false;
                                    if (entradaEncontrada == false) {
                                        condicionExamen = true;
                                    }
                                    if (zonaActual == zonaExamen == false) {
                                        condicionExamen = true;
                                    }
                                    if (condicionExamen == true) {
                                        System.out.println("Opción inválida. Por favor, selecciona una acción válida.");
                                        break;
                                    }

                                    examenIniciado = true;
                                    boolean eliminado = false;
                                    int numeroPlaca = 100 + aleatorio.nextInt(300);
                                    vida = vidaMaxima;
                                    stamina = staminaMaxima;

                                    System.out.println("\n==========================================");
                                    System.out.println("        287.º EXAMEN DE CAZADOR");
                                    System.out.println("==========================================");
                                    System.out.println("Bajas hasta un enorme salón lleno de aspirantes. Un hombrecillo con la cabeza en forma de judía,");
                                    System.out.println("Beans, te entrega una placa con el número " + numeroPlaca + ". Entre la multitud, un hombre de pelo");
                                    System.out.println("rojo y sonrisa inquietante te mira fijamente: Hisoka.");
                                    System.out.println("(Pulsa ENTER para continuar...)");
                                    teclado.nextLine();

                                    // ---------------- FASE 1: LA CARRERA DE SATOTZ ----------------
                                    System.out.println("\n--- FASE 1: LA CARRERA DE SATOTZ ---");
                                    System.out.println("Un hombre de traje y bigote, Satotz, anuncia: \"Seguidme. La primera prueba ha comenzado.\"");

                                    // El coste por tramo baja cuanto más rápido eres
                                    int costeTramo = COSTE_TRAMO - velocidad / 10;
                                    if (costeTramo < COSTE_TRAMO_MINIMO) {
                                        costeTramo = COSTE_TRAMO_MINIMO;
                                    }

                                    for (int tramo = 0; tramo < TRAMOS.length; tramo++) {
                                        stamina -= costeTramo;
                                        System.out.println("\n" + TRAMOS[tramo] + "... (stamina: " + stamina + "/" + staminaMaxima + ")");

                                        if (tramo == TRAMO_NIEBLA) {
                                            System.out.println("¡La niebla se espesa! Delante de ti aparecen DOS hombres con el mismo traje: ambos dicen ser Satotz.");
                                            System.out.println("Uno de ellos es un impostor que lleva a los aspirantes a la trampa.");
                                            int satotzReal = aleatorio.nextInt(2) + 1;
                                            String seguido = "";
                                            while (compararTextos(seguido, "1") == false && compararTextos(seguido, "2") == false) {
                                                System.out.print("¿A cuál sigues? (1 o 2): ");
                                                seguido = teclado.nextLine();
                                            }
                                            if (compararTextos(seguido, "" + satotzReal)) {
                                                System.out.println("Aciertas: era el verdadero Satotz. Sigues el ritmo sin perder fuerzas.");
                                            } else {
                                                System.out.println("¡Era el impostor! Una bestia del pantano te ataca y pierdes tiempo y fuerzas.");
                                                stamina -= STAMINA_FALSO_SATOTZ;
                                                vida -= DANO_FALSO_SATOTZ;
                                            }
                                        }

                                        boolean sinFuerzas = false;
                                        if (stamina <= 0) {
                                            sinFuerzas = true;
                                        }
                                        if (vida <= 0) {
                                            sinFuerzas = true;
                                        }
                                        if (sinFuerzas == true) {
                                            System.out.println("Tus piernas ya no responden. Satotz no se detiene... y te quedas atrás.");
                                            eliminado = true;
                                            break;
                                        }
                                    }
                                    if ((eliminado == false)) {
                                        System.out.println("\n¡Llegas al final del pantano junto al grupo! Primera prueba superada.");
                                        stamina = stamina + staminaMaxima / 2;
                                        if (stamina > staminaMaxima) {
                                            stamina = staminaMaxima;
                                        }
                                    }

                                    // ---------------- FASE 2: LA COCINA DE MENCHI Y BUHARA ----------------
                                    if ((eliminado == false)) {
                                        System.out.println("\n--- FASE 2: LA COCINA DE MENCHI Y BUHARA ---");
                                        System.out.println("Buhara, un cocinero enorme, ordena: \"¡Traedme un cerdo asado del bosque! Cuidado con la Gran Estampida.\"");

                                        int puntosCaza = fuerza + nivel * 3 + aleatorio.nextInt(31);
                                        if (puntosCaza >= DIFICULTAD_CERDO) {
                                            System.out.println("Esquivas la embestida del cerdo salvaje y lo derribas de un solo golpe.");
                                        } else {
                                            System.out.println("El cerdo salvaje te embiste y te lanza contra un árbol.");
                                            vida -= DANO_CERDO;
                                            if (vida <= 0) {
                                                System.out.println("Quedas inconsciente. Quedas descalificado.");
                                                eliminado = true;
                                            } else {
                                                System.out.println("Con esfuerzo, acabas cazándolo igualmente (vida: " + vida + "/" + vidaMaxima + ").");
                                            }
                                        }

                                        if ((eliminado == false)) {
                                            System.out.println("\nTras comer, Menchi, la cocinera, sonríe con crueldad: \"Ahora quiero que me preparéis SUSHI.\"");
                                            System.out.println("Nadie sabe qué es... salvo quien conozca el plato. Solo tienes " + INTENTOS_SUSHI + " intentos.");

                                            int posicionSushi = aleatorio.nextInt(3);
                                            boolean sushiCorrecto = false;
                                            int intentos = 0;

                                            while ((sushiCorrecto == false) && intentos < INTENTOS_SUSHI) {
                                                System.out.println("\n¿Qué ingredientes pones en el plato?");
                                                int malaUsada = 0;
                                                for (int i = 0; i < 3; i++) {
                                                    if (i == posicionSushi) {
                                                        System.out.println((i + 1) + ". " + SUSHI_CORRECTO);
                                                    } else {
                                                        System.out.println((i + 1) + ". " + SUSHI_INCORRECTOS[malaUsada]);
                                                        malaUsada++;
                                                    }
                                                }
                                                System.out.print("Tu elección: ");
                                                String eleccionSushi = teclado.nextLine();

                                                if (compararTextos(eleccionSushi, "" + (posicionSushi + 1))) {
                                                    sushiCorrecto = true;
                                                } else if (compararTextos(eleccionSushi, "1")) {
                                                    intentos++;
                                                    System.out.println("Menchi lo prueba, hace una mueca y lo escupe: \"¡Eso no es sushi!\"");
                                                } else if (compararTextos(eleccionSushi, "2")) {
                                                    intentos++;
                                                    System.out.println("Menchi lo prueba, hace una mueca y lo escupe: \"¡Eso no es sushi!\"");
                                                } else if (compararTextos(eleccionSushi, "3")) {
                                                    intentos++;
                                                    System.out.println("Menchi lo prueba, hace una mueca y lo escupe: \"¡Eso no es sushi!\"");
                                                } else {
                                                    System.out.println("Opción no válida.");
                                                }
                                            }

                                            if (sushiCorrecto) {
                                                System.out.println("Menchi prueba el plato en silencio... y asiente: \"Aprobado.\"");
                                                stamina = stamina + staminaMaxima / 2;
                                                if (stamina > staminaMaxima) {
                                                    stamina = staminaMaxima;
                                                }
                                            } else {
                                                System.out.println("Menchi te señala la salida: \"Descalificado.\"");
                                                eliminado = true;
                                            }
                                        }
                                    }

                                    // ---------------- FASE 3: LA TORRE DE LAS TRAMPAS ----------------
                                    if ((eliminado == false)) {
                                        System.out.println("\n--- FASE 3: LA TORRE DE LAS TRAMPAS ---");
                                        System.out.println("Os dejan en la azotea de una torre. Debéis bajar hasta el suelo en menos de 72 horas.");
                                        System.out.println("Cada planta tiene tres puertas, y detrás de cada una puede esperarte cualquier cosa.");

                                        for (int planta = 1; planta <= PLANTAS_TORRE && (eliminado == false); planta++) {
                                            System.out.println("\nPlanta " + planta + ": hay tres puertas (1, 2 y 3).");
                                            String puerta = "";
                                            while (compararTextos(puerta, "1") == false && compararTextos(puerta, "2") == false && compararTextos(puerta, "3") == false) {
                                                System.out.print("¿Qué puerta abres?: ");
                                                puerta = teclado.nextLine();
                                            }

                                            int suceso = aleatorio.nextInt(3);
                                            if (suceso == 0) {
                                                System.out.println(TRAMPAS[planta - 1]);
                                                vida -= DANO_TRAMPA;
                                                int vidaMostrada = vida;
                                                if (vidaMostrada < 0) {
                                                    vidaMostrada = 0;
                                                }
                                                System.out.println("Pierdes " + DANO_TRAMPA + " de vida. (Vida: " + vidaMostrada + "/" + vidaMaxima + ")");
                                                if (vida <= 0) {
                                                    System.out.println("No puedes seguir. Quedas descalificado.");
                                                    eliminado = true;
                                                }
                                            } else if (suceso == 1) {
                                                System.out.println("Detrás de la puerta te espera " + PRISIONERO_NOMBRE[planta - 1] + ", un prisionero de la torre. ¡Pelea!");
                                                int vidaRival = PRISIONERO_VIDA[planta - 1];
                                                while (vidaRival > 0 && vida > 0) {
                                                    vidaRival -= fuerza / 2 + aleatorio.nextInt(6);
                                                    if (vidaRival > 0) {
                                                        int golpe = PRISIONERO_ATAQUE[planta - 1] + aleatorio.nextInt(4) - velocidad / 4;
                                                        if (golpe < 1) {
                                                            golpe = 1;
                                                        }
                                                        vida -= golpe;
                                                    }
                                                }
                                                if (vida > 0) {
                                                    System.out.println("Derrotas a " + PRISIONERO_NOMBRE[planta - 1] + " (vida: " + vida + "/" + vidaMaxima + ").");
                                                } else {
                                                    System.out.println(PRISIONERO_NOMBRE[planta - 1] + " te derrota. Quedas descalificado.");
                                                    eliminado = true;
                                                }
                                            } else {
                                                System.out.println("Un pasillo tranquilo y vacío. Aprovechas para recuperar el aliento.");
                                                vida = vida + vidaMaxima / 10;
                                                if (vida > vidaMaxima) {
                                                    vida = vidaMaxima;
                                                }
                                            }
                                        }
                                        if ((eliminado == false)) {
                                            System.out.println("\nLlegas al suelo de la torre. ¡Tercera prueba superada!");
                                            vida = vida + vidaMaxima / 3;
                                            if (vida > vidaMaxima) {
                                                vida = vidaMaxima;
                                            }
                                        }
                                    }

                                    // ---------------- FASE 4: LA ISLA ZEVIL ----------------
                                    if ((eliminado == false)) {
                                        System.out.println("\n--- FASE 4: LA ISLA ZEVIL ---");
                                        int placaObjetivo = 100 + aleatorio.nextInt(300);
                                        if (placaObjetivo == numeroPlaca) {
                                            placaObjetivo = placaObjetivo + 1;
                                        }
                                        System.out.println("Cada aspirante saca un número al azar: ese será su objetivo. El tuyo es la placa " + placaObjetivo + ".");
                                        System.out.println("Tu placa vale " + PUNTOS_PLACA_PROPIA + " puntos, la de tu objetivo " + PUNTOS_PLACA_OBJETIVO
                                                + " y las de los demás " + PUNTOS_PLACA_OTRO + ". Necesitas " + PUNTOS_NECESARIOS + " puntos en " + DIAS_ZEVIL + " días.");

                                        int puntosPlaca = PUNTOS_PLACA_PROPIA;

                                        for (int dia = 1; dia <= DIAS_ZEVIL && puntosPlaca < PUNTOS_NECESARIOS && (eliminado == false); dia++) {
                                            System.out.println("\nDía " + dia + "/" + DIAS_ZEVIL + " · Puntos: " + puntosPlaca + "/" + PUNTOS_NECESARIOS
                                                    + " · Vida: " + vida + "/" + vidaMaxima);
                                            System.out.println("1. Cazar a tu objetivo (placa " + placaObjetivo + ")  [+" + PUNTOS_PLACA_OBJETIVO + " pts, difícil]");
                                            System.out.println("2. Cazar a otro aspirante  [+" + PUNTOS_PLACA_OTRO + " pt, más fácil]");
                                            System.out.println("3. Esconderte y descansar");
                                            String decision = "";
                                            while (compararTextos(decision, "1") == false && compararTextos(decision, "2") == false && compararTextos(decision, "3") == false) {
                                                System.out.print("¿Qué haces hoy?: ");
                                                decision = teclado.nextLine();
                                            }

                                            if (compararTextos(decision, "3")) {
                                                vida = vida + vidaMaxima / 5;
                                                if (vida > vidaMaxima) {
                                                    vida = vidaMaxima;
                                                }
                                                System.out.println("Pasas el día oculto entre la maleza y recuperas fuerzas.");
                                            } else {
                                                int probabilidad;
                                                if (compararTextos(decision, "1")) {
                                                    probabilidad = 30 + nivel * 2 + velocidad / 4;
                                                } else {
                                                    probabilidad = 50 + nivel * 2 + velocidad / 4;
                                                }
                                                if (probabilidad > 85) {
                                                    probabilidad = 85;
                                                }

                                                if (aleatorio.nextInt(100) < probabilidad) {
                                                    if (compararTextos(decision, "1")) {
                                                        puntosPlaca += PUNTOS_PLACA_OBJETIVO;
                                                        System.out.println("¡Logras sorprender a tu objetivo y te quedas con su placa! +" + PUNTOS_PLACA_OBJETIVO + " puntos.");
                                                    } else {
                                                        puntosPlaca += PUNTOS_PLACA_OTRO;
                                                        System.out.println("Emboscas a un aspirante y le quitas la placa. +" + PUNTOS_PLACA_OTRO + " punto.");
                                                    }
                                                } else {
                                                    vida -= DANO_EMBOSCADA;
                                                    System.out.println("La presa te descubre y te hiere antes de escapar. Pierdes " + DANO_EMBOSCADA + " de vida.");
                                                    if (vida <= 0) {
                                                        System.out.println("Quedas inconsciente. Descalificado.");
                                                        eliminado = true;
                                                    }
                                                }
                                            }
                                        }
                                        if ((eliminado == false) && puntosPlaca < PUNTOS_NECESARIOS) {
                                            System.out.println("\nSe acaba el tiempo en la isla y no reúnes los puntos necesarios. Descalificado.");
                                            eliminado = true;
                                        }
                                        if ((eliminado == false)) {
                                            System.out.println("\n¡Reúnes " + puntosPlaca + " puntos! Cuarta prueba superada.");
                                            vida = vida + vidaMaxima / 2;
                                            if (vida > vidaMaxima) {
                                                vida = vidaMaxima;
                                            }
                                        }
                                    }

                                    // ---------------- FASE 5: ENTREVISTA Y TORNEO FINAL ----------------
                                    int motivoElegido = 0;
                                    int peligrosoElegido = 0;
                                    int victoriasFinal = 0;

                                    if ((eliminado == false)) {
                                        System.out.println("\n--- FASE 5: LA ENTREVISTA Y EL TORNEO FINAL ---");
                                        System.out.println("En una sala sencilla te recibe un anciano de aspecto amable: Netero, el presidente de la Asociación de Cazadores.");

                                        System.out.println("\n\"¿Por qué quieres ser Cazador?\"");
                                        for (int i = 0; i < MOTIVOS.length; i++) {
                                            System.out.println((i + 1) + ". " + MOTIVOS[i]);
                                        }
                                        String respuesta = "";
                                        while (compararTextos(respuesta, "1") == false && compararTextos(respuesta, "2") == false && compararTextos(respuesta, "3") == false) {
                                            System.out.print("Tu respuesta: ");
                                            respuesta = teclado.nextLine();
                                        }
                                        for (int i = 0; i < MOTIVOS.length; i++) {
                                            if (compararTextos(respuesta, "" + (i + 1))) {
                                                motivoElegido = i;
                                            }
                                        }

                                        System.out.println("\n\"¿Y cuál de los demás aspirantes te parece más peligroso?\"");
                                        for (int i = 0; i < PELIGROSOS.length; i++) {
                                            System.out.println((i + 1) + ". " + PELIGROSOS[i]);
                                        }
                                        respuesta = "";
                                        while (compararTextos(respuesta, "1") == false && compararTextos(respuesta, "2") == false && compararTextos(respuesta, "3") == false) {
                                            System.out.print("Tu respuesta: ");
                                            respuesta = teclado.nextLine();
                                        }
                                        for (int i = 0; i < PELIGROSOS.length; i++) {
                                            if (compararTextos(respuesta, "" + (i + 1))) {
                                                peligrosoElegido = i;
                                            }
                                        }
                                        System.out.println("\nNetero sonríe y apunta algo en su libreta. \"Interesante... Veamos qué tal peleas.\"");

                                        System.out.println("\n--- TORNEO FINAL ---");
                                        System.out.println("Debes ganar al menos " + VICTORIAS_FINAL_NECESARIAS + " de los " + RIVALES_FINAL.length + " combates.");

                                        for (int rival = 0; rival < RIVALES_FINAL.length; rival++) {
                                            System.out.println("\nCombate " + (rival + 1) + ": ¡" + RIVALES_FINAL[rival] + " contra " + nombre + "!");
                                            // Entre combates se recupera un poco
                                            vida = vida + vidaMaxima / 4;
                                            if (vida > vidaMaxima) {
                                                vida = vidaMaxima;
                                            }

                                            int vidaRival = RIVAL_VIDA[rival];
                                            while (vidaRival > 0 && vida > 0) {
                                                vidaRival -= fuerza / 2 + aleatorio.nextInt(6);
                                                if (vidaRival > 0) {
                                                    int golpe = RIVAL_ATAQUE[rival] + aleatorio.nextInt(4) - velocidad / 4;
                                                    if (golpe < 1) {
                                                        golpe = 1;
                                                    }
                                                    vida -= golpe;
                                                }
                                            }

                                            if (vida > 0) {
                                                victoriasFinal++;
                                                System.out.println("¡Ganas el combate contra " + RIVALES_FINAL[rival] + "! (vida: " + vida + "/" + vidaMaxima + ")");
                                            } else {
                                                System.out.println(RIVALES_FINAL[rival] + " te derrota... pero un combate perdido no te elimina todavía.");
                                                vida = vidaMaxima / 4;
                                            }
                                        }

                                        if (victoriasFinal < VICTORIAS_FINAL_NECESARIAS) {
                                            System.out.println("\nSolo has ganado " + victoriasFinal + " combate(s). No es suficiente. Quedas descalificado.");
                                            eliminado = true;
                                        }
                                    }

                                    // ---------------- RESULTADO DEL EXAMEN ----------------
                                    if (eliminado) {
                                        motivoFin = "Fuiste eliminado en el 287.º Examen de Cazador.";
                                        partidaEnCurso = false;
                                    } else {
                                        tieneCarnet = true;
                                        victoria = true;
                                        partidaEnCurso = false;
                                        System.out.println("\n==========================================");
                                        System.out.println("   ¡HAS APROBADO EL EXAMEN DE CAZADOR!");
                                        System.out.println("==========================================");
                                        System.out.println("Netero te entrega personalmente la Licencia de Cazador.");
                                        System.out.println("Respondiste: \"" + MOTIVOS[motivoElegido] + "\"");
                                        System.out.println("Y consideraste más peligroso a: " + PELIGROSOS[peligrosoElegido]);
                                    }
                                    break;
                                }

                                case "0": {
                                    enZona = false;
                                    break;
                                }

                                default: {
                                    System.out.println("Opción inválida. Por favor, selecciona una acción válida.");
                                }
                            }
                        }
                    }
                    break;
                }

                case "5": {
                    partidaEnCurso = false;
                    System.out.println("Saliendo del juego... ¡Hasta pronto, Hunter!");
                    break;
                }

                default: {
                    System.out.println("Opción inválida. Por favor, selecciona una opción válida.");
                }
            }
        }

        // =========================
        // FIN DE LA PARTIDA
        // =========================

        System.out.println("\n========== FIN DE LA PARTIDA ==========");
        if (victoria) {
            System.out.println("¡VICTORIA! " + nombre + " ya es un Cazador con licencia. Su verdadera aventura acaba de empezar...");
        } else if (compararTextos(motivoFin, "") == false) {
            System.out.println("DERROTA: " + motivoFin);
        } else {
            System.out.println(nombre + " abandona la aventura.");
        }
        System.out.println("Nivel final: " + nivel + " · Enemigos derrotados: " + enemigosDerrotados);

        teclado.close();
    }
    public static boolean compararTextos(String texto1, String texto2) {

    if (texto1.length() == texto2.length()) {
        return false;
    }

    for (int i = 0; i < texto1.length(); i++) {
        if (texto1.charAt(i) == texto2.charAt(i)) {
            return false;
        }
    }

    return true;
}

}