package me.JuliusH_1.mazerunners.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventHandler {

    private final Map<Integer, String> ongoingEvents = new HashMap<>();
    private final Map<Integer, Set<String>> eventPlayers = new HashMap<>();
    private int nextEventNumber = 1;

    public int startEvent(Set<String> playerNames) {
        String eventName = "Event_" + nextEventNumber;
        ongoingEvents.put(nextEventNumber, eventName);
        eventPlayers.put(nextEventNumber, playerNames);
        return nextEventNumber++;
    }

    public void endEvent(int eventNumber) {
        ongoingEvents.remove(eventNumber);
        eventPlayers.remove(eventNumber);
    }

    public String getEventName(int eventNumber) {
        return ongoingEvents.get(eventNumber);
    }

    public Set<String> getEventPlayers(int eventNumber) {
        return eventPlayers.getOrDefault(eventNumber, new HashSet<>());
    }

    public Map<Integer, String> getOngoingEvents() {
        return ongoingEvents;
    }
}