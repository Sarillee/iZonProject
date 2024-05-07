package com.test.acremote;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationBaseActivity extends AppCompatActivity
{

    private AlertDialog alertDialogProgressDialog;
    private View dialogViewProgressDialog;
    private AnimationDrawable animationDrawable;

    protected void startProgressDialog(final Context mContext, final String title, final String msg)
    {
        if(alertDialogProgressDialog !=null){if (alertDialogProgressDialog.isShowing())
        { alertDialogProgressDialog.dismiss(); alertDialogProgressDialog =null;}}
        runOnUiThread(new Runnable()
        {
            @SuppressLint("NewApi")
            public void run()
            {
                alertDialogProgressDialog = getDialogProgressBar(mContext,title,msg).create();
                WindowManager.LayoutParams lp = alertDialogProgressDialog.getWindow().getAttributes();
                lp.dimAmount=1.0f;
                alertDialogProgressDialog.getWindow().setAttributes(lp);
                alertDialogProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                alertDialogProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                //animationDrawable.start();
                alertDialogProgressDialog.show();
            }
        });
        Util.DelayMili(500);
    }

    @SuppressLint("InflateParams")
    public AlertDialog.Builder getDialogProgressBar(Context mContext, String title, String msg)
    {
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent KEvent)
            {
                return keyCode == KeyEvent.KEYCODE_HOME;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setOnKeyListener(keyListener);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogViewProgressDialog = inflater.inflate(R.layout.activity_alert_dialog, null);
        //animationDrawable = (AnimationDrawable)((ImageView) dialogViewProgressDialog.findViewById(R.id.img)).getDrawable();
        ((TextView) dialogViewProgressDialog.findViewById(R.id.title)).setText(title);
        ((TextView) dialogViewProgressDialog.findViewById(R.id.msg)).setText(msg);
        //((ProgressBar) dialogViewProgressDialog.findViewById(R.id.progress)).setMax(100);
        builder.setView(dialogViewProgressDialog);
        builder.setCancelable(false);
        return builder;
    }

    protected void closeProgressDialog()
    {
        if(alertDialogProgressDialog !=null)
        {
            if(alertDialogProgressDialog.isShowing())
            {
                alertDialogProgressDialog.dismiss();
            }
        }
        alertDialogProgressDialog =null;
        if(animationDrawable!=null){animationDrawable.stop();}
    }
}
