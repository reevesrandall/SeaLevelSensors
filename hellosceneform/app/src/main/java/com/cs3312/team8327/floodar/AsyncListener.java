package com.cs3312.team8327.floodar;

/**
 * Interface to guarantee the presence of a callback function for classes calling asynchronous methods
 */
public interface AsyncListener {
    void onEventCompleted();
}
