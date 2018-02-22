package sulaiman.com.lightsensor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;


/**
 * Created by abed on 2/19/18.
 */

public class SocketManager {
    private Socket mSocket;
    private String ip;
    private int port;

    private boolean connected = false;
    private Context mContext;

    public SocketManager(Context context) {
        mContext = context;
    }

    public void connect(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
        try {
            mSocket = new Socket(ip, port);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    public void sendLUX(final float lux) {


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lux", String.format("%010d", (long) lux));



            String data = jsonObject.toString();

            mSocket.getOutputStream().write(data.getBytes());
            mSocket.getOutputStream().flush();


            Log.d("LUX", data.getBytes().length + " bytes");
            Log.d("LUX", data);

        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
            Toast.makeText(mContext, "Disconnected!!!", Toast.LENGTH_LONG).show();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            connected = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        try {
            mSocket.close();
            mSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
