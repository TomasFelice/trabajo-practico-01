package PGM.archivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class ArchivoPgmP5 extends ArchivoPgm {

    private static final String MAGIC_NUMBER = "P5";

    public ArchivoPgmP5(int alto, int ancho, int valorMax) {
        super(MAGIC_NUMBER, alto, ancho, valorMax);
    }

    @Override
    protected void readPixelData(BufferedInputStream bis) throws IOException {
        // ❶ Saltamos (si existe) el '\n' inmediato al valorMax del encabezado
        bis.mark(1);
        int first = bis.read();
        if (first != '\n' && first != '\r') {   // no era salto de línea → volvemos atrás
            bis.reset();
        }

        int bytesPerPixel = (valorMax <= 255) ? 1 : 2; //puede ser 1 o 2 bytes
        int totalBytes    = alto * ancho * bytesPerPixel;

        byte[] buffer = new byte[totalBytes];
        int offset = 0;
        while (offset < totalBytes) { 
            int n = bis.read(buffer, offset, totalBytes - offset);
            if (n == -1)
                throw new IOException("Datos de píxeles insuficientes en P5.");
            offset += n;
        }

        // ❸ Convertimos el bloque plano en la matriz [alto][ancho]
        matriz = new int[alto][ancho];
        int idx = 0;
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (bytesPerPixel == 1) {
                    matriz[i][j] = buffer[idx++] & 0xFF;
                } else {                       // 16-bits, big-endian
                    int hi = buffer[idx++] & 0xFF;
                    int lo = buffer[idx++] & 0xFF;
                    matriz[i][j] = (hi << 8) | lo;
                }
            }
        }
    }

    @Override
    protected void writePixelData(BufferedOutputStream bos) throws IOException {
        int bytesPerPixel = (valorMax <= 255) ? 1 : 2;

        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                int val = matriz[i][j];
                if (bytesPerPixel == 1) {
                    bos.write(val & 0xFF);
                } else {                       // 16-bits, big-endian
                    bos.write((val >> 8) & 0xFF);
                    bos.write(val & 0xFF);
                }
            }
        }
        bos.flush(); // aseguramos escritura
    }

    @Override
    protected ArchivoPgm createNewInstance(int alto, int ancho, int valorMax) {
        return new ArchivoPgmP5(alto, ancho, valorMax);
    }
}