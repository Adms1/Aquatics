package water.works.waterworks.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by glenns on 09/08/13.
 */
public class PowerChecker extends Service{

    @Override
    public IBinder onBind(Intent intent){
            return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy(){
    super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerBroadcastReceiver receiver = new PowerBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(receiver, filter);

        return START_STICKY;
    }

}
