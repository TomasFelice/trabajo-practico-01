package PGM.archivos;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ArchivoPgmP2Test {

    @Test
    void construccionPgmP2() {
        ArchivoPgmP2 pgm = new ArchivoPgmP2(2, 3, 255);

        pgm.setPixel(0, 0, 100);
        pgm.setPixel(1, 2, 200);

        assertEquals(100, pgm.getPixel(0, 0));
        assertEquals(200, pgm.getPixel(1, 2));
    }

    @Test
    void lanzaErrorSiFaltanPixeles() {
        String input1 = "1 2\n3"; // falta un valor
        ByteArrayInputStream bais1 = new ByteArrayInputStream(input1.getBytes(StandardCharsets.US_ASCII));
        BufferedInputStream bis1 = new BufferedInputStream(bais1);

        ArchivoPgmP2 pgm1 = new ArchivoPgmP2(2, 2, 255);

        assertThrows(IOException.class, () -> {
            pgm1.readPixelData(bis1);
        });

        String input2 = "1\n2\n3"; // falta un valor
        ByteArrayInputStream bais2 = new ByteArrayInputStream(input1.getBytes(StandardCharsets.US_ASCII));
        BufferedInputStream bis2 = new BufferedInputStream(bais2);

        ArchivoPgmP2 pgm2 = new ArchivoPgmP2(2, 2, 255);

        assertThrows(IOException.class, () -> {
            pgm2.readPixelData(bis2);
        });
    }

    @Test
    void escribeArchivoP2Correctamente() throws IOException {
        // Arrange: crear un archivo P2 con datos conocidos
        ArchivoPgm original = new ArchivoPgmP2(3, 3, 255);
        int valor = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                original.setPixel(y, x, valor++);
            }
        }

        // Crear directorio temporal
        File tempFile = File.createTempFile("testP2", ".pgm");
        tempFile.deleteOnExit(); // borrar archivo al terminar los tests

        // Act: escribir y luego leer el archivo
        original.write(tempFile.getAbsolutePath());
        ArchivoPgm leido = ArchivoPgm.read(tempFile.getAbsolutePath());

        // Assert: el archivo leído debe ser igual al original
        assertEquals(original, leido);
    }

    @Test
    void escribeArchivoP2DesplazadoCorrectamente() throws IOException {
        File archivoTemp = File.createTempFile("pgmTest", ".pgm");
        archivoTemp.deleteOnExit();

        ArchivoPgm original = new ArchivoPgmP2(3, 3, 255);
        int valor = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                original.setPixel(i, j, valor++);
            }
        }

        ArchivoPgm desplazada = original.shifted(1, 1, 0);
        desplazada.write(archivoTemp.getAbsolutePath());

        ArchivoPgm leida = ArchivoPgm.read(archivoTemp.getAbsolutePath());

        // Verificamos que la lectura coincide con lo que escribimos
        assertEquals(desplazada, leida);
    }
}

