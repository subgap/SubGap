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

public class SubGap extends Command {

	public SubGap() {
		super(">subgap", "`Displays the subgap between PewDiePie and T-Series!`");
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
					return o2.getSubscribers() - o1.getSubscribers();
				}
			});

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < channels.size(); i++) {
				Channel channel = channels.get(i);
				if (i == 0)
					sb.append(channel.getName() + " - " + channel.getSubscribersDisplay() + " Subscribers "
							+ Emojis.FIRST_PLACE);
				else
					sb.append("\n" + channel.getName() + " - " + channel.getSubscribersDisplay() + " Subscribers "
							+ Emojis.MEDAL);
			}

			sb.append("\n" + "Sub Gap for First Place: "
					+ Constants.df.format(channels.get(0).getSubscribers() - channels.get(1).getSubscribers())
					+ " Subscribers");

			meb.setDescription(String.valueOf(sb));

			event.getChannel().sendMessage(meb.build()).queue();
		}
	}
}
