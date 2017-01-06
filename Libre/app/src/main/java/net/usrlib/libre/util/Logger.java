package net.usrlib.libre.util;

import android.util.Log;

/**
 * Created by rgr-myrg on 1/4/17.
 */

public class Logger {
	public static final void i(String tag, String msg) {
		msg = String.format("[%s] %s", tag, msg);
		Log.i("APP", msg);
	}
}
