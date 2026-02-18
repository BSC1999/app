package com.simats.afinal;

import java.io.Serializable;

public class LogEntry implements Serializable {
    private String userName;
    private String role;
    private String activity;
    private String timestamp;
    private String ipAddress;
    private long timeMillis;

    public LogEntry(String userName, String role, String activity, String timestamp, String ipAddress, long timeMillis) {
        this.userName = userName;
        this.role = role;
        this.activity = activity;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.timeMillis = timeMillis;
    }

    public String getUserName() { return userName; }
    public String getRole() { return role; }
    public String getActivity() { return activity; }
    public String getTimestamp() { return timestamp; }
    public String getIpAddress() { return ipAddress; }
    public long getTimeMillis() { return timeMillis; }
}
