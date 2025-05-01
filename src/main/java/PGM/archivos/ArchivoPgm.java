package PGM.archivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Objects;

public abstract class ArchivoPgm {
    protected String nroMagico;
    protected int alto;
    protected int ancho;
    protected int valorMax;
    protected int[][] matriz;

    public String getNroMagico(){
        return nroMagico;
    }

    public int getAlto(){
        return alto;
    }

    public int getAncho(){
        return ancho;
    }

    public int getValorMax(){
        return valorMax;
    }

    public int[][] getMatriz(){
        return matriz;
    }

    // Constructor protegido: solo accesible por las subclases
    protected ArchivoPgm(String nroMagico, int alto, int ancho, int valorMax) {
        if (alto <= 0 || ancho <= 0 || valorMax < 0 || nroMagico == null || nroMagico.isEmpty()) {
            throw new IllegalArgumentException("Parámetros de imagen PGM inválidos.");
        }
        this.nroMagico = nroMagico; // El magic number se define en la subclase
        this.alto = alto;
        this.ancho = ancho;
        this.valorMax = valorMax;
        this.matriz = new int[alto][ancho]; // Inicializa la matriz común
    }

    public int getPixel(int fila, int columna) {
        if (fila < 0 || fila >= this.alto || columna < 0 || columna >= this.ancho) {
            throw new IndexOutOfBoundsException(
                    String.format("Índices de pixel (%d, %d) fuera de rango [%d, %d].",
                            fila, columna, this.alto, this.ancho));
        }
        return this.matriz[fila][columna];
    }

    public void setPixel(int fila, int columna, int valor) {
        if (fila < 0 || fila >= this.alto || columna < 0 || columna >= this.ancho) {
            throw new IndexOutOfBoundsException(
                    String.format("Índices de pixel (%d, %d) fuera de rango [%d, %d].",
                            fila, columna, this.alto, this.ancho));
        }

        if (valor < 0 || valor > this.valorMax) {
            throw new IllegalArgumentException("Valor de pixel fuera del rango [0, " + this.valorMax + "]: " + valor);
        }
        this.matriz[fila][columna] = valor;
    }

    // --- Métodos Abstractos ---

    // Metodo abstracto para que cada formato lea sus datos de pixeles
    // Recibe el stream posicionado justo después de la cabecera
    protected abstract void readPixelData(BufferedInputStream bis) throws IOException;

    // Metodo abstracto para que cada formato escriba sus datos de pixeles
    // Recibe el stream donde escribir los datos
    protected abstract void writePixelData(BufferedOutputStream bos) throws IOException;

    // Metodo abstracto de fábrica: permite a la clase base crear una nueva instancia
    // del mismo tipo concreto (PGM_P2 o PGM_P5) con las dimensiones dadas.
    protected abstract ArchivoPgm createNewInstance(int alto, int ancho, int valorMax);


    // --- Metodo de Fábrica Estático para crear un ArchivoPGM leyendo de un archivo ---
    public static ArchivoPgm read(String archivoOrigen) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivoOrigen);
             BufferedInputStream bis = new BufferedInputStream(fis))
        {
            // Metodo manual para leer líneas sin consumir de más
            String magicNumber = Objects.requireNonNull(readAsciiLine(bis)).trim();
            if (magicNumber.isEmpty() || (!magicNumber.equals("P2") && !magicNumber.equals("P5"))) {
                throw new IllegalArgumentException("Formato PGM no soportado o archivo vacío: " + magicNumber);
            }

            String dimensionesLine;
            do {
                dimensionesLine = Objects.requireNonNull(readAsciiLine(bis)).trim();
            } while (dimensionesLine.startsWith("#") || dimensionesLine.isEmpty());

            String[] dimensiones = dimensionesLine.split("\\s+");
            if (dimensiones.length != 2) {
                throw new IOException("Formato de dimensiones incorrecto: '" + dimensionesLine + "'");
            }
            int ancho = Integer.parseInt(dimensiones[0]);
            int alto = Integer.parseInt(dimensiones[1]);

            String valorMaxLine;
            do {
                valorMaxLine = Objects.requireNonNull(readAsciiLine(bis)).trim();
            } while (valorMaxLine.startsWith("#") || valorMaxLine.isEmpty());

            int valorMax = Integer.parseInt(valorMaxLine);

            ArchivoPgm imagen;
            if (magicNumber.equals("P2")) {
                imagen = new ArchivoPgmP2(alto, ancho, valorMax);
            } else {
                imagen = new ArchivoPgmP5(alto, ancho, valorMax);
            }

            imagen.readPixelData(bis);
            return imagen;
        }
    }

    // Función auxiliar para leer una línea ASCII sin avanzar demás
    private static String readAsciiLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = is.read()) != -1) {
            if (c == '\n') break;
            if (c != '\r') sb.append((char) c);
        }
        if (c == -1 && sb.isEmpty()) {
            return null;
        }
        return sb.toString();
    }


    // --- Metodo de Instancia para escribir la imagen a un archivo ---
    public void write(String archivoDestino) throws IOException {
        File destinationFile = new File(archivoDestino);
        File outputDir = destinationFile.getParentFile();
        if (outputDir != null && !outputDir.exists()){
            boolean dirCreated = outputDir.mkdirs();
            if (!dirCreated) {
                throw new IOException("No se pudo crear el directorio de salida: " + outputDir.getAbsolutePath());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(archivoDestino);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             PrintWriter pw = new PrintWriter(bos))
        {
            // Escribir la cabecera PGM (común a P2 y P5, solo cambia el Magic Number)
            pw.println(this.nroMagico);
            pw.println("# Archivo generado | Desplazamiento (shift) de imagen"); // Comentario
            pw.printf("%d %d\n", this.ancho, this.alto); // Dimensiones
            pw.println(this.valorMax); // Valor máximo
            pw.flush(); // Importante: Aseguramos que la cabecera se escriba antes de los datos

            this.writePixelData(bos); // La subclase sabe cómo escribir sus píxeles

            // No cerramos PrintWriter aquí, ya que bos debe permanecer abierto para writePixelData (si es P5)
            // y el try-with-resources cerrará bos y fos al salir.
        } // El try-with-resources asegura que bos y fos se cierren automáticamente
    }


    // --- Metodo de Instancia para desplazar la imagen ---
    public ArchivoPgm shifted(int dx, int dy, int valorFijo) {

        if (dx == 0 && dy == 0) {
            return this;
        }

        // Creamos una nueva instancia del mismo tipo (P2 o P5)
        ArchivoPgm imagenDesplazada = this.createNewInstance(this.alto, this.ancho, this.valorMax);

        for (int y = 0; y < this.alto; ++y) {
            for (int x = 0; x < this.ancho; ++x) {
                imagenDesplazada.setPixel(y, x, valorFijo);
            }
        }

        // Desplazamiento
        for (int y = 0; y < this.alto; ++y) {
            for (int x = 0; x < this.ancho; ++x) {
                int nuevoX = x + dx;
                int nuevaY = y + dy;

                // Verificamos que la nueva posición esté dentro de los límites, si no, la ignoramos
                if (nuevaY >= 0 && nuevaY < this.alto && nuevoX >= 0 && nuevoX < this.ancho) {
                    imagenDesplazada.setPixel(nuevaY, nuevoX, this.getPixel(y, x));
                }
            }
        }

        return imagenDesplazada;
    }

    @Override
    public String toString() {
        return String.format("ArchivoPGM [Magic: %s, Dimension: %dx%d, MaxVal: %d]",
                nroMagico, ancho, alto, valorMax);
    }
}