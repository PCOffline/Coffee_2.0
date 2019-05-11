package Global;

import crypto.Secret;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    private static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = new JDABuilder(Secret.token).build().awaitReady();
        jda.addEventListener(new Main());

        jda.setAutoReconnect(true);
        jda.getPresence().setPresence(OnlineStatus.ONLINE, RichPresence.playing("Drinking Tea").asRichPresence());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(Strings.WELCOME.replace("_USER_", e.getUser().getAsMention())).queue());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(Strings.prefix + "magicshell")
                || e.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(Strings.prefix + "ms")) {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " " + Strings.magicShellYesNo[new Random().nextInt(Strings.magicShellYesNo.length)]).queue();
        }

        if (e.getMessage().getContentRaw().equals(Secret.key)) {
            e.getMessage().delete().queue();
            e.getMember().getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Hello master, how shall I serve you?").queue());
        }



    }

    @Override
    public void onReady(ReadyEvent e) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < jda.getGuildById("531471489220870166").getMembers().size(); i++) {
                    if (jda.getGuildById("531471489220870166").getMembers() != null && jda.getGuildById("531471489220870166").getMembers().get(i).getGame() != null && jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes & Generals") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes & Generals WWII") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes and Generals") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes and Generals WWII"))
                        jda.getGuildById("531471489220870166").getController().addRolesToMember(jda.getGuildById("531471489220870166").getMembers().get(i), jda.getGuildById("531471489220870166").getRolesByName("Heroes & Generals", true).get(0)).queue();
                    else
                        jda.getGuildById("531471489220870166").getController().removeRolesFromMember(jda.getGuildById("531471489220870166").getMembers().get(i), jda.getGuildById("531471489220870166").getRolesByName("Heroes & Generals", true).get(0)).queue();
                }

            }
        }, 5000);
    }

}