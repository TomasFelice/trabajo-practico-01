package PGM.tpInvestigacion;

import java.io.IOException;
import PGM.archivos.ArchivoPgm;

public class tpInvestigacion {
    private static final String INPUT_DIRECTORY = "data/";
    private static final String OUTPUT_DIRECTORY = "output/";

    public static void main(String[] args) {

        try {
            // Valores arbitrarios para test
            int dx = 1;
            int dy = 2;
            int valorFijo = 0;

            // Procesamiento P2
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("-----------------------------Procesamiento archivo p2.pgm-----------------------------");
            System.out.println("--------------------------------------------------------------------------------------");

            String archivoOrigenP2 = INPUT_DIRECTORY + "p2.pgm";
            System.out.println("Leyendo archivo: " + archivoOrigenP2);
            ArchivoPgm PGMOrigenP2 = ArchivoPgm.read(archivoOrigenP2);
            System.out.println("Aplicando desplazamiento: dx=" + dx + ", dy=" + dy + ", valorFijo=" + valorFijo);

            long inicio = System.nanoTime();
            ArchivoPgm matrizDesplazadaP2 = PGMOrigenP2.shifted(dx, dy, valorFijo);
            long fin = System.nanoTime();
            System.out.println("Tiempo de desplazamiento P2: " + (fin - inicio) + " ns");

            String nombreNuevoArchivoP2 = "NuevoPGMP2.pgm";
            String archivoDestinoP2 = OUTPUT_DIRECTORY + nombreNuevoArchivoP2;
            System.out.println("Escribiendo archivo: " + archivoDestinoP2);
            matrizDesplazadaP2.write(archivoDestinoP2);
            System.out.println("Archivo " + nombreNuevoArchivoP2 + " Generado en " + OUTPUT_DIRECTORY);
            System.out.println("Matriz original -> " + PGMOrigenP2);
            System.out.println("Matriz desplazada -> " + matrizDesplazadaP2);

            // Procesamiento P5
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("-----------------------------Procesamiento archivo p5.pgm-----------------------------");
            System.out.println("--------------------------------------------------------------------------------------");

            String archivoOrigenP5 = INPUT_DIRECTORY + "p5.pgm";
            System.out.println("Leyendo archivo: " + archivoOrigenP5);
            ArchivoPgm PGMOrigenP5 = ArchivoPgm.read(archivoOrigenP5);
            System.out.println("Aplicando desplazamiento: dx=" + dx + ", dy=" + dy + ", valorFijo=" + valorFijo);

            inicio = System.nanoTime();
            ArchivoPgm matrizDesplazadaP5 = PGMOrigenP5.shifted(dx, dy, valorFijo);
            fin = System.nanoTime();
            System.out.println("Tiempo de desplazamiento P5: " + (fin - inicio) + " ns");

            String nombreNuevoArchivoP5 = "NuevoPGMP5.pgm";
            String archivoDestinoP5 = OUTPUT_DIRECTORY + nombreNuevoArchivoP5;
            System.out.println("Escribiendo archivo: " + archivoDestinoP5);
            matrizDesplazadaP5.write(archivoDestinoP5);
            System.out.println("Archivo " + nombreNuevoArchivoP5 + " Generado en " + OUTPUT_DIRECTORY);
            System.out.println("Matriz original -> " + PGMOrigenP5);
            System.out.println("Matriz desplazada -> " + matrizDesplazadaP5);

        } catch (IOException e) {
            System.err.println("Ocurrió un error de E/S al procesar los archivos PGM:");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el formato del archivo PGM o parámetros inválidos:");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error de índice al acceder a píxeles:");
            e.printStackTrace();
        } catch (Exception e) { // Capturar otras posibles excepciones
            System.err.println("Ocurrió un error inesperado:");
            e.printStackTrace();
        }
    }
}