# Rensa
A multi-purpose Discord bot written in Java.

Find us on Discord!

[<img src="https://discordapp.com/api/guilds/125788425793044480/widget.png?style=shield">](https://discord.gg/0hYtpOLWHxKLWOQL)  


#Core Features
- Modularized design to easily expand using `AbstractManager` and `AbstractCommand`.
- Multi-threaded feature handling for smooth responses.
- Buffered messaging and moderation to avoid Discord's rate limits.
- Anti-clutter message management that deletes unnecessary messages from interactions with Rensa to keep chat clean and readable.

#Built-In Features (Commands)
###ChatterBot
- Allows Rensa to have human-like conversations with users using Cleverbot or PandoraBots (defaults to PandoraBot).
- "Thinks" on a separate thread to not slow other tasks.
- Creates separate sessions for each user for uninterrupted conversations.
- Terminates inactive chat sessions to avoid excess memory usage.
- Automatically parses out @mentions and #channel-mentions to their display text (i.e. reads a user's name instead of their ID).

###The Casino - `!balance`, `!gift`, `!coinflip`
- A set of fun games to allow players on your Discord server to earn virtual currency.
- Get a small amount of free currency from `!gift` to keep playing!
- Flip a coin to double a bet or lose it all.

###Hangman - `!hangman`, `!hguess`, `!hanswer`
- A Discord version of the classic Hangman game. Guess the word, but make too many mistakes and you lose!
- Rewards players with Casino currency based on their performance.

###Anti-bullying - `!bully`
- No bullying!

###Status
- Returns the status of the Zentrela Minecraft server.

##How do I use this?
If you're a developer, just clone this repo and compile with `mvn clean compile assembly:single` for a single JAR file with all dependencies included. You should be able to run it with a simple `java -jar rensa.jar` from command line.

If you're not a developer:

1. Instructions coming soon!

Rensa is NOT designed to be a multi-guild bot! Rensa will probably work even while connected to multiple guilds, but behavior will be more buggy. The intention is for Rensa to be run separately by each person who would like to use it. This also ensures better privacy, as there will be no centralized bot reading your guild's messages.

##Writing your own commands
`com.edasaki.rensa.abstracts.AbstractCommand` was designed to be an easy way to add versatile commands!

1. Create a new class that `extends AbstractCommand`.
2. Create a constructor (most commands will just call `super(identifier, type);`)
3. Implement the execute() method.
4. Your class should look like this:
  ```
  public class TestCommand extends AbstractCommand {
    public TestCommand(String identifier, CommandMatchType type) {
      super(identifier, type);
    }
    
    @Override
    protected void execute(String[] args, IMessage msg) {
      // command content
    }
  }
  ```
5. Code whatever is needed in your execution. For example, the following code will respond to the command with a "Hello!" in the channel the command was received.
  
  ```
  @Override
  protected void execute(String[] args, IMessage msg) {
    Rensa.getInstance().sendMessage(msg.getChannel(), "Hello!");
  }
  ```
6. Go to `com.edasaki.rensa.commands.CommandManager` and use `register(AbstractCommand)` to set up your command.
7. When instantiating your command, the first argument (String) is the phrase that will trigger the command (aka "identifier"). This can be one word or multiple - for consistency with the majority of other bots, I suggest choosing an identifier that's one word with a prefix, such as `!help`.
8. The second argument is a value from the `CommandMatchType` enum. You can read the Javadocs for the enum to see what each prefix does. The most common `CommandMatchType` is `CommandMatchType.PREFIX` which matches commands when at the front of the message and is case-insensitive.
9. Here is an example of what a register looks like for a command that matches messages starting with "!test":

  ```
    register(new TestCommand("!test", CommandMatchType.PREFIX));
  ```
10. You're all set! After the command has been registered, Rensa will check for it and respond when it is used.

##Organizing more advanced extensions
`com.edasaki.rensa.abstracts.AbstractManager` was designed to be an easy way to extend Rensa with your own code. Simply create a class extending `AbstractManager` and implement the very simple `initialize()` method.

After you have created your class, add it to Rensa by going to `com.edasaki.rensa.RensaLauncher` and adding a line formatted this (replace AFancyNewManager with the name of your new class) to the `main` method:

```
  AbstractManager.create(AFancyNewManager.class);
```

##I don't like the built-in commands. Can I remove them or change their identifiers?
Right now the only way to change the built-in commands is by editing the `CommandManager` and recompiling.

Sorry it's a bit troublesome! Better modularity will be added in the future.

##Development

Rensa is constantly in development, and changes may include major restructures of code.

In its current state, Rensa is not particularly friendly for non-programmers as modification of commands requires editing code. I plan to add support for "drag-and-drop" plugins in the future, so that new commands/features can be added by simply dropping a JAR into a sub-directory.

New commands/features will be added directly to Rensa. Please feel free to make pull requests for new features you have created that you think would be appropriate for Rensa!

##Other Info

Rensa was originally created for the [Zentrela Minecraft server](http://www.zentrela.net), an extensive RPG in Minecraft complete with quests, bosses, dungeons, trading, and much more! If you like RPGs and have Minecraft, be sure to check it out! It's a totally different experience from normal Minecraft.
