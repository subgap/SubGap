package me.kavin.subgap.consts;

import java.awt.Color;
import java.text.DecimalFormat;

public class Constants {
	public static final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");

	public static final String GOOGLE_API_KEY = System.getenv().get("GOOGLE_API_KEY");

	public static final String FIREBASE_CREDENTIALS_URL = System.getenv().get("FIREBASE_CREDENTIALS_URL");

	public static final Color NO_COLOR_EMBED = new Color(54, 57, 63);

	public static final DecimalFormat df = new DecimalFormat("#,###");
}
