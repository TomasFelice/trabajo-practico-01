package PGM.archivos;

import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    @Test
    void setYGetPixelDebeSetearYObtenerCorrectamente() {
        ArchivoPgm pgmP2 = new ArchivoPgmP2(3, 3, 255);

        pgmP2.setPixel(1, 1, 123);
        pgmP2.setPixel(0, 2, 200);

        assertEquals(123, pgmP2.getPixel(1, 1));
        assertEquals(200, pgmP2.getPixel(0, 2));

        ArchivoPgm pgmP5 = new ArchivoPgmP5(3, 3, 255);

        pgmP5.setPixel(1, 1, 123);
        pgmP5.setPixel(0, 2, 200);

        assertEquals(123, pgmP5.getPixel(1, 1));
        assertEquals(200, pgmP5.getPixel(0, 2));
    }

    @Test
    void setPixelValorInvalidoExcepcion() {
        ArchivoPgm pgmP2 = new ArchivoPgmP2(3, 3, 100);

        assertThrows(IllegalArgumentException.class, () -> pgmP2.setPixel(0, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> pgmP2.setPixel(0, 0, 101));

        ArchivoPgm pgmP5 = new ArchivoPgmP5(3, 3, 100);

        assertThrows(IllegalArgumentException.class, () -> pgmP5.setPixel(0, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> pgmP5.setPixel(0, 0, 101));
    }

    @Test
    void accesoMatrizFueraDeLimitesExcepcion() {
        ArchivoPgm pgmP2 = new ArchivoPgmP2(3, 3, 100);

        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.setPixel(3, 0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.setPixel(0, 3, 10));

        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.getPixel(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.getPixel(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.getPixel(0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.getPixel(10, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP2.getPixel(10, 0));


        ArchivoPgm pgmP5 = new ArchivoPgmP5(3, 3, 100);

        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.setPixel(3, 0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.setPixel(0, 3, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.getPixel(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.getPixel(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.getPixel(0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.getPixel(10, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> pgmP5.getPixel(10, 0));
    }

    @Test
    void readArchivoformatoErroneoMagicNumberoExcepcion() {
        String archivoErroneoMN = "data/formatoErroneoMagicNumber.pgm"; //para documentos que no tengan
        assertThrows(IllegalArgumentException.class, () -> ArchivoPgm.read(archivoErroneoMN));
    }

    @Test
    void readArchivoErroneoDimensionExcepcion() {
        String archivoErroneoDimension = "data/archivoSinDimension.pgm";
        assertThrows(IOException.class, () -> ArchivoPgm.read(archivoErroneoDimension));
    }

    @Test
    void readArchivoarchivoErroneoValMaxExcepcion() {
        String archivoErroneoValMax = "data/archivoErrorValMax.pgm";
        assertThrows(NumberFormatException.class, () -> ArchivoPgm.read(archivoErroneoValMax));
    }

    //lo de directorio hace falta hacer el test?

    @Test
    void shiftedConOffsetCeroDevuelveMismaInstancia() {
        ArchivoPgm p2 = new ArchivoPgmP2(2,2,255);
        ArchivoPgm result = p2.shifted(0,0, 42);
        assertSame(p2, result, "Cuando dx=0 y dy=0 debe retornar la misma instancia");
    }

    @Test
    void shiftedP2RellenaBordeYDesplazaCorrectamente() {
        // Creamos un 3×3 con valores conocidos
        ArchivoPgm p2 = new ArchivoPgmP2(3,3,255);

        //Original:
        // 1 2 3
        // 4 5 6
        // 7 8 9

        int v = 1;
        for(int y=0; y<3; y++){
            for(int x=0; x<3; x++){
                p2.setPixel(y,x, v++);
            }
        }

        // Desplazamos +1 en X y +1 en Y, borde en 0
        ArchivoPgm sh = p2.shifted(1,1, 0);

        //Quedaría:
        // 0 0 0
        // 0 1 2
        // 0 4 5

        assertEquals(0, sh.getPixel(0,0));
        assertEquals(1, sh.getPixel(1,1));
        assertEquals(5, sh.getPixel(2,2));
    }

    @Test
    void shiftedP5RellenaBordeYDesplazaCorrectamente() {

        ArchivoPgm p5 = new ArchivoPgmP5(3,3,255);
        //Original:
        // 1 2 3
        // 4 5 6
        // 7 8 9

        int v=1;
        for(int y=0;y<3;y++) {
            for (int x = 0; x < 3; x++) {
                p5.setPixel(y, x, v++);
            }
        }
        ArchivoPgm sh5 = p5.shifted(-1, 1, 7); // desplaza X izquierda, Y abajo, borde=7

        //Quedaría:
        // 7 7 7
        // 2 3 7
        // 5 6 7

        assertEquals(7, sh5.getPixel(0,0));
        assertEquals(3, sh5.getPixel(1,1));
        assertEquals(7, sh5.getPixel(2,2));
    }
}
