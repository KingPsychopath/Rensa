package com.edasaki.rensa.exceptions;

@SuppressWarnings("serial")
public class InvalidChannelIDException extends Exception {
    public InvalidChannelIDException(String id) {
        super("No associated channel could be found for ID " + id + ".");
    }
}
