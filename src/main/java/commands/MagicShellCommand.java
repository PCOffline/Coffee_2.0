package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import net.dv8tion.jda.core.Permission;

import java.util.Random;

import static global.Constants.MAGICSHELL;

public class MagicShellCommand extends CommandExt {

    public MagicShellCommand() {
        this.name = "magicshell";
        this.aliases = new String[]{"ms"};
        this.arguments = "<question>";
        this.description = "Answers a Yes or No question, just like a magic shell";
        this.usage = Constants.PREFIX + this.name + " " + arguments;
        this.example = description.replace(arguments, "is @FPGa#9995 cool?");
        this.category = FUN;
        this.botPermissions = new Permission[]{};
        this.userPermissions = new Permission[]{};
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.ownerCommand = false;
        this.guildOnly = false;
        this.help = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(MAGICSHELL[new Random().nextInt(MAGICSHELL.length)]);
    }
}
