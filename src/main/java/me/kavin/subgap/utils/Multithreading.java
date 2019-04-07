package me.kavin.subgap.utils;

public class Multithreading {

	public static void runAsync(Runnable runnable) {
		new Thread(runnable).start();
	}

}