package com.example.myapplication;
import java.util.HashMap;
import java.util.Map;

public class Pi {
    private String name;
    private String IpAddress;
    private String password;
    private static HashMap<String, Pi> tracker = new HashMap<>();


    Pi(String n, String a, String p) {
        name = n;
        IpAddress = a;
        password = p;
        System.out.println(tracker.size());
        putInMap();

    }
    private void putInMap() {
        tracker.put(name, this);

    }
    public static HashMap<String, Pi> getTracker() {
        return tracker;
    }
    public String getIp() {
        return IpAddress;
    }
    public String getPassword() {
        return password;
    }
}
