
package scibd.lab.vms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessage {

    public static void showMessage(final Context c, final String title, final String s) {
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(c);
        aBuilder.setTitle(title);
        aBuilder.setIcon(R.drawable.info);
        aBuilder.setMessage(s);

        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }

        });

        aBuilder.show();
    }

    public static void showProgress(final Context c, ProgressDialog progressDialog) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(c);
        }
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(c, "Please wait...", "Buffering...", true, true);
        }

    }

    public static void cancelProgress(final Context c, final ProgressDialog progressDialog) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }
    }

    public static void dialog(final Context c, final ProgressDialog progressDialog) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
