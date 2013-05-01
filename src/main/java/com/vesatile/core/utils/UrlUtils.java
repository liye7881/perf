package com.vesatile.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class UrlUtils {
	private static Logger logger = Logger.getLogger(UrlUtils.class);

	public static String retrieve(String url) {
		StringBuffer body = new StringBuffer();
		BufferedReader reader = null;
		try {
			URL uriObject = new URL(url);
			reader = new BufferedReader(new InputStreamReader(
					uriObject.openStream()));
		} catch (MalformedURLException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return body.toString();
	}
}
