package edu.ntnu.arunang.wargames.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for a Subject. The class store functions
 * for notifying, adding, and attaching listeners.
 */

public abstract class Subject {
    List<EventListener> listeners;

    /**
     * Constructor that creates a new empty arraylist for listeners.
     */

    public Subject() {
        listeners = new ArrayList<>();
    }

    /**
     * Attaches an observer to the subject. The event-listener will then
     * observe the subject.
     *
     * @param eventListener the event listener that is being attached
     */

    public void attach(EventListener eventListener) {
        listeners.add(eventListener);
    }

    /**
     * Detaches a given observer from the subject. The listener will no longer be
     * notified of changes.
     *
     * @param eventListener that is being removed.
     */

    public void detach(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    /**
     * Notifies the attached listeners. An event type must be given
     * for the type of changes.
     *
     * @param eventtype type of change that has occured.
     */

    public void notifyObservers(EventType eventtype) {
        listeners.forEach(observer -> observer.update(eventtype));
    }

}
