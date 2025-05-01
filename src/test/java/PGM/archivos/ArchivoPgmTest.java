package PGM.archivos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class ArchivoPgmTest {

    @Test
    void ValoresValidosCrearInstancia() {
        ArchivoPgm pgmp2 = new ArchivoPgmP2(4, 5, 100);
        assertEquals("P2", pgmp2.getNroMagico());
        assertEquals(4, pgmp2.getAlto());
        assertEquals(5, pgmp2.getAncho());
        assertEquals(100, pgmp2.getValorMax());
        assertNotNull(pgmp2.getMatriz());
        assertEquals(4, pgmp2.getMatriz().length);
        assertEquals(5, pgmp2.getMatriz()[0].length);

        ArchivoPgm pgmp5 = new ArchivoPgmP5(4, 5, 100);

        assertEquals("P5", pgmp5.getNroMagico());
        assertEquals(4, pgmp5.getAlto());
        assertEquals(5, pgmp5.getAncho());
        assertEquals(100, pgmp5.getValorMax());
        assertNotNull(pgmp5.getMatriz());
        assertEquals(4, pgmp5.getMatriz().length);
        assertEquals(5, pgmp5.getMatriz()[0].length);
    }

    @Test
    void DimensionesInvalidasDeberiaLanzarExcepcion() {
        //p2
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2( 0, 4, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2( 0, 0, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2( 4, 0, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2(4, -1, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2(-4, -2, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2(-100, 2, 100) {}
        );

        //p5:

        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5( 0, 4, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5( 0, 0, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5( 4, 0, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5(4, -1, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5(-4, -2, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP5(-100, 2, 100) {}
        );
    }

    @Test
    void matrizInicializacionCorrectaLlenaDeCeros() {
        ArchivoPgm pgm2 = new ArchivoPgmP2(2, 3, 100) {};
        int[][] matrizp2 = pgm2.getMatriz();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, matrizp2[i][j]);
            }
        }

        ArchivoPgm pgm5 = new ArchivoPgmP2(2, 3, 100) {};
        int[][] matrizp5 = pgm5.getMatriz();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, matrizp5[i][j]);
            }
        }
    }


}
