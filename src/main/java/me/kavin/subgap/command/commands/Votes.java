package me.kavin.subgap.command.commands;

import java.util.Collections;
import java.util.Comparator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.subgap.command.Command;
import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.consts.Emojis;
import me.kavin.subgap.utils.Channel;
import me.kavin.subgap.utils.SubscribersUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Votes extends Command {

	public Votes() {
		super(">votes", "`Displays the votes for PewDiePie vs T-Series!`");
	}

	@Override
	public void onCommand(String string, MessageReceivedEvent event) throws Throwable {
		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setTitle("Sub Gap: Command");
			meb.setColor(Constants.NO_COLOR_EMBED);
			ObjectArrayList<Channel> channels = SubscribersUtil.getChannels();
			Collections.sort(channels, new Comparator<Channel>() {
				@Override
				public int compare(Channel o1, Channel o2) {
					try {
						return o2.getVotes() - o1.getVotes();
					} catch (Exception e) {
						return 0;
					}
				}
			});

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < channels.size(); i++) {
				Channel channel = channels.get(i);
				if (i == 0)
					sb.append(channel.getName() + " - " + channel.getVotesDisplay() + " Votes " + Emojis.FIRST_PLACE);
				else
					sb.append("\n" + channel.getName() + " - " + channel.getVotesDisplay() + " Votes " + Emojis.MEDAL);
			}

			sb.append("\n" + "Vote difference for First Place: "
					+ Constants.df.format(channels.get(0).getVotes() - channels.get(1).getVotes()) + " Votes!");

			meb.setDescription(String.valueOf(sb));

			event.getChannel().sendMessage(meb.build()).queue();
		}
	}
}
