package tfn.android.lollipops.view.widget.blogtextview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import tfn.android.lollipops.R;

/**
 * Created by tangfunan on 2017/12/20.
 */

public class MyUrlSpan extends ClickableSpan {

    private String url;
    private int color;
    private Context context;

    public MyUrlSpan(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    public MyUrlSpan(Parcel src) {
        this.url = src.readString();
    }

    public int getSpanTypeId() {
        return 11;
    }

    public String getUrl() {
        return this.url;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void onClick(View view) {
        Logger.v(MyUrlSpan.class.getSimpleName(), String.format("the link(%s) was clicked ", new Object[]{this.getUrl()}));
        Uri uri = Uri.parse(getUrl());
        Context context = view.getContext();
        Intent intent;
        if (uri.getScheme().startsWith("http")) {
            Logger.v("点击了");
            Toast.makeText(context, "点击了外链", Toast.LENGTH_SHORT).show();
//            intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            intent.setData(uri);
//            context.startActivity(intent);
        } else {
            Logger.v("点击了");
            Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
//            intent = new Intent("android.intent.action.VIEW", uri);
//            intent.putExtra("com.android.browser.application_id", context.getPackageName());
//            context.startActivity(intent);
        }
    }

    public void onLongClick(View view) {
        Uri data = Uri.parse(getUrl());
        if (data != null) {
            String d = data.toString();
            String value = "";
            if (d.startsWith("tfn.android.lollipops")) {
                int index = d.lastIndexOf("/");
                value = d.substring(index + 1);
            } else if (d.startsWith("http")) {
                value = d;
            }

            if (!TextUtils.isEmpty(value)) {
                ClipboardManager cm = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("ui", value));
                Toast.makeText(view.getContext(), context.getString(R.string.comm_hint_copied), Toast.LENGTH_LONG).show();
            }

        }



    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (this.color == 0) {
            int[] attrs = new int[]{R.attr.colorPrimary};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            ds.setColor(ta.getColor(0, -16776961));
        } else {
            ds.setColor(this.color);
        }
    }
}
