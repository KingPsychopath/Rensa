package com.edasaki.rensa.exceptions;

@SuppressWarnings("serial")
public class NoRensaException extends Exception {
    public NoRensaException() {
        super("No instance of Rensa existS.");
    }
}
