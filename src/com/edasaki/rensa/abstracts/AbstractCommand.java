package com.edasaki.rensa.abstracts;

import com.edasaki.rensa.commands.CommandMatchType;

import sx.blah.discord.handle.obj.IMessage;

public abstract class AbstractCommand {

    private String identifier; // the word at the start of a chat message that triggers this command
    private String identifierExact; // identifier with case preserved
    private long lastExecution; // when the command was last executed
    private long cooldown; // the amount of time to wait between executions of this command
    private CommandMatchType matchType; // the type of matching this command uses

    /**
     * This method assumes that check() has already been verified.
     * @param content the content of the message
     * @param msg the IMessage object
     */
    public void execute(String content, IMessage msg) {
        if (System.currentTimeMillis() - lastExecution < cooldown)
            return;
        String contentLower = content.toLowerCase(); // use to find indices
        String trimmed;
        switch (matchType) {
            case ANY:
            case ANY_EXACT:
                execute(content.split(" "), msg);
                break;
            case PREFIX:
            case PREFIX_EXACT:
                // Drop the identifier from the front
                trimmed = content.substring(contentLower.indexOf(identifier) + identifier.length()).trim();
                execute(trimmed.split(" "), msg);
                break;
            case SUFFIX:
            case SUFFIX_EXACT:
                // Drop the identifier from the end
                trimmed = content.substring(0, contentLower.lastIndexOf(identifier)).trim();
                execute(trimmed.split(" "), msg);
                break;
        }
    }

    /**
     * Checks to see if the given message content matches
     * this command
     * @param messageExact
     * @return
     */
    public boolean check(String content) {
        String messageExact = content.trim();
        String message = messageExact.toLowerCase();
        switch (matchType) {
            case ANY:
                return message.contains(identifier);
            case ANY_EXACT:
                return messageExact.contains(identifierExact);
            case PREFIX:
                return message.startsWith(identifier);
            case PREFIX_EXACT:
                return messageExact.startsWith(identifierExact);
            case SUFFIX:
                return message.endsWith(identifier);
            case SUFFIX_EXACT:
                return messageExact.endsWith(identifierExact);
        }
        return false;
    }

    protected abstract void execute(String[] args, IMessage msg);

    public AbstractCommand(String identifier, CommandMatchType matchType) {
        this(identifier, matchType, 0);
    }

    public AbstractCommand(String identifier, CommandMatchType matchType, long cooldown) {
        identifier = identifier.trim();
        this.identifier = identifier.toLowerCase();
        this.identifierExact = identifier;
        this.matchType = matchType;
        this.cooldown = cooldown;
    }

}
