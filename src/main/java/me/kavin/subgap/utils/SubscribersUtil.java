package me.kavin.subgap.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import me.kavin.subgap.Main;
import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.consts.Emojis;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class SubscribersUtil {

	private static final ObjectArrayList<Channel> MONITOR_CHANNELS = new ObjectArrayList<>();

	public static void initialise() {

		MONITOR_CHANNELS.add(new Channel("PewDiePie", "UC-lHJZR3Gqxm24_Vd_AJ5Yw")); // PewDiePie
		MONITOR_CHANNELS.add(new Channel("T-Series", "UCq-Fj5jknLsUf-MWSy4_brA")); // T-Series

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				MONITOR_CHANNELS.forEach(channel -> {
					try {
						Any resp = JsonIterator.deserialize(Unirest.get(
								"https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channel.getId()
										+ "&fields=items/statistics/subscriberCount&key=" + Constants.GOOGLE_API_KEY)
								.asString().getBody());
						int subscribers = resp.get("items").asList().get(0).get("statistics").toInt("subscriberCount");
						channel.setSubscribers(subscribers);
					} catch (Exception e) {
					}
				});
			}
		}, 0, TimeUnit.SECONDS.toMillis(30));

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ObjectArrayList<Channel> channels = SubscribersUtil.getChannels();
				Collections.sort(channels, new Comparator<Channel>() {
					@Override
					public int compare(Channel o1, Channel o2) {
						return o2.getSubscribers() - o1.getSubscribers();
					}
				});
				if (channels.get(0).getSubscribers() - channels.get(1).getSubscribers() <= 15000) {
					EmbedBuilder meb = new EmbedBuilder();
					meb.setTitle("Sub Gap: LOW SUB GAP");
					meb.setColor(Constants.NO_COLOR_EMBED);

					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < channels.size(); i++) {
						Channel channel = channels.get(i);
						if (i == 0)
							sb.append(channel.getName() + " - " + channel.getSubscribersDisplay() + " Subscribers "
									+ Emojis.FIRST_PLACE);
						else
							sb.append("\n" + channel.getName() + " - " + channel.getSubscribersDisplay()
									+ " Subscribers " + Emojis.MEDAL);
					}

					sb.append("\n" + "Sub Gap for First Place: "
							+ Constants.df.format(channels.get(0).getSubscribers() - channels.get(1).getSubscribers())
							+ " Subscribers");

					meb.setDescription(String.valueOf(sb));

					for (TextChannel channel : Main.api.getTextChannelsByName("Sub-Gap", true))
						try {
							channel.sendMessage(meb.build()).queue();
						} catch (Exception e) {
						}
				}
			}
		}, TimeUnit.SECONDS.toMillis(10), TimeUnit.MINUTES.toMillis(2));

		long time = System.currentTimeMillis();
		long delay = time - time - (time % TimeUnit.HOURS.toMillis(1)) + TimeUnit.HOURS.toMillis(1);

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ObjectArrayList<Channel> channels = SubscribersUtil.getChannels();
				Collections.sort(channels, new Comparator<Channel>() {
					@Override
					public int compare(Channel o1, Channel o2) {
						return o2.getSubscribers() - o1.getSubscribers();
					}
				});
				EmbedBuilder meb = new EmbedBuilder();
				meb.setTitle("Sub Gap: Hourly Update");
				meb.setColor(Constants.NO_COLOR_EMBED);

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

				for (TextChannel channel : Main.api.getTextChannelsByName("Sub-Gap", true))
					try {
						channel.sendMessage(meb.build()).queue();
					} catch (Exception e) {
					}
			}
		}, delay, TimeUnit.HOURS.toMillis(1));
	}

	public static ObjectArrayList<Channel> getChannels() {
		return MONITOR_CHANNELS;
	}
}
