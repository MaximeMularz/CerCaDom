package lpa.aumont.cercadom.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CerCaDomSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static CerCaDomSyncAdapter sCerCaDomSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("CerCaDomSyncService", "onCreate - CerCaDomSyncService");
        synchronized (sSyncAdapterLock) {
            if (sCerCaDomSyncAdapter == null) {
                sCerCaDomSyncAdapter = new CerCaDomSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sCerCaDomSyncAdapter.getSyncAdapterBinder();
    }
}