package modules.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {

    private String name;
    private String description;
    private String parameters;
    private CommandType type;

    public Command(String name, String description, String parameters, CommandType type) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public boolean validate(MessageReceivedEvent e) {
        if (e.getAuthor().isBot() || e.getAuthor().isFake())
            return false;
        if (this.parameters == null || this.parameters.equals(""))
            return true;

        String[] split = this.parameters.split(" ");


        return true;
    }

}