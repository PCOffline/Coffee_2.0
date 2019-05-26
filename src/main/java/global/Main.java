package global;

import crypto.Secret;
import net.dv8tion.jda.core.*;
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
        Guild guild = e.getGuild();
        String first = split[0];
        String lowerCase = contentRaw.toLowerCase();
        Member member = e.getMember();

        if (guild == null || messageChannel.getType().equals(ChannelType.PRIVATE)) {
            return;
        }

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member mentionedMember = mentionedMembers != null && !mentionedMembers.isEmpty() ? mentionedMembers.get(0) : null;
        User user = mentionedMember != null ? mentionedMember.getUser() : e.getAuthor();
        String avatarUrl = user.getAvatarUrl();

        if (first.matches(regex)) {
            messageChannel.sendMessage(asMention + " " + Strings.magicShellYesNo[new Random().nextInt(Strings.magicShellYesNo.length)]).queue();
        }

        if (first.equalsIgnoreCase(Strings.PREFIX + "vote")
                && split.length > 1) {
            messageChannel.addReactionById(messageId, "\u2705").queue();
            messageChannel.addReactionById(messageId, "\u274C").queue();
        }

        if (lowerCase.equalsIgnoreCase("i love coffee") || (lowerCase.contains("i") && lowerCase.contains("love") && lowerCase.contains("coffee"))) {
            guild.getController().addSingleRoleToMember(member, guild.getRoleById(578981415027474462L)).queue();
            messageChannel.sendMessage("aww ty :blush:").queue();
        }

        if (first.equalsIgnoreCase(Strings.PREFIX + "game") && split.length == 1) {

            e.getMember().getVoiceState().getChannel().putPermissionOverride(guild.getRolesByName("usa", true).get(0)).setDeny(Permission.VOICE_USE_VAD).queue();
        }

        if (messageChannel.getIdLong() == 574276000414826506L && first.equalsIgnoreCase(Strings.PREFIX + "class")) {
            if ((mentionedMembers != null ? mentionedMembers.size() : 0) == 1) {
                messageChannel.sendMessage(embed((mentionedMember != null ? mentionedMember.getEffectiveName() : null) + " Classes", null, null, ResourceWriter.getClasses(user.getIdLong()), Color.cyan, null, null, avatarUrl, null)).queue();
            } else {
                MessageAction invalidUseOfClassCommand = messageChannel.sendMessage("Invalid use of class command");
                if (split.length < 3)
                    invalidUseOfClassCommand.queue();
                else {
                    if (split.length > 3) {
                        switch (split[1].toLowerCase()) {
                            case "inf":
                            case "infantry":
                                ResourceWriter.addToClass(0);
                                ResourceWriter.addToClass(0, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "para":
                            case "parachute":
                            case "paratrooper":
                                ResourceWriter.addToClass(1);
                                ResourceWriter.addToClass(1, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "recon":
                            case "sniper":
                                ResourceWriter.addToClass(2);
                                ResourceWriter.addToClass(2, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "tank":
                            case "tanker":
                                ResourceWriter.addToClass(3);
                                ResourceWriter.addToClass(3, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "pilot":
                            case "plane":
                            case "fighter":
                            case "fighterpilot":
                                ResourceWriter.addToClass(4);
                                ResourceWriter.addToClass(4, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
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
                        messageChannel.sendMessage("Successfully added " + split[1] + " to your classes!").queue();
                    else
                        invalidUseOfClassCommand.queue();
                }
            }
        }

        if (split.length == 1 && contentRaw.equalsIgnoreCase(Strings.PREFIX + "classes"))
            messageChannel.sendMessage(embed("Classes", null, null, ResourceWriter.getClasses(), Color.GRAY, null, null, null, null)).queue();

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