package me.kavin.subgap.command.commands;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.subgap.command.Command;
import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.utils.FirebaseUtils;
import me.kavin.subgap.utils.SubscribersUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Vote extends Command {

	public Vote() {
		super(">vote", "`Allows you to vote for who you think should be number one!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Throwable {
		ObjectArrayList<String> channels = new ObjectArrayList<>();
		SubscribersUtil.getChannels().forEach(channel -> channels.add(channel.getName()));
		String[] split = message.split(" ");
		if (split.length > 1) {
			long id = event.getAuthor().getIdLong();
			String chosen = null;
			for (String s : channels)
				if (split[1].equalsIgnoreCase(s)) {
					chosen = s;
					break;
				}
			if (chosen == null) {
				{
					EmbedBuilder meb = new EmbedBuilder();
					meb.setColor(Constants.NO_COLOR_EMBED);
					meb.setTitle("Vote: ");
					StringBuilder sb = new StringBuilder("Please choose one of the choice as your argument: ");
					for (String s : channels)
						sb.append("\n>vote **" + s + "**");
					meb.setDescription(sb.toString());
					event.getChannel().sendMessage(meb.build()).queue();
				}
			} else {
				if (FirebaseUtils.canVote(id)) {
					FirebaseUtils.addVote(chosen);
					FirebaseUtils.setVoted(id);
					{
						EmbedBuilder meb = new EmbedBuilder();
						meb.setColor(Constants.NO_COLOR_EMBED);
						meb.setTitle("Vote: ");
						meb.setDescription("Voted for " + chosen);
						event.getChannel().sendMessage(meb.build()).queue();
					}
				} else {
					EmbedBuilder meb = new EmbedBuilder();
					meb.setColor(Constants.NO_COLOR_EMBED);
					meb.setTitle("Vote: ");
					// TODO: Allow voting again if they vote for the bot.
					meb.setDescription("You cannot vote! You have already voted in the past 24 hours!");
					event.getChannel().sendMessage(meb.build()).queue();
				}
			}
		} else {
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(Constants.NO_COLOR_EMBED);
			meb.setTitle("Vote: ");
			StringBuilder sb = new StringBuilder("Please choose one of the choice as your argument: ");
			for (String s : channels)
				sb.append("\n>vote **" + s + "**");
			meb.setDescription(sb.toString());
			event.getChannel().sendMessage(meb.build()).queue();
		}
	}

}
