package net.usrlib.libre.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by rgr-myrg on 12/8/16.
 */

public class UiUtil {
//	public static final void showNewItemAlertDialog(
//			final Context context,
//			final String defaultValue,
//			final OnShowNewItemAlertDialog callback) {
//
//		final View dialogView = LayoutInflater
//				.from(context)
//				.inflate(R.layout.enter_item_dialog, null);
//
//		final EditText itemDialogInput = (EditText) dialogView
//				.findViewById(R.id.enter_item_dialog_input);
//
//		if (defaultValue != null && !defaultValue.isEmpty()) {
//			itemDialogInput.setText(defaultValue);
//		}
//
//		new AlertDialog.Builder(context)
//				.setView(dialogView)
//				.setCancelable(false)
//				.setPositiveButton("OK", (DialogInterface dialog, int id) -> {
//					callback.run(String.valueOf(itemDialogInput.getText()));
//				})
//				.setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
//					dialog.cancel();
//				})
//				.show();
//	}

	public static final void showAlertDialog(
			final Context context,
			final String title,
			final String message,
			final OnShowAlertDialog callback) {

		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", (DialogInterface dialog, int id) -> {
					callback.run(true);
				})
				.setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
					callback.run(false);
					dialog.cancel();
				})
				.show();
	}

	public static void makeSnackbar(final View view, final String message) {
		Snackbar.make(view, message, Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
	}

	public interface OnShowNewItemAlertDialog {
		void run(String value);
	}

	public interface OnShowAlertDialog {
		void run(boolean value);
	}
}
