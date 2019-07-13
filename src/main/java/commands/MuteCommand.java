package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import global.FileWrite;
import net.dv8tion.jda.core.Permission;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MuteCommand extends ModCommand {

    public MuteCommand() {
        super();
        this.arguments = "<member> [duration] [reason]";
        this.usage = Constants.PREFIX + this.name + this.arguments;
        this.example = description.replace(arguments, Constants.OWNER_AS_MENTION + " 10m he's too cool");
        this.category = MOD;
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.help = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.guildOnly = true;
    }

    @Override
    protected void modLog(CommandEvent commandEvent) {
        String[] args = commandEvent.getArgs().split(" ");
        StringBuilder reason = new StringBuilder();
        for (String s : args) {
            reason.append(s.equals(args[0]) ? "" : s);
        }
        commandEvent.getGuild().getTextChannelById(Constants.MOD_LOG).sendMessage(Constants.MUTE + "**MUTED** " + args[0] + (args.length > 1 ? " for " + reason.toString() : "") + " by " + commandEvent.getAuthor().getAsMention()).queue();
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] args = commandEvent.getArgs().split(" ");
        if (!validate(commandEvent) || (args.length > 1 && !args[1].matches("(\\d+)((?:d(?:ay(?:s)?)?)|(?:h(?:our(?:s)?)?)|(?:m(?:in(?:ute(?:s)?)?)?))"))) {
            return;
        }
        String id = args[0].replace("<@!", "").replace(">", "");
        FileWrite.writeFile(Constants.MUTED, id + ": " + (args.length > 1 ? addTime(commandEvent.getMessage().getCreationTime(), args[1]).format(DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm").withZone(ZoneId.of("Europe/London"))) : "-1"));
        modLog(commandEvent);
    }


    private OffsetDateTime addTime(OffsetDateTime time, String plus) {
        String substring = plus.substring(1);
        if (substring.matches("(?:m(?:in(?:ute(?:s)?)?)?)"))
            return time.plusMinutes((long) plus.charAt(0) - 48);
        else if (substring.matches("(?:h(?:our(?:s)?)?)"))
            return time.plusHours((long) plus.charAt(0) - 48);
        else if (substring.matches("(?:d(?:ay(?:s)?)?)"))
            return time.plusDays((long) plus.charAt(0) - 48);
        return time;
    }

}