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

            System.out.println("Desplazamientos a realizar: dx=" + dx + ", dy=" + dy + ", valorFijo=" + valorFijo + "\n");

            String[] fileNamesP2 = new String[]{"p2_sm.pgm", "p2_md.pgm", "p2_lg.pgm"};
            procesarArchivos("p2", fileNamesP2, dx, dy, valorFijo);

            System.out.println("\n\n");

            String[] fileNamesP5 = new String[]{"p5_sm.pgm", "p5_md.pgm", "p5_lg.pgm"};
            procesarArchivos("p5", fileNamesP5, dx, dy, valorFijo);

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

    private static void procesarArchivos (String tipoArchivo, String[]fileNames,int dx, int dy, int valorFijo) throws IOException, IllegalArgumentException, IndexOutOfBoundsException, Exception{
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("---------------------------------Análisis archivos " + tipoArchivo + "---------------------------------");
        System.out.println("--------------------------------------------------------------------------------------\n");

        for (String fileName : fileNames) {
            String archivoOrigen = INPUT_DIRECTORY + fileName;
            System.out.println("\n** Leyendo archivo: " + archivoOrigen + " **");

            ArchivoPgm PGMOrigen = ArchivoPgm.read(archivoOrigen);

            long inicio = System.nanoTime();
            ArchivoPgm matrizDesplazada = PGMOrigen.shifted(dx, dy, valorFijo);
            long fin = System.nanoTime();
            System.out.println("- Tiempo de desplazamiento: " + (fin - inicio) + " ns");

            String nombreNuevoArchivo = fileName.replace(".pgm", "_shifted.pgm");
            String archivoDestino = OUTPUT_DIRECTORY + nombreNuevoArchivo;

            matrizDesplazada.write(archivoDestino);

            System.out.println("- Archivo " + nombreNuevoArchivo + " Generado en " + OUTPUT_DIRECTORY);
            System.out.println("- Matriz original -> " + PGMOrigen);
            System.out.println("- Matriz desplazada -> " + matrizDesplazada);
        }
    }
}