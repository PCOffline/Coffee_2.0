package global;

import crypto.Secret;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    private String id;

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(Secret.TOKEN).build().awaitReady();
        jda.addEventListener(new Main());

        jda.setAutoReconnect(true);
        jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.playing("Drinking Tea"));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        RestAction<PrivateChannel> privateChannelRestAction = user.openPrivateChannel();

        privateChannelRestAction.queue(channel -> {
            String asMention = user.getAsMention();

            channel.sendMessage(Strings.WELCOME.replace("_USER_", asMention)).queue((message -> {
                message.addReaction("\uD83D\uDC4C").queue();
                this.id = message.getId();
            }));
        });
    }

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent e) {
        User user = e.getUser();
        String name = e.getReaction().getReactionEmote().getName();
        boolean idEquals = e.getMessageId().equals(id);
        boolean nameEquals = name.equals("\uD83D\uDC4C");

        if (!user.isBot() && nameEquals && idEquals) {
            Guild guild = user.getMutualGuilds().get(0);
            GuildController controller = guild.getController();
            Member member = guild.getMember(user);
            Role role = guild.getRoleById("571626241216610304");

            controller.addSingleRoleToMember(member, role).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String regex = "\\" + Strings.PREFIX + "(ms|magicshell)";
        Message message = e.getMessage();
        String contentRaw = message.getContentRaw();
        String[] split = contentRaw.split(" ");
        MessageChannel messageChannel = e.getChannel();
        User author = e.getAuthor();
        String asMention = author.getAsMention();
        String messageId = message.getId();
        List<Member> mentionedMembers = message.getMentionedMembers();

        if (split[0].matches(regex)) {
            messageChannel.sendMessage(asMention + " " + Strings.magicShellYesNo[new Random().nextInt(Strings.magicShellYesNo.length)]).queue();
        }

        if (split[0].equalsIgnoreCase(Strings.PREFIX + "vote")
                && split.length > 1) {
            messageChannel.addReactionById(messageId, "\u2705").queue();
            messageChannel.addReactionById(messageId, "\u274C").queue();
        }

        String s = contentRaw.toLowerCase();
        if (s.equalsIgnoreCase("i love coffee") || (s.contains("I") && s.contains("love") && s.contains("coffee"))) {
            e.getGuild().getController().addSingleRoleToMember(e.getMember(), e.getGuild().getRoleById(578981415027474462L)).queue();
            e.getChannel().sendMessage("aww ty :blush:").queue();
        }

        if (messageChannel.getIdLong() == 574276000414826506L && split[0].equalsIgnoreCase(Strings.PREFIX + "class")) {
            if (mentionedMembers.size() == 1) {
                Member member = mentionedMembers.get(0);
                User user = member.getUser();
                String avatarUrl = user.getAvatarUrl();
                e.getChannel().sendMessage(embed(member.getEffectiveName() + " Classes", null, null, ResourceWriter.getClasses(user.getIdLong()), Color.cyan, null, null, avatarUrl, null)).queue();
            } else {
                MessageAction invalidUseOfClassCommand = e.getChannel().sendMessage("Invalid use of class command");
                if (split.length < 3)
                    invalidUseOfClassCommand.queue();
                else {
                    if (split.length > 3) {
                        switch (split[1].toLowerCase()) {
                            case "inf":
                            case "infantry":
                                ResourceWriter.addToClass(0);
                                ResourceWriter.addToClass(0, e.getAuthor().getIdLong(), split[2], contentRaw.substring(split[0].length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "para":
                            case "parachute":
                            case "paratrooper":
                                ResourceWriter.addToClass(1);
                                ResourceWriter.addToClass(1, e.getAuthor().getIdLong(), split[2], contentRaw.substring(split[0].length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "recon":
                            case "sniper":
                                ResourceWriter.addToClass(2);
                                ResourceWriter.addToClass(2, e.getAuthor().getIdLong(), split[2], contentRaw.substring(split[0].length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "tank":
                            case "tanker":
                                ResourceWriter.addToClass(3);
                                ResourceWriter.addToClass(3, e.getAuthor().getIdLong(), split[2], contentRaw.substring(split[0].length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "pilot":
                            case "plane":
                            case "fighter":
                            case "fighterpilot":
                                ResourceWriter.addToClass(4);
                                ResourceWriter.addToClass(4, e.getAuthor().getIdLong(), split[2], contentRaw.substring(split[0].length() + split[1].length() + split[2].length() + 3));
                                break;
                            default:
                                invalidUseOfClassCommand.queue();
                                return;
                        }
                    } else if (split[2].equalsIgnoreCase("18")) {
                        ResourceWriter.addToClass(5);
                        switch (split[1].toLowerCase()) {
                            case "inf":
                            case "infantry":
                                ResourceWriter.addToClass(5, e.getAuthor().getIdLong());
                                break;
                            case "para":
                            case "parachute":
                            case "paratrooper":
                                ResourceWriter.addToClass(1, e.getAuthor().getIdLong());
                                break;
                            case "recon":
                            case "sniper":
                                ResourceWriter.addToClass(2, e.getAuthor().getIdLong());
                                break;
                            case "tank":
                            case "tanker":
                                ResourceWriter.addToClass(3, e.getAuthor().getIdLong());
                                break;
                            case "pilot":
                            case "plane":
                            case "fighter":
                            case "fighterpilot":
                                ResourceWriter.addToClass(4, e.getAuthor().getIdLong());
                                break;
                            default:
                                invalidUseOfClassCommand.queue();
                                return;
                        }
                    } else {
                        invalidUseOfClassCommand.queue();
                        return;
                    }

                    if (split[2].matches("([1-9]|1[0-8])"))
                        e.getChannel().sendMessage("Successfully added " + split[1] + " to your classes!").queue();
                    else
                        invalidUseOfClassCommand.queue();
                }
            }
        }


    }

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        Member member = e.getMember();
        Game game = member.getGame();
        String hng = "573827985103388687";
        GuildController controller = e.getGuild().getController();
        Role role = e.getGuild().getRoleById(hng);
        String regex = "Heroes (and|&) Generals( WWII)?";

        if (game != null && (game.getName().matches(regex)))
            controller.addSingleRoleToMember(member, role).queue();
        else if (member.getRoles().contains(role))
            controller.removeSingleRoleFromMember(member, role).queue();
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        if (e.getRoles().get(0).getId().equals("531475341244366848")) {
            TextChannel channel = e.getGuild().getTextChannelById("574276000414826506");
            Member member = e.getMember();
            channel.sendMessage(embed("Classes & Assault Teams", null, null, "Welcome " + member.getAsMention() + " to USA! Please write down your classes in the next format: \n`" + Strings.PREFIX + "class Class Type (Infantry, Paratrooper, Recon, Tanker, Pilot) | Rank | Main Weapon`\n **Example: **`" + Strings.PREFIX + "class Infantry 12 M3 Grease Gun`\n Please do this for each class you have.\n The format for assault teams is: \n`" + Strings.PREFIX + "at AT Type Level Quantity of Assault Teams of that Type & Level`\n**Example: **`" + Strings.PREFIX + "at recon 1 20`", Color.green, null, null, null, null)).queue();
        }
    }


    private MessageEmbed embed(String title, String titleURL, String img, String content, Color color, String footer, String footerURL, String thumbnail, String author) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title, titleURL);
        builder.setImage(img);
        builder.setDescription(content);
        builder.setColor(color);
        builder.setFooter(footer, footerURL);
        builder.setThumbnail(thumbnail);
        builder.setAuthor(author);
        return builder.build();
    }
}