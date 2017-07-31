package io.github.skrzyneckik.activityrxnetbegone;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import io.github.skrzyneckik.activityrxnetbegone.util.RxUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Long running service for network calls
 */

public class NetworkCallService extends Service {

    public static final String LONG_RUNNING_TASK_RESULT = "io.github.skrzyneckik.LONG_RUNNING_TASK_RESULT";

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
        // below "TODO" copied from IntentService - maybe we can think about it
        // TODO: It would be nice to have an option to hold a partial wakelock
        // during processing, and to have a static startService(Context, Intent)
        // method that would launch the service & hand off a wakelock.

        super.onCreate();
        HandlerThread thread = new HandlerThread("NetworkCallService[Normal]", Thread.NORM_PRIORITY);
        thread.start();

        mServiceLooper = thread.getLooper();
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        subscriptions.add(
            longRunningTask()
            .subscribeOn(AndroidSchedulers.from(mServiceLooper))
            .subscribe(success ->
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LONG_RUNNING_TASK_RESULT))
            )
        );

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtils.cleanSubscriptionsIfNotNull(subscriptions);
        mServiceLooper.quit();
    }

    public Observable longRunningTask() {
        return Observable.just(null).delay(10, TimeUnit.SECONDS);
    }
}
