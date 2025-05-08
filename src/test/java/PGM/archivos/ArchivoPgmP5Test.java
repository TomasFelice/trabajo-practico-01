package PGM.archivos;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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

        assertThrows(IOException.class, () -> pgm.readPixelData(bis));
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
    @Test
    void readPixelData_ConValorMaxMayor255_LeeCorrectamente() throws IOException {
        ArchivoPgmP5 pgm = new ArchivoPgmP5(1, 2, 60000); // 1 fila, 2 columnas, valorMax > 255

        // Datos binarios para dos píxeles de 2 bytes (big-endian)
        // Píxel 1: valor 258 (0x0102)
        // Píxel 2: valor 515 (0x0203)
        byte[] pixelData = new byte[] {
            (byte)0x01, (byte)0x02, // Píxel (0,0)
            (byte)0x02, (byte)0x03  // Píxel (0,1)
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(pixelData);
        BufferedInputStream bis = new BufferedInputStream(bais);

        // Simular que ya se leyó el encabezado y el salto de línea si es necesario
        // (la lógica del salto de línea después de valorMax ya está en readPixelData)
        
        pgm.readPixelData(bis);

        assertEquals(258, pgm.getPixel(0, 0));
        assertEquals(515, pgm.getPixel(0, 1));
    }

    @Test
    void writeAndRead_ConValorMaxMayor255_Correctamente() throws IOException {
        final int ALTO = 2;
        final int ANCHO = 2;
        final int VALOR_MAX_GRANDE = 30000;

        ArchivoPgmP5 original = new ArchivoPgmP5(ALTO, ANCHO, VALOR_MAX_GRANDE);
        original.setPixel(0, 0, 256);      // 0x0100
        original.setPixel(0, 1, 5000);     // 0x1388
        original.setPixel(1, 0, VALOR_MAX_GRANDE); // Max value
        original.setPixel(1, 1, 0);        // Min value

        File tempFile = File.createTempFile("testP5_2bytes", ".pgm");
        tempFile.deleteOnExit();

        original.write(tempFile.getAbsolutePath());
        ArchivoPgm leido = ArchivoPgm.read(tempFile.getAbsolutePath());

        assertInstanceOf(ArchivoPgmP5.class, leido, "El archivo leído debe ser P5");
        assertEquals(VALOR_MAX_GRANDE, leido.getValorMax());
        assertEquals(original.getPixel(0,0), leido.getPixel(0,0));
        assertEquals(original.getPixel(0,1), leido.getPixel(0,1));
        assertEquals(original.getPixel(1,0), leido.getPixel(1,0));
        assertEquals(original.getPixel(1,1), leido.getPixel(1,1));
        assertEquals(original, leido);
    }
}