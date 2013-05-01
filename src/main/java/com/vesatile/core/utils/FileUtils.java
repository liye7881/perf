package com.vesatile.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static List<String> read(InputStream inputStream) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String content = null;
		while ((content = reader.readLine()) != null) {
			result.add(content);
		}

		return result;
	}
}
