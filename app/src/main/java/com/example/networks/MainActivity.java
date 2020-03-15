package com.example.networks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout background;
    private Button weatherButton;
    private TextView weatherText;
    private TextView pressureText;
    private TextView humidityText;

    private static final String HOST_NAME = "10.18.232.161";
    private static final int PORT_NUM = 12235;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        background = findViewById(R.id.background);
        weatherButton = findViewById(R.id.weather);
        weatherText = findViewById(R.id.weatherText);
        pressureText = findViewById(R.id.pressureText);
        humidityText = findViewById(R.id.humidityText);
        AsyncTask<Void, Void, byte[]> runningTask = new DownloadData();
        Toast.makeText(MainActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
        runningTask.execute();
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
                AsyncTask<Void, Void, byte[]> runningTask = new DownloadData();
                runningTask.execute();
            }
        });
    }

    private class DownloadData extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected byte[] doInBackground(Void... voids) {
            try {
                System.out.println("fucking asshole");
                Socket tcpSocket = new Socket(InetAddress.getByName(HOST_NAME), PORT_NUM);
                System.out.println("CONNECTED");
                System.out.println(tcpSocket.isConnected());
                DataOutputStream dOut = new DataOutputStream(tcpSocket.getOutputStream());
                DataInputStream dIn = new DataInputStream(tcpSocket.getInputStream());
                byte[] checkOut = new byte[12];
                int received = dIn.read(checkOut);
                System.out.println(received);
                return checkOut;
            } catch(IOException e) {
                e.printStackTrace();
            }
            return new byte[0];
        }

        @Override
        protected void onPostExecute(byte[] result) {
            byte[] temp = new byte[4];
            byte[] pressure = new byte[4];
            byte[] humidity = new byte[4];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = result[i];
            }
            for (int i = 4; i < pressure.length + 4; i++) {
                pressure[i - 4] = result[i];
            }
            for (int i = 8; i < humidity.length + 8; i++) {
                humidity[i - 8] = result[i];
            }

            float tempFloat = ByteBuffer.wrap(temp).getFloat();
            float pressureFloat = ByteBuffer.wrap(pressure).getFloat() * (float)0.0145037738;
            float humidityFloat = ByteBuffer.wrap(humidity).getFloat();

            if (tempFloat <= 10) {
                background.setBackgroundColor(Color.CYAN);
            } else {
                background.setBackgroundColor(Color.YELLOW);
            }

            weatherText.setText(tempFloat + " °C");
            pressureText.setText(pressureFloat + " psi");
            humidityText.setText(humidityFloat + "%");
            Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTexts() {
        try {
            System.out.println("fucking asshole");
            Socket tcpSocket = new Socket(InetAddress.getByName(HOST_NAME), PORT_NUM);
            System.out.println("CONNECTED");
            System.out.println(tcpSocket.isConnected());
            DataOutputStream dOut = new DataOutputStream(tcpSocket.getOutputStream());
            DataInputStream dIn = new DataInputStream(tcpSocket.getInputStream());
            byte[] checkOut = new byte[12];
            int received = dIn.read(checkOut);
            System.out.println(received);
            System.out.println("fucking asshole2");
            System.out.println(checkOut);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
