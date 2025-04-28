package PGM.tpInvestigacion;
import java.io.IOException;
import PGM.archivos.ArchivoPGM;

public class tpInvestigacion {

    public static void main(String[] args) {

        try {

            int dx = 2;
            int dy = 1;
            int valorFijo = 0;

            //P2
            String archivoOrigenP2 = "C:\\Users\\Ignacio Nogueira\\Pictures\\prograAvanzada\\P2.pgm";
            ArchivoPGM PGMOrigenP2 = ArchivoPGM.leerPGM(archivoOrigenP2);

            ArchivoPGM matrizDesplazadaP2 = ArchivoPGM.desplazarImagen(PGMOrigenP2, dx, dy, valorFijo);

            String nombreNuevoArchivoP2 = "NuevoPGMP2.pgm"; // le agregué la extensión para que sea un .pgm válido
            String archivoDestinoP2 = "C:\\Users\\Ignacio Nogueira\\Pictures\\prograAvanzada\\" + nombreNuevoArchivoP2;

            ArchivoPGM.escribirPGM(archivoDestinoP2, matrizDesplazadaP2);

            System.out.println("Archivo " +nombreNuevoArchivoP2 + "Generado");

            //P5
            String archivoOrigenP5 = "C:\\Users\\Ignacio Nogueira\\Pictures\\prograAvanzada\\P5.pgm";
            ArchivoPGM PGMOrigenP5 = ArchivoPGM.leerPGM(archivoOrigenP5);

            ArchivoPGM matrizDesplazadaP5 = ArchivoPGM.desplazarImagen(PGMOrigenP5, dx, dy, valorFijo);

            String nombreNuevoArchivoP5 = "NuevoPGMP5.pgm"; // le agregué la extensión para que sea un .pgm válido
            String archivoDestinoP5 = "C:\\Users\\Ignacio Nogueira\\Pictures\\prograAvanzada\\" + nombreNuevoArchivoP5;

            ArchivoPGM.escribirPGM(archivoDestinoP5, matrizDesplazadaP5);

            System.out.println("Archivo " +nombreNuevoArchivoP5 + "Generado");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}