package me.kavin.subgap.consts;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

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
	}

	public static final String BOT_TOKEN;

	public static final String GOOGLE_API_KEY;

	public static final String FIREBASE_CREDENTIALS_URL;

	public static final Color NO_COLOR_EMBED = new Color(54, 57, 63);

	public static final DecimalFormat df = new DecimalFormat("#,###");
}
