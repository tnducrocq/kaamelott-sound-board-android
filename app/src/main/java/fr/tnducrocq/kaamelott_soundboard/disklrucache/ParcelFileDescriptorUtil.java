package fr.tnducrocq.kaamelott_soundboard.disklrucache;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tony on 30/05/2017.
 */
public class ParcelFileDescriptorUtil {

    public static ParcelFileDescriptor pipeFrom(InputStream inputStream)
            throws IOException {
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
        ParcelFileDescriptor readSide = pipe[0];
        ParcelFileDescriptor writeSide = pipe[1];

        new TransferThread(inputStream, new ParcelFileDescriptor.AutoCloseOutputStream(writeSide))
                .start();

        return readSide;
    }


    public static TransferThread pipeTo(OutputStream outputStream, ParcelFileDescriptor output)
            throws IOException {

        TransferThread t = new TransferThread(new ParcelFileDescriptor.AutoCloseInputStream(output), outputStream);

        t.start();
        return t;
    }


    static class TransferThread extends Thread {

        public static final String TAG = TransferThread.class.getSimpleName();

        final InputStream mIn;
        final OutputStream mOut;

        TransferThread(InputStream in, OutputStream out) {
            super("IPC Transfer Thread");
            mIn = in;
            mOut = out;
            setDaemon(true);
        }

        @Override
        public void run() {
            byte[] buf = new byte[4096];
            int len;

            try {
                while ((len = mIn.read(buf)) > 0) {
                    mOut.write(buf, 0, len);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException when writing to out", e);
            } finally {
                try {
                    mIn.close();
                } catch (IOException ignored) {
                }
                try {
                    mOut.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
