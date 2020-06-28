package com.spacetime.tardis.exceptions;

public class ParadoxException extends RuntimeException {
    public ParadoxException(String id) {
        super("Could not allow traveller " + id + " to visit requested place and time due to already being present!");
    }
}
