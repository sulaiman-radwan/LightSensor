package sulaiman.com.lightsensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView textLight;
    EditText textIp;
    Button btnConnect;
    SensorManager mSensorManager;
    Sensor mLight;
    OutputStreamWriter outputStreamWriter;

    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;

        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        textLight = findViewById(R.id.light_text);
        textIp = findViewById(R.id.ip_text);

        socketManager = new SocketManager(this);

        btnConnect = findViewById(R.id.connect_btn);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!socketManager.isConnected()) {
                    socketManager.connect(textIp.getText().toString(), 1025);
                    btnConnect.setText("Disconnect");
                } else {
                    socketManager.disconnect();
                    btnConnect.setText("Connect");
                }
            }
        });
    }


    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @SuppressLint("SetTextI18n")
    @Override
    public final void onSensorChanged(SensorEvent event) {
        float value = event.values[0];
        textLight.setText((int) value + " lux");
        if (socketManager.isConnected()) {
            socketManager.sendLUX(value);
        } else {
            btnConnect.setText("Connect");
        }
    }

}
