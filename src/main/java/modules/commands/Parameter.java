package modules.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Parameter {

    private String name;
    private ParameterType type;
    private boolean condition;
    private Parameter[] children;

    Parameter(String name, ParameterType type, String condition, MessageReceivedEvent e) {
        this.name = name;
        this.type = type;
        this.condition = stringToBool(condition, e);
    }

    private boolean stringToBool(String condition, MessageReceivedEvent e) {
        return true;
    }
}
