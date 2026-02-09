import java.util.Arrays;
import java.util.Scanner;

/**
 * Juego de Tres en Raya por consola.
 *
 * <p>Permite:
 * <ul>
 *   <li>Jugar una partida entre 2 jugadores</li>
 *   <li>Mostrar un ranking de las 4 mejores partidas (menos turnos = mejor)</li>
 * </ul>
 *
 * <p>El tablero se representa con una matriz 3x3 de caracteres:
 * <ul>
 *   <li>'_' para casilla vacía</li>
 *   <li>'X' y 'O' para las fichas de los jugadores</li>
 * </ul>
 *
 * <p>Incluye colores ANSI para mejorar la salida por consola.
 */
public class tresEnRaya {
    public static final String RESET = "\u001B[0m";
    public static final String VERDE  = "\u001B[32m";
    public static final String FONDO_AZUL = "\u001B[44m";
    public static final String ROJO = "\u001B[31m";
    static Scanner key = new Scanner(System.in);
    public static void main(String[] args) {

        String [] jugadores = new String[2];
        char [] ficha = {'X', 'O'};
        String[] mejoresJugadores = new String[4];
        int [] mejoresTurnos = new int[4];


        int fila = -1, columna = -1;
        char opcion;

        do {
            System.out.print("""
                Selecciona una opcion: 
                
                [A]. JUGAR
                [B]. MOSTRAR PUNTUACIONES
                [C]. SALIR
                
                OPCION →  """);
            opcion = key.next().toUpperCase().charAt(0);
            key.nextLine();

            switch (opcion) {
                case 'A':
                    char[][] tablero = {
                            {'_','_','_'},
                            {'_','_','_'},
                            {'_','_','_'}
                    };

                    int turnoJugador;
                    String ganador = "";
                    boolean lleno = false, victoria = false;

                    for (int i = 0; i < jugadores.length; i++) {
                        System.out.print("Introduce el nombre del jugador " + (i+1)+": ");
                        jugadores[i] = key.nextLine().toUpperCase();
                    }

                    decideQuienEmpieza(jugadores);

                    System.out.println("\nEMPIEZA " +jugadores[0] + "["+ficha[0]+"]");
                    turnoJugador = 0;
                    int ronda = 0;
                    //Mustra tablero
                    mostrarTablero(tablero, ROJO, VERDE, RESET);
                    do {
                        //Jugador selecciona casilla
                        do {
                            fila = 0;
                            columna = 0;
                            System.out.print("Selecciona una fila(1-3): ");
                            fila = key.nextInt() -1;
                            key.nextLine();

                            System.out.print("Selecciona una columna(1-3): ");
                            columna = key.nextInt() -1;
                            key.nextLine();

                        } while (!esMovimientoValido(tablero, fila, columna));


                        realizarMovimiento(tablero, fila, columna, ficha, turnoJugador);
                        ronda++;
                        mostrarTablero(tablero, ROJO, VERDE, RESET);

                        if (haGanado(tablero) == ficha[0]) {
                            victoria = true;
                            ganador = jugadores[0];
                            System.out.printf("%S HA GANADO!", jugadores[0]);
                            System.out.println();
                        } else if (haGanado(tablero) == ficha[1]) {
                            victoria = true;
                            ganador = jugadores[1];
                            System.out.printf("%S HA GANADO!", jugadores[1]);
                            System.out.println();
                        } else if (tableroLleno(tablero)) {
                            lleno = true;
                            System.out.printf("EMPATE!%n");
                        } else {
                            turnoJugador = cambiarTurno(jugadores, turnoJugador, ficha);
                        }
                    } while ((!lleno) && (!victoria));

                    if (victoria){
                        almacenarPuntuacion(ganador, ronda, mejoresJugadores, mejoresTurnos);
                    }
                    System.out.println("Fin del juego.");

                    break;


                case 'B':
                    mostrarPuntuaciones(mejoresJugadores, mejoresTurnos);
                    break;


                case 'C':
                    System.out.println("FIN DEL PROGRAMA.");
                    break;


                default:
                    System.out.println("La opcion no es valida.");
            }

        } while (opcion != 'C');
    }
    /**
     * Decide qué jugador empieza.
     *
     * <p>Actualmente ordena alfabéticamente el array de jugadores y coloca primero al que "va antes".
     *
     * @param jugadores array con los nombres de los jugadores.
     */
    public static void decideQuienEmpieza(String [] jugadores) {
        Arrays.sort(jugadores);
    }
    /**
     * Muestra el tablero por consola.
     *
     * <p>Imprime la matriz 3x3 aplicando color:
     * <ul>
     *   <li>'X' en rojo</li>
     *   <li>'O' en verde</li>
     *   <li>'_' sin color</li>
     * </ul>
     *
     * @param tablero matriz 3x3 con el estado actual de la partida.
     * @param rojo código ANSI para el color rojo.
     * @param verde código ANSI para el color verde.
     * @param reset código ANSI para resetear el color.
     */
    public static void mostrarTablero(char[][] tablero, String rojo, String verde, String reset) {

        System.out.println("[TABLERO]");
        for (int y = 0; y < tablero.length; y++) {
            for (int x = 0; x < tablero[y].length; x++) {
                if (tablero[y][x] == 'X'){
                    System.out.printf("%s%-4c%s", rojo, tablero[y][x],reset);
                } else if (tablero[y][x] == 'O') {
                    System.out.printf("%s%-4c%s", verde, tablero[y][x],reset);
                } else {
                    System.out.printf("%-4s", tablero[y][x]);
                }
            }
            System.out.println();
        }
    }
    /**
     * Comprueba si el movimiento que quiere hacer el jugador es válido.
     *
     * <p>Un movimiento es válido si:
     * <ul>
     *   <li>Fila y columna están dentro del rango (0..2)</li>
     *   <li>La casilla está vacía ('_')</li>
     * </ul>
     *
     * @param tablero matriz 3x3 del tablero.
     * @param fila fila seleccionada (0..2).
     * @param columna columna seleccionada (0..2).
     * @return {@code true} si el movimiento es válido; {@code false} en caso contrario.
     */
    public static boolean esMovimientoValido(char[][] tablero, int fila, int columna) {

        if ((fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero[fila].length) && tablero[fila][columna] == '_') {
            return true;
        } else {
            System.out.println("Posicion no valida!");
            return false;
        }
    }
    /**
     * Coloca la ficha del jugador en la posición indicada.
     *
     * @param tablero matriz 3x3 del tablero.
     * @param fila fila donde colocar la ficha (0..2).
     * @param columna columna donde colocar la ficha (0..2).
     * @param ficha array con las fichas de los jugadores (por ejemplo {'X','O'}).
     * @param turnoJugador índice del jugador actual (0 o 1).
     */
    public static void realizarMovimiento(char[][] tablero, int fila, int columna, char[] ficha, int turnoJugador) {

        tablero[fila][columna] = ficha[turnoJugador];
    }
    /**
     * Comprueba si hay un ganador en el tablero.
     *
     * <p>Revisa:
     * <ul>
     *   <li>Todas las filas</li>
     *   <li>Todas las columnas</li>
     *   <li>Las dos diagonales</li>
     * </ul>
     *
     * @param tablero matriz 3x3 del tablero.
     * @return la ficha ganadora ('X' u 'O') si existe; si no hay ganador devuelve ' ' (espacio).
     */
    public static char haGanado(char[][] tablero) {
        char fichaGanadora = ' ';

        //Comprbar filas
        for (int y = 0; y < tablero.length; y++) {
            if (((tablero[y][0] == tablero[y][1]) && (tablero[y][0] == tablero[y][2])) && tablero[y][0] != '_') {
                fichaGanadora = tablero[y][0];
            }
        }

        //Comprbar columnas
        for (int x = 0; x < tablero.length; x++) {
            if (((tablero[0][x] == tablero[1][x]) && (tablero[0][x] == tablero[2][x])) && tablero[0][x] != '_') {
                fichaGanadora = tablero[0][x];
            }
        }

        //Comprobar diagonales
        if (((tablero[0][0] == tablero[1][1]) && (tablero[0][0] == tablero[2][2])) && tablero[0][0] != '_') {
            fichaGanadora = tablero[0][0];
        } else if (((tablero[2][0] == tablero[1][1]) && (tablero[2][0] == tablero[0][2])) && tablero[2][0] != '_') {
            fichaGanadora = tablero[2][0];
        }

        return fichaGanadora;
    }
    /**
     * Comprueba si el tablero está lleno.
     *
     * <p>El tablero está lleno si no queda ninguna casilla con '_'.
     *
     * @param tablero matriz 3x3 del tablero.
     * @return {@code true} si no quedan huecos; {@code false} si aún hay casillas vacías.
     */
    public static boolean tableroLleno(char[][] tablero) {
        boolean lleno = true;

        for (int y = 0; y < tablero.length; y++) {
            for (int x = 0; x < tablero[y].length; x++) {
                if (tablero[y][x] == '_') {
                    lleno = false;
                }
            }
        }
        return lleno;
    }
    /**
     * Cambia el turno del jugador actual.
     *
     * <p>Alterna entre 0 y 1 y muestra por consola a quién le toca.
     *
     * @param nombres array con los nombres de los jugadores.
     * @param turnoJugdor turno actual (0 o 1).
     * @param ficha array con las fichas de los jugadores.
     * @return el nuevo turno (0 o 1).
     */
    public static int cambiarTurno(String[] nombres, int turnoJugdor, char[] ficha) {

        if (turnoJugdor == 0){
            turnoJugdor = 1;
        } else if (turnoJugdor == 1) {
            turnoJugdor = 0;
        }
        System.out.println("Turno de " + nombres[turnoJugdor]+"["+ ficha[turnoJugdor] +"]: ");
        return turnoJugdor;
    }

    /**
     * Almacena la puntuación de una partida en un ranking de 4 posiciones.
     *
     * <p>La puntuación se basa en el número de rondas/turnos:
     * cuanto menos turnos, mejor posición en el ranking.
     *
     * <p>Reglas:
     * <ul>
     *   <li>Si hay hueco (turnos = 0), se guarda directamente</li>
     *   <li>Si está lleno, reemplaza al peor (más turnos) si el nuevo resultado es mejor</li>
     *   <li>Tras guardar, ordena ascendentemente por turnos</li>
     * </ul>
     *
     * @param ganador nombre del jugador ganador.
     * @param rondas número de rondas/turnos empleados en la partida.
     * @param mejores4 array con los nombres de los mejores jugadores (top 4).
     * @param mejores4punt array con los turnos de los mejores resultados (top 4).
     */
    public static void almacenarPuntuacion(String ganador, int rondas, String []mejores4, int [] mejores4punt) {
        boolean almacenado = false;
        int masRondas;
        int posPeor = 0;

        for (int i = 0; i < mejores4punt.length && !almacenado; i++) {
            if (mejores4punt[i] == 0){
                mejores4punt[i] = rondas;
                mejores4[i] = ganador;
                almacenado = true;
            }
        }

        if (!almacenado) {
            masRondas = mejores4punt[0];
            for (int i = 0; i < mejores4punt.length; i++) {
                if (masRondas < mejores4punt[i]) {
                    masRondas = mejores4punt[i];
                    posPeor = i;
                }
            }
            if (rondas < mejores4punt[posPeor]){
                mejores4[posPeor] = ganador;
                mejores4punt[posPeor] = rondas;
                almacenado = true;
            }
        }

        if (almacenado){
            ordenarAscendente(mejores4, mejores4punt);
        }

    }
    /**
     * Ordena dos arrays vinculados de forma ascendente según los turnos.
     *
     * <p>Los arrays se mantienen sincronizados:
     * si se intercambia una puntuación, se intercambia también el nombre asociado.
     *
     * <p>Los valores 0 se consideran "huecos" y se empujan al final.
     *
     * @param mejores4 array de nombres.
     * @param mejores4punt array de turnos (puntuaciones).
     */
    public static void ordenarAscendente(String []mejores4, int [] mejores4punt) {
        int punt;
        String nombre;

        for (int i = 0; i < mejores4punt.length - 1; i++) {
            for (int j = 0; j < mejores4punt.length - 1; j++) {
                if ((mejores4punt[j] > mejores4punt[j+1]) && mejores4punt[j+1] != 0
                        || (mejores4punt[j] == 0 && mejores4punt[j+1] != 0) ) {

                    punt = mejores4punt[j];
                    nombre = mejores4[j];

                    mejores4punt[j] = mejores4punt[j+1];
                    mejores4[j] = mejores4[j+1];

                    mejores4punt[j+1] = punt;
                    mejores4[j+1] = nombre;
                }
            }
        }
    }
    /**
     * Muestra el ranking de puntuaciones por consola.
     *
     * <p>Imprime una tabla con:
     * <ul>
     *   <li>Nombre del jugador</li>
     *   <li>Número de turnos</li>
     * </ul>
     *
     * @param mejoresJugadores array con los nombres del ranking (top 4).
     * @param mejoresTurnos array con los turnos del ranking (top 4).
     */
    public static void mostrarPuntuaciones(String[] mejoresJugadores, int[] mejoresTurnos) {

        System.out.println("[RANKING DE PUNTUACIONES]");
        System.out.printf(FONDO_AZUL + ROJO +
                        "%-15s | %-10s%n" + RESET,
                "JUGADOR", "TURNOS");

        System.out.println("---------------------------");

        for (int i = 0; i < mejoresJugadores.length; i++) {
            System.out.printf(VERDE +
                            "%-15s | %-10d%n" + RESET,
                    mejoresJugadores[i],
                    mejoresTurnos[i]);
        }
    }
}
