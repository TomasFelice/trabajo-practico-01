package PGM.archivos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchivoPgmTest {

    @Test
    void constructor_ValoresValidos_DeberiaCrearInstancia() {
        ArchivoPgm pgmp2 = new ArchivoPgmP2(4, 5, 100);

        assertEquals("P2", pgmp2.getNroMagico());
        assertEquals(4, pgmp2.getAlto());
        assertEquals(5, pgmp2.getAncho());
        assertEquals(100, pgmp2.getValorMax());
        assertNotNull(pgmp2.getMatriz());
        assertEquals(4, pgmp2.getMatriz().length);
        assertEquals(5, pgmp2.getMatriz()[0].length);

        ArchivoPgm pgmp5 = new ArchivoPgmP5(4, 5, 100);

        assertEquals("P5", pgmp2.getNroMagico());
        assertEquals(4, pgmp2.getAlto());
        assertEquals(5, pgmp2.getAncho());
        assertEquals(100, pgmp2.getValorMax());
        assertNotNull(pgmp2.getMatriz());
        assertEquals(4, pgmp2.getMatriz().length);
        assertEquals(5, pgmp2.getMatriz()[0].length);

    }

    @Test
    void constructor_ValorMaxFueraDeRango_DeberiaLanzarExcepcion() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2(4, 4, 300) {}
        );
        assertTrue(ex.getMessage().contains("El valor máximo debe estar entre 0 y 255"));
    }

    @Test
    void constructor_DimensionesInvalidas_DeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2( 0, 4, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgmP2(4, -1, 100) {}
        );
    }

    /* seguir
    @Test
    void constructor_NroMagicoNuloOInvalido_DeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgm(null, 4, 4, 100) {}
        );
        assertThrows(IllegalArgumentException.class, () ->
                new ArchivoPgm("", 4, 4, 100) {}
        );
    }

    @Test
    void matriz_InicializacionCorrecta_DeberiaEstarLlenaDeCeros() {
        ArchivoPgm pgm = new ArchivoPgm("P2", 2, 3, 100) {};
        int[][] matriz = pgm.getMatriz();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, matriz[i][j]);
            }
        }
    }*/
}
