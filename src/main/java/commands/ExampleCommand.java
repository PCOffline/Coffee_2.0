package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import net.dv8tion.jda.core.Permission;

public class ExampleCommand extends CommandExt {

    public ExampleCommand() {
        this.name = "name";
        this.aliases = new String[]{"alias"};
        this.arguments = "[argument]";
        this.description = "description";
        this.usage = Constants.PREFIX + this.name + " " + arguments;
        this.example = description.replace(arguments, "value");
        this.category = OWNER;
        this.botPermissions = new Permission[]{Permission.MESSAGE_READ};
        this.userPermissions = new Permission[]{Permission.MESSAGE_READ};
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.ownerCommand = true;
        this.requiredRole = "role";
        this.guildOnly = true;
        this.help = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

    }
}
