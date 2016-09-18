package com.edasaki.rensa.commands;

public enum CommandMatchType {
    /** 
     * Command matches when identifier occurs at the start of a chat message. Case insensitive. Identifier is not included in args.
     */
    PREFIX,
    /** 
     * Command matches when identifier occurs at the end of a chat message. Case insensitive. Identifier is not included in args.
     */
    SUFFIX,
    /** 
     * Command matches when identifier is found anywhere in the chat message. Case insensitive. Identifier is included in args.
     */
    ANY,

    /** 
     * Command matches when identifier occurs at the start of a chat message. Case sensitive. Identifier is not included in args.
     */
    PREFIX_EXACT,
    /** 
     * Command matches when identifier occurs at the end of a chat message. Case sensitive. Identifier is not included in args.
     */
    SUFFIX_EXACT,
    /** 
     * Command matches when identifier is found anywhere in the chat message. Case sensitive. Identifier is included in args.
     */
    ANY_EXACT;
}
