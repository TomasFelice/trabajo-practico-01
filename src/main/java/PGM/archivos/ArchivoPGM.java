package PGM.archivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ArchivoPGM {
    private String nroMagico;
    private int alto;
    private int ancho;
    private int valorMax;
    private int[][] matriz;

    public ArchivoPGM(String nroMagico,int alto,int ancho,int valorMax) {
        this.nroMagico = nroMagico;
        this.alto = alto;
        this.ancho = ancho;
        this.valorMax = valorMax;
        this.matriz = new int[alto][ancho];
    }

    public String getNroMagico() {
        return this.nroMagico;
    }

    public int getAlto() {
        return this.alto;
    }

    public int getAncho() {
        return this.ancho;
    }

    public int getValorMax() {
        return this.valorMax;
    }

    public int[][] getMatriz() {
        return this.matriz;
    }

    public void setMatriz(int fila, int columna, int valor) {
        this.matriz[fila][columna] = valor;
    }

    public static ArchivoPGM leerPGM(String archivoOrigen) throws IOException {
        FileInputStream fis = new FileInputStream(archivoOrigen);
        BufferedInputStream bis = new BufferedInputStream(fis); // Guardo en memoria un pedazo grande del archivo, y
        // después voy leyendo byte por byte de ahí
        Scanner scan = new Scanner(bis);

        // Leer Magic Number
        String magicNumber = scan.next();
        if (!magicNumber.equals("P2") && !magicNumber.equals("P5")) {
            throw new IllegalArgumentException("Formato no soportado: " + magicNumber);
        }

        // Leer y saltar comentarios
        String linea = scan.nextLine();
        while (linea.startsWith("#") || linea.trim().isEmpty()) {
            linea = scan.nextLine();
        }

        // Leer dimensiones
        Scanner dimensionScanner = new Scanner(linea);
        int ancho = dimensionScanner.nextInt();
        int alto = dimensionScanner.nextInt();

        // Leer máximo valor
        int valorMax = scan.nextInt();

        // Hasta acá es igual para P2 y P5

        ArchivoPGM imagen = new ArchivoPGM(magicNumber,alto,ancho,valorMax);

        if (imagen.getNroMagico() == "P2") {
            // Lectura de datos ASCII
            for (int i = 0; i < imagen.getAlto(); ++i) {
                for (int j = 0; j < imagen.getAncho(); ++j) {
                    imagen.setMatriz(i, j, scan.nextInt());
                }
            }
        } else { // P5 (binario)
            // Avanzar el BufferedInputStream hasta donde quedaron los datos
            bis.skip(scan.match().end());

            for (int i = 0; i < imagen.getAlto(); ++i) {
                for (int j = 0; j < imagen.getAncho(); ++j) {
                    int pixel = bis.read();
                    imagen.setMatriz(i, j, pixel);
                }
            }
        }

        scan.close();
        bis.close();
        return imagen;
    }

    public static void escribirPGM(String archivoDestino, ArchivoPGM imagen) throws IOException {
        FileOutputStream fos = new FileOutputStream(archivoDestino);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        PrintWriter pw = new PrintWriter(bos);
        boolean esBinario = imagen.getNroMagico() == "P5";

        if (esBinario) {
            pw.print("P5\n");
        } else {
            pw.print("P2\n");
        }
        pw.print("# Archivo generado | Grupo Alpha-1: Desplazamiento (shift) de imagen \n");
        pw.printf("%d %d\n", imagen.getAncho(), imagen.getAlto());
        pw.println(imagen.getValorMax());
        pw.flush(); // importante para que no se quede en buffer antes del binario

        if (esBinario) {
            for (int i = 0; i < imagen.getAlto(); ++i) {
                for (int j = 0; j < imagen.getAncho(); ++j) {
                    bos.write(imagen.getMatriz()[i][j]);
                }
            }
        } else {
            for (int i = 0; i < imagen.getAlto(); ++i) {
                for (int j = 0; j < imagen.getAncho(); ++j) {
                    pw.print(imagen.getMatriz()[i][j] + " ");
                }
                pw.println();
            }
        }
        pw.close();
        bos.close();
    }

    public static ArchivoPGM desplazarImagen(ArchivoPGM imagenOriginal, int dx, int dy, int valorFijo) {

        String nroMagico = imagenOriginal.getNroMagico();
        int alto = imagenOriginal.getMatriz().length;
        int ancho = imagenOriginal.getMatriz()[0].length;
        int valorMax = imagenOriginal.getValorMax();
        ArchivoPGM matrizDesplazada = new ArchivoPGM(nroMagico,alto, ancho, valorMax);

        // Inicialización de valorFijo

        for (int y = 0; y < alto; ++y) {
            for (int x = 0; x < ancho; ++x) {
                matrizDesplazada.setMatriz(y, x, valorFijo);
            }
        }

        // Desplazamiento Der || Izq || Arriba || Abajo

        for (int y = 0; y < alto; ++y) {
            for (int x = 0; x < ancho; ++x) {
                int nuevoX = x + dx;
                int nuevaY = y + dy;

                if (nuevaY >= 0 && nuevaY < alto && nuevoX >= 0 && nuevoX < ancho) {
                    matrizDesplazada.setMatriz(nuevaY, nuevoX, imagenOriginal.getMatriz()[y][x]);
                }
            }
        }

        return matrizDesplazada;
    }
}
