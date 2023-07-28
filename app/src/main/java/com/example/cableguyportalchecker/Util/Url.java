package com.example.cableguyportalchecker.Util;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class Url {

    URL url;

    public Url(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public static class Ping {
        public String net = "NO_CONNECTION";
        public String host = "";
        public String ip = "";
        public int dns = Integer.MAX_VALUE;
        public int cnt = Integer.MAX_VALUE;
        public boolean status = false;
    }

    public Ping ping(Context ctx) throws InterruptedException {
        Ping r = new Ping();
        if (isNetworkConnected(ctx)) {
            r.net = getNetworkType(ctx);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String hostAddress;
                        int port = url.getPort() != -1 ? url.getPort() : 80;
                        long start = System.currentTimeMillis();
                        hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
                        long dnsResolved = System.currentTimeMillis();
                        Socket socket = new Socket(hostAddress,port);
//                        Log.d(TAG, "run: ");
                        socket.close();
                        long probeFinish = System.currentTimeMillis();
                        r.dns = (int) (dnsResolved - start);
                        r.cnt = (int) (probeFinish - start);
                        r.host = url.getHost();
                        r.ip = hostAddress;
                        r.status = true;
//                        Log.d(TAG, "run: " + r.ip);

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        r.status = false;
//                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        r.status = false;
//                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
            thread.join();
        }

        return r;
    }

    public int isUP(Context ctx) throws InterruptedException {
        final int[] response = {0};

        if (isNetworkConnected(ctx)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        Log.d(TAG, "run: " + connection.getResponseCode());
                        if ((connection.getResponseCode() <= 300 && connection.getResponseCode() >= 200) || connection.getResponseCode() == 401) {
                            response[0] = 1;
                        }
                        connection.disconnect();
                    } catch (IOException e) {
                        response[0] = 0;
//                      throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
            thread.join();

        }

        return response[0];
    }

    private static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Nullable
    public static String getNetworkType(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }

}