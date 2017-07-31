package io.github.skrzyneckik.activityrxnetbegone.util;

import rx.subscriptions.CompositeSubscription;

/**
 * Taken from Rx-Android-Samples created by Kaushik Gopal
 *
 * @see <a href="https://github.com/kaushikgopal/RxJava-Android-Samples">RxJava-Android-Samples</a>
 */
public class RxUtils {
    public static void cleanSubscriptionsIfNotNull(CompositeSubscription subscription) {
        if (subscription != null) {
            subscription.clear();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
