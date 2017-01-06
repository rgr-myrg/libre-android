package net.usrlib.libre.util;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by rgr-myrg on 12/20/16.
 */

public class AdRequestUtil {
	public static final void makeAdRequest(final AdView adView) {
		if (adView == null) {
			return;
		}

		// Create an ad request.
		// Use .addTestDevice(AdRequestUtil.DEVICE_ID_EMULATOR) for testing
		// And check logcat output for the hashed device ID to
		// get test ads on a physical device. e.g. sample output:
		// "Use AdRequestUtil.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

		final AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.addTestDevice("F3F46ADC2A7E17C2BB778F660A2E59F8")
				.build();

		adView.loadAd(adRequest);
	}
}
