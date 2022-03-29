package com.example.project2bookingsample;

/**
 * Functional interface.
 * Add a sleep statement in listen() to prevent busy polling.
 */
public interface NotifierListener {
    void listen(String str);
}
