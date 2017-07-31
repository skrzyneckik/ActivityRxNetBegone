package io.github.skrzyneckik.activityrxnetbegone;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import io.github.skrzyneckik.activityrxnetbegone.util.RxUtils;
import rx.subscriptions.CompositeSubscription;

/**
 * Long running service for network calls
 */

public class NetworkCallService extends Service {

    private volatile Looper mServiceLooper;
    private volatile CompositeSubscription subscriptions;

    /**
     * ignore binding support for now
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // below "TODO" copied form IntentService - maybe we can think about it
        // TODO: It would be nice to have an option to hold a partial wakelock
        // during processing, and to have a static startService(Context, Intent)
        // method that would launch the service & hand off a wakelock.

        super.onCreate();
        HandlerThread thread = new HandlerThread("JFSSyncService[Normal]", Thread.NORM_PRIORITY);
        thread.start();

        mServiceLooper = thread.getLooper();
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtils.cleanSubscriptionsIfNotNull(subscriptions);
        mServiceLooper.quit();
    }
}
