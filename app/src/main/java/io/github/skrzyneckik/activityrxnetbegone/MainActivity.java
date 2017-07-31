package io.github.skrzyneckik.activityrxnetbegone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.skrzyneckik.activityrxnetbegone.NetworkCallService.LONG_RUNNING_TASK_RESULT;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver receiver;

    AtomicInteger receivedResultCount = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        startService(new Intent(this, NetworkCallService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receivedResultCount.getAndIncrement();
                Toast.makeText(MainActivity.this,
                    String.format("received intent: %d", receivedResultCount.intValue()),
                    Toast.LENGTH_LONG).show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            new IntentFilter(LONG_RUNNING_TASK_RESULT));
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
