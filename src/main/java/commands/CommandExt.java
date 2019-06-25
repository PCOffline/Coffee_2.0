package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import global.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
abstract class CommandExt extends Command {

    static final Category OWNER = new Category("Owner");
    static final Category HNG = new Category("Heroes & Generals");
    static final Category FUN = new Category("Fun");
    static final Category MOD = new Category("Moderation");
    static final Category UTIL = new Category("Tools & Utilities");
    String description;
    String usage;
    String example;
    long[] channels = {Constants.COFFEE, Constants.COMMANDS};

    private int isArgs() {
        boolean req = usage.contains("<");
        boolean opt = usage.contains("[");
        if (!req && !opt)
            return 0;
        if (opt && !req)
            return 1;
        if (!opt)
            return 2;
        if (usage.indexOf('<') < usage.indexOf('['))
            return 3;
        if (usage.indexOf('<') > usage.indexOf('['))
            return 4;
        return -1;
    }

    private String getAvailableChannels() {
        StringBuilder message = new StringBuilder();
        for (long ch : channels) {
            message.append(Main.getChannel(ch).getAsMention()).append(" ");
        }
        return message.toString();
    }

    private boolean prevalidate(CommandEvent event) {
        List<Role> roles = event.getMember().getRoles();
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        int length = event.getMessage().getContentRaw().split(" ").length;
        int args = isArgs();

        if (args == 0) {
            if (length > 1) {
                event.replyError("This command doesn't require arguments");
                return false;
            }
        } else if ((args == 2 || args == 3) && length <= 1) {
            event.replyError("This command requires arguments");
            return false;
        }
        if (channels != null && guildOnly && !Arrays.asList(channels).contains(channel.getId())
                && !roles.contains(guild.getRoleById(Constants.ADMIN)) && !roles.contains(guild.getRoleById(Constants.MODERATOR))) {
            event.getMessage().delete().queue();
            channel.sendMessage("You cannot use this command here!\n"
                    + "Available channels: " + getAvailableChannels()).queue(message -> new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    message.delete().queue();
                }
            }, Constants.DELAY));
            return false;
        }
        return true;
    }

    boolean validate(CommandEvent event) {
        return !prevalidate(event);
    }

}
