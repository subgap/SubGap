package me.kavin.subgap.consts;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

import net.dv8tion.jda.api.EmbedBuilder;

public class Constants {

	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		BOT_TOKEN = prop.getProperty("BOT_TOKEN");
		GOOGLE_API_KEY = prop.getProperty("GOOGLE_API_KEY");
		FIREBASE_CREDENTIALS_URL = prop.getProperty("FIREBASE_CREDENTIALS_URL");
		DBL_TOKEN = prop.getProperty("DBL_TOKEN");
	}

	public static final String BOT_TOKEN;

	public static final String GOOGLE_API_KEY;

	public static final String FIREBASE_CREDENTIALS_URL;

	public static final String DBL_TOKEN;

	public static final Color NO_COLOR_EMBED = new Color(54, 57, 63);

	public static final DecimalFormat df = new DecimalFormat("#,###");

	public static final EmbedBuilder AD_EMBED = new EmbedBuilder(Constants.AD_EMBED).setFooter(
			"Powered by sparkedhost.us",
			"https://cdn.discordapp.com/icons/392881838605205504/40ca03d4e1a1f066ad7a68e9af516cd9.png");
}
