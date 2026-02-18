package com.simats.afinal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class LogManager {
    private static List<LogEntry> logs = new ArrayList<>();
    private static final long THIRTY_DAYS_MILLIS = 30L * 24 * 60 * 60 * 1000;

    static {
        // Sample data
        addLog("Dr. Emily Carter", "Doctor", "Viewed Patient Record", "192.168.1.100");
        addLog("Sarah Miller", "Intern", "Updated Appointment", "192.168.1.101");
        addLog("Dr. David Lee", "Consultant", "Added New Patient", "192.168.1.102");
    }

    public static void addLog(String name, String role, String activity, String ip) {
        long now = System.currentTimeMillis();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(new Date(now));
        logs.add(0, new LogEntry(name, role, activity, timestamp, ip, now));
        cleanOldLogs();
    }

    public static List<LogEntry> getLogs() {
        cleanOldLogs();
        return logs;
    }

    private static void cleanOldLogs() {
        long now = System.currentTimeMillis();
        Iterator<LogEntry> iterator = logs.iterator();
        while (iterator.hasNext()) {
            if (now - iterator.next().getTimeMillis() > THIRTY_DAYS_MILLIS) {
                iterator.remove();
            }
        }
    }
}
