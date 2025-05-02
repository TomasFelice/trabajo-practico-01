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
    void escribeArchivoP5Correctamente() throws IOException {

    }

    @Test
    void escribeArchivoP5DesplazadoCorrectamente() throws IOException {
    }
}
