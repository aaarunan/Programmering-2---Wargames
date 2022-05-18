package edu.ntnu.arunang.wargames.event;

/**
 * A generic event listener interface used for observing subjects.
 * The observer is used primarily for gui purposes.
 * Standalone event listeners can be created as a separate classes or by
 * functional programming.
 */

public interface EventListener {

    /**
     * This method is called when the subject has been changed or altered.
     * An event type has to be sent, to indicate the type of changes that has
     * occured.
     *
     * @param eventtype the type of event that has occured
     */

    void update(EventType eventtype);
}
