package Global;

import crypto.Secret;
import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static Global.Strings.WELCOME;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder(Secret.token).build();
        jda.addEventListener(new Main());

        jda.setAutoReconnect(true);
        jda.getPresence().setPresence(OnlineStatus.ONLINE, RichPresence.playing("Drinking Tea").asRichPresence());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < jda.getGuildById("531471489220870166").getMembers().size(); i++) {
                        if (jda.getGuildById("531471489220870166") != null && jda.getGuildById("531471489220870166").getMembers() != null && (jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes & Generals") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes & Generals WWII") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes and Generals") || jda.getGuildById("531471489220870166").getMembers().get(i).getGame().getName().equalsIgnoreCase("Heroes and Generals WWII")))
                            jda.getGuildById("531471489220870166").getController().addRolesToMember(jda.getGuildById("531471489220870166").getMembers().get(i), jda.getRolesByName("Heroes & Generals", true).get(0)).complete();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 15000);

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(WELCOME.replace("_USER_", e.getUser().getAsMention())).queue());
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
    public void onFriendRequestReceived(FriendRequestReceivedEvent e) {
        e.getFriendRequest().accept().queue();
    }
}