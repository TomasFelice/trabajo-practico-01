package PGM.archivos;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchivoPgmTest {

    // --- Constantes para los tests de shifted ---
    private static final int ALTO_TEST = 3;
    private static final int ANCHO_TEST = 3;
    private static final int VALOR_MAX_TEST = 255;
    private static final int VALOR_RELLENO_P2 = 0;
    private static final int VALOR_RELLENO_P5_CASO1 = 7; // Usado en el test original renombrado
    private static final int VALOR_RELLENO_P5_OTROS = 42;


    // --- Helper para llenar la imagen de prueba con un patrón secuencial ---
    private void llenarImagenConPatronSecuencial(ArchivoPgm pgm) {
        int valorPixel = 1;
        for (int y = 0; y < pgm.getAlto(); y++) {
            for (int x = 0; x < pgm.getAncho(); x++) {
                pgm.setPixel(y, x, valorPixel++);
            }
        }
    }

    // --- Helper para crear y llenar una imagen esperada ---
    private ArchivoPgm crearImagenEsperada(String tipo, int[][] datosPixel) {
        ArchivoPgm esperado;
        if ("P2".equals(tipo)) {
            esperado = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        } else {
            esperado = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        }
        for (int y = 0; y < ALTO_TEST; y++) {
            for (int x = 0; x < ANCHO_TEST; x++) {
                esperado.setPixel(y, x, datosPixel[y][x]);
            }
        }
        return esperado;
    }


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

        ArchivoPgm pgm5 = new ArchivoPgmP2(2, 3, 100) {}; // Debería ser ArchivoPgmP5
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


    @Test
    void shiftedConOffsetCeroDevuelveMismaInstancia() {
        ArchivoPgm p2 = new ArchivoPgmP2(2,2,255);
        ArchivoPgm result = p2.shifted(0,0, 42);
        assertSame(p2, result, "Cuando dx=0 y dy=0 debe retornar la misma instancia");

        ArchivoPgm p5 = new ArchivoPgmP5(2,2,255);
        ArchivoPgm resultP5 = p5.shifted(0,0, 42);
        assertSame(p5, resultP5, "Cuando dx=0 y dy=0 debe retornar la misma instancia para P5");
    }

    // --- Tests para P2 shifted ---

    @Test
    void testShiftedP2_dxPos_dyPos_RellenaCorrectamente() { // Anteriormente shiftedP2RellenaBordeYDesplazaCorrectamente
        ArchivoPgm p2Original = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p2Original);

        int dx = 1, dy = 1;
        ArchivoPgm resultado = p2Original.shifted(dx, dy, VALOR_RELLENO_P2);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P2, VALOR_RELLENO_P2, VALOR_RELLENO_P2},
                {VALOR_RELLENO_P2, 1, 2},
                {VALOR_RELLENO_P2, 4, 5}
        };
        ArchivoPgm esperado = crearImagenEsperada("P2", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP2_dxPos_dyNeg_RellenaCorrectamente() {
        ArchivoPgm p2Original = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p2Original);

        int dx = 1, dy = -1;
        ArchivoPgm resultado = p2Original.shifted(dx, dy, VALOR_RELLENO_P2);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P2, 4, 5},
                {VALOR_RELLENO_P2, 7, 8},
                {VALOR_RELLENO_P2, VALOR_RELLENO_P2, VALOR_RELLENO_P2}
        };
        ArchivoPgm esperado = crearImagenEsperada("P2", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP2_dxNeg_dyPos_RellenaCorrectamente() {
        ArchivoPgm p2Original = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p2Original);

        int dx = -1, dy = 1;
        ArchivoPgm resultado = p2Original.shifted(dx, dy, VALOR_RELLENO_P2);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P2, VALOR_RELLENO_P2, VALOR_RELLENO_P2},
                {2, 3, VALOR_RELLENO_P2},
                {5, 6, VALOR_RELLENO_P2}
        };
        ArchivoPgm esperado = crearImagenEsperada("P2", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP2_dxNeg_dyNeg_RellenaCorrectamente() {
        ArchivoPgm p2Original = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p2Original);

        int dx = -1, dy = -1;
        ArchivoPgm resultado = p2Original.shifted(dx, dy, VALOR_RELLENO_P2);

        int[][] datosEsperados = {
                {5, 6, VALOR_RELLENO_P2},
                {8, 9, VALOR_RELLENO_P2},
                {VALOR_RELLENO_P2, VALOR_RELLENO_P2, VALOR_RELLENO_P2}
        };
        ArchivoPgm esperado = crearImagenEsperada("P2", datosEsperados);
        assertEquals(esperado, resultado);
    }

    // --- Tests para P5 shifted ---

    @Test
    void testShiftedP5_dxNeg_dyPos_RellenaCorrectamente() { // Anteriormente shiftedP5RellenaBordeYDesplazaCorrectamente
        ArchivoPgm p5Original = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p5Original);
        
        int dx = -1, dy = 1;
        // Usamos VALOR_RELLENO_P5_CASO1 para mantener la consistencia con el test original
        ArchivoPgm resultado = p5Original.shifted(dx, dy, VALOR_RELLENO_P5_CASO1);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P5_CASO1, VALOR_RELLENO_P5_CASO1, VALOR_RELLENO_P5_CASO1},
                {2, 3, VALOR_RELLENO_P5_CASO1},
                {5, 6, VALOR_RELLENO_P5_CASO1}
        };
        ArchivoPgm esperado = crearImagenEsperada("P5", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP5_dxPos_dyPos_RellenaCorrectamente() {
        ArchivoPgm p5Original = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p5Original);

        int dx = 1, dy = 1;
        ArchivoPgm resultado = p5Original.shifted(dx, dy, VALOR_RELLENO_P5_OTROS);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS},
                {VALOR_RELLENO_P5_OTROS, 1, 2},
                {VALOR_RELLENO_P5_OTROS, 4, 5}
        };
        ArchivoPgm esperado = crearImagenEsperada("P5", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP5_dxPos_dyNeg_RellenaCorrectamente() {
        ArchivoPgm p5Original = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p5Original);

        int dx = 1, dy = -1;
        ArchivoPgm resultado = p5Original.shifted(dx, dy, VALOR_RELLENO_P5_OTROS);

        int[][] datosEsperados = {
                {VALOR_RELLENO_P5_OTROS, 4, 5},
                {VALOR_RELLENO_P5_OTROS, 7, 8},
                {VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS}
        };
        ArchivoPgm esperado = crearImagenEsperada("P5", datosEsperados);
        assertEquals(esperado, resultado);
    }

    @Test
    void testShiftedP5_dxNeg_dyNeg_RellenaCorrectamente() {
        ArchivoPgm p5Original = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p5Original);

        int dx = -1, dy = -1;
        ArchivoPgm resultado = p5Original.shifted(dx, dy, VALOR_RELLENO_P5_OTROS);

        int[][] datosEsperados = {
                {5, 6, VALOR_RELLENO_P5_OTROS},
                {8, 9, VALOR_RELLENO_P5_OTROS},
                {VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS, VALOR_RELLENO_P5_OTROS}
        };
        ArchivoPgm esperado = crearImagenEsperada("P5", datosEsperados);
        assertEquals(esperado, resultado);
    }

    // --- Test adicional para desplazamiento total ---
    @Test
    void testShiftedP2_DesplazamientoTotalRellenaCompletamente() {
        ArchivoPgm p2Original = new ArchivoPgmP2(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p2Original);

        int dx = ALTO_TEST + 1; // Desplazamiento mayor que la altura
        int dy = ANCHO_TEST + 1; // Desplazamiento mayor que el ancho
        int valorRelleno = 77;
        ArchivoPgm resultado = p2Original.shifted(dx, dy, valorRelleno);

        int[][] datosEsperados = new int[ALTO_TEST][ANCHO_TEST];
        for (int y = 0; y < ALTO_TEST; y++) {
            for (int x = 0; x < ANCHO_TEST; x++) {
                datosEsperados[y][x] = valorRelleno;
            }
        }
        ArchivoPgm esperado = crearImagenEsperada("P2", datosEsperados);
        assertEquals(esperado, resultado);
    }
     @Test
    void testShiftedP5_DesplazamientoTotalRellenaCompletamente() {
        ArchivoPgm p5Original = new ArchivoPgmP5(ALTO_TEST, ANCHO_TEST, VALOR_MAX_TEST);
        llenarImagenConPatronSecuencial(p5Original);

        int dx = ALTO_TEST + 1; // Desplazamiento mayor que la altura
        int dy = ANCHO_TEST + 1; // Desplazamiento mayor que el ancho
        int valorRelleno = 88;
        ArchivoPgm resultado = p5Original.shifted(dx, dy, valorRelleno);

        int[][] datosEsperados = new int[ALTO_TEST][ANCHO_TEST];
        for (int y = 0; y < ALTO_TEST; y++) {
            for (int x = 0; x < ANCHO_TEST; x++) {
                datosEsperados[y][x] = valorRelleno;
            }
        }
        ArchivoPgm esperado = crearImagenEsperada("P5", datosEsperados);
        assertEquals(esperado, resultado);
    }
}