package c.proyecto.utils;


import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public final class Imagenes {


    //Comprueba si el dispositivo dispone de cámara.
    public static boolean hayCamara(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static String getRealPathFromGallery(Context context, Uri galleryUri){
        // Se consulta en el content provider de la galería el path real del archivo de la foto.
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(galleryUri, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String path = c.getString(columnIndex);
        c.close();
        return path;
    }
    public static Bitmap escalar(int width, int height, String path){
        // Se obtiene el tamaño de la imagen.
        BitmapFactory.Options opciones = new BitmapFactory.Options();
        opciones.inJustDecodeBounds = true; // Solo para cálculo.
        BitmapFactory.decodeFile(path, opciones);
        int anchoFoto = opciones.outWidth;
        int altoFoto = opciones.outHeight;
        // Se obtiene el factor de escalado para la imagen.
        int factorEscalado = Math.min(anchoFoto / width,
                altoFoto / height);
        // Se escala la imagen con dicho factor de escalado.
        opciones.inJustDecodeBounds = false; // Se escalará.
        opciones.inSampleSize = factorEscalado;
        return BitmapFactory.decodeFile(path, opciones);
    }

    public static boolean guardarBitmapEnArchivo(Bitmap bitmapFoto, File archivo) {
        try {
            FileOutputStream flujoSalida = new FileOutputStream(archivo);
            bitmapFoto.compress(Bitmap.CompressFormat.JPEG, 100, flujoSalida);
            flujoSalida.flush();
            flujoSalida.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Crea un archivo de foto con el nombre indicado en almacenamiento externo si es posible, o si
    // no en almacenamiento interno, y lo retorna. Retorna null si fallo.
    // Si publico es true -> en la carpeta pública de imágenes.
    // Si publico es false, en la carpeta propia de imágenes.
    public static File crearArchivoFoto(Context context, String nombre, boolean publico) {
        // Se obtiene el directorio en el que almacenarlo.
        File directorio;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (publico)
                // En el directorio público para imágenes del almacenamiento externo.
                directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            else
                directorio = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            // En almacenamiento interno.
            directorio = context.getFilesDir();
        }
        // Su no existe el directorio, se crea.
        if (directorio != null && !directorio.exists())
            if (!directorio.mkdirs())
                return null;

        // Se crea un archivo con ese nombre y la extensión jpg en ese
        // directorio.
        File archivo = null;
        if (directorio != null)
            archivo = new File(directorio.getPath() + File.separator + nombre);

        // Se retorna el archivo creado.
        return archivo;
    }


}
