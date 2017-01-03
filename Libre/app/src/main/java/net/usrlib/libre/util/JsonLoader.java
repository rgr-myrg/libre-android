package net.usrlib.libre.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rgr-myrg on 12/23/16.
 */

public class JsonLoader {
	public static final String NUMERICAL_ALPHABET_JS = "data/numerical_alphabet_items.json";

	public static final String loadNumericalAlphabet(final Context context) {
		final String jsonString = loadJSONFromAsset(context, NUMERICAL_ALPHABET_JS);
		return jsonString;
	}

	public static final String loadJSONFromAsset(final Context context, final String jsonFileName) {
		String jsonString = null;

		try {
			final AssetManager manager = context.getAssets();
			final InputStream inputStream = manager.open(jsonFileName);

			final int size = inputStream.available();
			final byte[] buffer = new byte[size];

			inputStream.read(buffer);
			inputStream.close();

			jsonString = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

		return jsonString;
	}
}
