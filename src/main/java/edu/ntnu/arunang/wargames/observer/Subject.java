package edu.ntnu.arunang.wargames.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

}
