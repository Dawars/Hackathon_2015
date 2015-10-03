package me.dawars.hackathon2015;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

public class BlueComms {

    private static final String TAG = "BlueComms";
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBluetoothAdapter;
    private static final String MAC = "98:D3:31:60:2B:37";
    private BluetoothDevice device;
    private BluetoothSocket btSocket;
    private BufferedReader br;
    private PrintWriter pw;

    public boolean connect() {
        Log.d(TAG, "connect()");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
            return false;
        if (!mBluetoothAdapter.isEnabled())
            return false;

        device = mBluetoothAdapter.getRemoteDevice(MAC);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (Exception e) {
            return false;
        }

        try {
            btSocket.connect();
        } catch (Exception e) {
            close();
            return false;
        }
        try {
            br = new BufferedReader(new InputStreamReader(btSocket.getInputStream()));
            pw = new PrintWriter(btSocket.getOutputStream());
        } catch (Exception e) {
            close();
            return false;
        }
        return true;
    }

    public void close() {
        Log.d(TAG, "close()");
        try {
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            pw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            btSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getDataI(char t) {
        Log.d(TAG, "getDataI("+t+")");
        try {
            pw.print(t);
            pw.flush();
            return Integer.parseInt(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
        return -1;
    }

    public void turnLed(boolean b) {
        Log.d(TAG, "turnLed("+b+")");
        try {
            if(b)
                pw.print("l 255 0 127 ");
            else
                pw.print("l 0 0 0 ");
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }
}
