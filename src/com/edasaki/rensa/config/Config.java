package com.edasaki.rensa.config;

public enum Config {
    BOT_TOKEN("App Bot User Token", "bot_token"),
    GUILD_ID("Primary Guild ID", "guild_id");

    private String displayName;
    private String storedName;

    Config(String displayName, String storedName) {
        this.displayName = displayName;
        this.storedName = storedName;
    }

    public String getStoredName() {
        return storedName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
