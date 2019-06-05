package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;

public class VoteCommand extends CommandExt {

    public VoteCommand() {
        this.name = "vote";
        this.aliases = new String[]{"poll"};
        this.arguments = "<content>";
        this.category = UTIL;
        this.description = "Generate a poll with " + Constants.UPVOTE + " and " + Constants.DOWNVOTE + " reactions.";
        this.usage = Constants.PREFIX + this.name + " " + this.arguments;
        this.example = Constants.PREFIX + name + " Should Coffee drink more tea?";
        this.help = "`" + this.usage + "`" + "\n" + this.description;

    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (validate(commandEvent))
            return;
        commandEvent.getMessage().addReaction(Constants.UPVOTE).queue();
        commandEvent.getMessage().addReaction(Constants.DOWNVOTE).queue();
    }

}
