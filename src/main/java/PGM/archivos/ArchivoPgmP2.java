package PGM.archivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;   // ❶ Aseguramos ASCII
import java.util.Scanner;

public class ArchivoPgmP2 extends ArchivoPgm {

    private static final String MAGIC_NUMBER = "P2";

    public ArchivoPgmP2(int alto, int ancho, int valorMax) {
        super( MAGIC_NUMBER, alto, ancho, valorMax);
    }

    // --- Implementación de métodos abstractos ---

    @Override
    protected void readPixelData(BufferedInputStream is) throws IOException {
        // ❷ Forzamos ASCII y añadimos un delimitador que ignora comentarios (# …)
        Scanner pixelScanner = new Scanner(is, StandardCharsets.US_ASCII);
        pixelScanner.useDelimiter("(\\s+|#.*\\R)+");   // blancos  o  comentarios

        this.matriz = new int[alto][ancho];
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (!pixelScanner.hasNextInt()) {
                    throw new IOException(String.format(
                            "Datos de píxel faltantes en (%d,%d)", i, j));
                }
                matriz[i][j] = pixelScanner.nextInt();
            }
        }
        // No cerramos el Scanner: el BufferedInputStream se cierra fuera.
    }

    @Override
    protected void writePixelData(BufferedOutputStream os) {
        // Para P2, escribimos los píxeles como enteros ASCII
        PrintWriter pixelWriter = new PrintWriter(os);

        for (int i = 0; i < this.alto; ++i) {
            for (int j = 0; j < this.ancho; ++j) {
                pixelWriter.print(this.matriz[i][j] + " ");
            }
            pixelWriter.println();
        }
        pixelWriter.flush(); // Aseguramos que se escriba en el BufferedOutputStream por completo
        // NO cerramos este PrintWriter, el BufferedOutputStream se cierra en el metodo write() de AbstractPGM.
    }

    // --- Implementación del metodo de fábrica para crear una nueva instancia P2 ---
    @Override
    protected ArchivoPgm createNewInstance(int alto, int ancho, int valorMax) {
        return new ArchivoPgmP2(alto, ancho, valorMax);
    }
}