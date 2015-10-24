package com.example.rguleria.responderapp;

import android.graphics.Color;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Formatter;

public class MainActivity extends AppCompatActivity {


    TextView tl,tr,ml,mr,bl,br;
    private RunServerInThread runServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl = (TextView) findViewById(R.id.tl);
        tr = (TextView) findViewById(R.id.tr);

        ml = (TextView) findViewById(R.id.ml);
        mr = (TextView) findViewById(R.id.mr);

        bl = (TextView) findViewById(R.id.bl);
        br = (TextView) findViewById(R.id.br);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        tl.setBackgroundColor(Color.WHITE);
        tl.setText(ip);

        WindowManager w = this.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        Point realSize = new Point();
        try {
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        int width = realSize.x;
        int height = realSize.y;

        //Display display = getWindowManager().getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        //int width = size.x;
        //int height = size.y;

        tl.setWidth(width/2);
        tl.setHeight(height / 3);
        //tl.setBackgroundColor(Color.WHITE);

        tr.setWidth(width/2);
        tr.setHeight(height / 3);
        //tr.setBackgroundColor(Color.WHITE);

        ml.setWidth(width / 2);
        ml.setHeight(height / 3);
        //ml.setBackgroundColor(Color.WHITE);

        mr.setWidth(width/2);
        mr.setHeight(height / 3);
        //mr.setBackgroundColor(Color.WHITE);

        bl.setWidth(width / 2);
        bl.setHeight(height / 3);
        //bl.setBackgroundColor(Color.WHITE);

        br.setWidth(width/2);
        br.setHeight(height / 3);
        //br.setBackgroundColor(Color.WHITE);

        runServer = new RunServerInThread();
        runServer.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RunServerInThread extends Thread{
        private boolean keepRunning = true;
        private String lastMessage = "";

        @Override
        public void run() {
            String message;
            byte[] lmessage = new byte[1024];
            DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
            DatagramSocket socket = null;
            while(keepRunning){
                //Log.d("WHOOPS","WHOOPS");
                try {
                    socket = new DatagramSocket(9876);
                    socket.receive(packet);
                    message = new String(packet.getData());

                    Log.d("Message",message);

                    tl.setText(message);

                    tl.setBackgroundColor(Color.BLACK);
                    tr.setBackgroundColor(Color.BLACK);

                    ml.setBackgroundColor(Color.BLACK);
                    mr.setBackgroundColor(Color.BLACK);

                    bl.setBackgroundColor(Color.BLACK);
                    br.setBackgroundColor(Color.BLACK);


                    switch (message) {
                        case "tl" : {
                            tl.setBackgroundColor(Color.WHITE);
                            break;
                        }
                        case "tr": {
                            tr.setBackgroundColor(Color.WHITE);
                            break;
                        }
                        case "ml": {
                            ml.setBackgroundColor(Color.WHITE);
                            break;
                        }
                        case "bl":{
                            bl.setBackgroundColor(Color.WHITE);
                            break;
                        }
                        case "br":{
                            br.setBackgroundColor(Color.WHITE);
                            break;
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}
