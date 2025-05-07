package PGM.archivos;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchivoPgmP5Test {

    @Test
    void construccionPgmP5() {
        ArchivoPgmP5 pgm = new ArchivoPgmP5(2, 3, 255);

        pgm.setPixel(0, 0, 100);
        pgm.setPixel(1, 2, 200);

        assertEquals(100, pgm.getPixel(0, 0));
        assertEquals(200, pgm.getPixel(1, 2));
    }

    @Test
    void lanzaErrorSiFaltanPixeles() {
        // Una imagen de 2x2 con valorMax=255 necesita 4 bytes
        // Aquí damos solo 3 → debe lanzar IOException

        byte[] inputIncompleto = new byte[] {10, 20, 30}; // falta 1 byte

        ByteArrayInputStream bais = new ByteArrayInputStream(inputIncompleto);
        BufferedInputStream bis = new BufferedInputStream(bais);

        ArchivoPgmP5 pgm = new ArchivoPgmP5(2, 2, 255);

        IOException ex = assertThrows(IOException.class, () -> {
            pgm.readPixelData(bis);
        });
    }
    
    

    @Test
    void escribeArchivoGrandeP5Correctamente() throws IOException {
        // Valores grandes para probar límites
        final int LARGE_WIDTH = 10000;
        final int LARGE_HEIGHT = 10000;
        final int MAX_VALUE = 255;

        // Crear imagen grande
        ArchivoPgmP5 pgm = new ArchivoPgmP5(LARGE_WIDTH, LARGE_HEIGHT, MAX_VALUE);

        // Establecer algunos valores de prueba
        pgm.setPixel(0, 0, 100);
        pgm.setPixel(LARGE_WIDTH - 1, LARGE_HEIGHT - 1, 200);

        // Crear archivo temporal
        File tempFile = File.createTempFile("testP5Large", ".pgm");
        tempFile.deleteOnExit();

        // Escribir y leer el archivo
        pgm.write(tempFile.getAbsolutePath());
        ArchivoPgm leido = ArchivoPgm.read(tempFile.getAbsolutePath());

        // Verificar que la imagen leída coincide con la original
        assertEquals(pgm.getPixel(0, 0), leido.getPixel(0, 0));
        assertEquals(pgm.getPixel(LARGE_WIDTH - 1, LARGE_HEIGHT - 1),
                leido.getPixel(LARGE_WIDTH - 1, LARGE_HEIGHT - 1));
        assertEquals(pgm, leido);
    }

    @Test
    void escribeArchivoP5DesplazadoCorrectamente() throws IOException {
        // Crear imagen original
        ArchivoPgmP5 pgm = new ArchivoPgmP5(5, 5, 255);

        // Establecer algunos valores de prueba
        pgm.setPixel(0, 0, 100);
        pgm.setPixel(2, 2, 150);
        pgm.setPixel(4, 4, 200);

        // Desplazar imagen
        int dx = 2;
        int dy = 1;
        int valorFijo = 50;
        ArchivoPgm desplazada = pgm.shifted(dx, dy, valorFijo);

        // Crear archivo temporal
        File tempFile = File.createTempFile("testP5Shifted", ".pgm");
        tempFile.deleteOnExit();

        // Escribir y leer el archivo desplazado
        desplazada.write(tempFile.getAbsolutePath());
        ArchivoPgm leida = ArchivoPgm.read(tempFile.getAbsolutePath());

        // Verificar que la imagen leída coincide con la desplazada
        assertEquals(valorFijo, leida.getPixel(0, 0)); // Nuevo espacio rellenado con valorFijo
        assertEquals(100, leida.getPixel(1, 2)); // Pixel original desplazado
        assertEquals(150, leida.getPixel(3, 4)); // Pixel original desplazado
        assertEquals(desplazada, leida);
    }
}
