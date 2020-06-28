package com.spacetime.tardis.exceptions;

public class TravellerNotFoundException extends RuntimeException {
    public TravellerNotFoundException(String id) {
        super("Could not find traveller " + id);
    }
}
