package me.kavin.subgap.command.commands;

import com.jsoniter.JsonIterator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import me.kavin.subgap.Main;
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
					EmbedBuilder meb = new EmbedBuilder(Constants.AD_EMBED);
					meb.setColor(Constants.NO_COLOR_EMBED);
					meb.setTitle("Vote: ");
					StringBuilder sb = new StringBuilder("Please choose one of the choice as your argument: ");
					for (String s : channels)
						sb.append("\n>vote **" + s + "**");
					meb.setDescription(sb.toString());
					event.getChannel().sendMessage(meb.build()).queue();
				}
			} else {
				boolean voted = JsonIterator.deserialize(Unirest
						.get("https://discordbots.org/api/bots/" + Main.api.getSelfUser().getIdLong() + "/check?userId="
								+ event.getMember().getUser().getIdLong())
						.header("Authorization", Constants.DBL_TOKEN).asString().getBody()).toBoolean("voted");
				if (FirebaseUtils.canVote(id)) {
					FirebaseUtils.addVote(chosen);
					FirebaseUtils.setVoted(id);
					FirebaseUtils.setVoted(id, chosen);
					{
						EmbedBuilder meb = new EmbedBuilder(Constants.AD_EMBED);
						meb.setColor(Constants.NO_COLOR_EMBED);
						meb.setTitle("Vote: ");
						String msg = "Voted for " + chosen;
						if (!voted)
							msg += "\nYou can vote again by voting for the bot [here](https://discordbots.org/bot/"
									+ Main.api.getSelfUser().getId() + "/vote)";
						meb.setDescription(msg);
						event.getChannel().sendMessage(meb.build()).queue();
					}
				} else {
					EmbedBuilder meb = new EmbedBuilder(Constants.AD_EMBED);
					meb.setColor(Constants.NO_COLOR_EMBED);
					meb.setTitle("Vote: ");
					String msg = "You cannot vote! You have already voted in the past 24 hours!";
					if (!voted)
						msg += "\nYou can vote again by voting for the bot [here](https://discordbots.org/bot/"
								+ Main.api.getSelfUser().getId() + "/vote)";
					meb.setDescription(msg);
					event.getChannel().sendMessage(meb.build()).queue();
				}
			}
		} else {
			EmbedBuilder meb = new EmbedBuilder(Constants.AD_EMBED);
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
