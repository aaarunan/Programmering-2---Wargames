package edu.ntnu.arunang.wargames.event;

/**
 * The different types of events that can occur.
 * <p>
 * UPDATE - used for notifying the observer that a change has
 * occured.
 * <p>
 * FINISH - notifies the observers that the operation has been finished.
 */

public enum EventType {
    UPDATE, FINISH
}
