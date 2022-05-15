package edu.ntnu.arunang.wargames.event;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    List<EventListener> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void attach(EventListener observer) {
        observers.add(observer);
    }

    public void detach(EventListener observer) {
        observers.remove(observer);
    }

    public void notifyObservers(EventType eventtype) {
        observers.forEach(observer -> observer.update(eventtype));
    }

}
