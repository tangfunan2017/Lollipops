package tfn.android.lollipops.view.widget.blogtextview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.LruCache;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import tfn.android.lollipops.util.KeyGenerator;


/**
 * 微博富文本显示textview
 * 表情、话题、@、链接 高亮展示并设置点击事件
 * Created by tangfunan on 2017/12/20.
 */

public class BlogTextView extends TextView{

    private static LruCache<String, SpannableString> stringMemoryCache;
    private String content;


    public BlogTextView(Context context) {
        super(context);
    }

    public BlogTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlogTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置文本内容
    public void setContent(String text) {
        content = text;
        if (stringMemoryCache == null) {
            stringMemoryCache = new LruCache<>(200);
        }
        String key = KeyGenerator.generateMD5(content);
        SpannableString spannableString = stringMemoryCache.get(key);
        if (spannableString != null) {
            super.setText(spannableString);
            Logger.v("从内存中加载spannable数据");
        } else {
            Logger.v("开启线程，开始加载spannable数据");
            super.setText(content);
            Observable.create(new ObservableOnSubscribe<SpannableString>() {
                @Override
                public void subscribe(ObservableEmitter<SpannableString> e) throws Exception {
                    SpannableString sp = SpannableString.valueOf(content);

                    //表情
//                    Matcher localMacher = Pattern.compile("\\[(\\S+?)\\]").matcher(spannableString);
//                    while (localMacher.find()) {
//
//                    }
                    //@用户名称
                    Pattern patternUser = Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
                    String scheme = "tfn.android.lollipops.userInfo://";
                    Linkify.addLinks(sp, patternUser, scheme);
                    //#话题#
                    Pattern patternTopic = Pattern.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
                    scheme = "tfn.android.lollipops.topics://";
                    Linkify.addLinks(sp, patternTopic, scheme);
                    //链接
                    Pattern patternUrl = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
                    scheme = "tfn.android.lollipops.browser://";
                    Linkify.addLinks(sp, patternUrl, scheme);

                    URLSpan[] urlSpans = sp.getSpans(0, sp.length(), URLSpan.class);
                    MyUrlSpan span;
                    for (URLSpan urlSpan : urlSpans) {
                        span = new MyUrlSpan(urlSpan.getURL(), getContext());
                        int start = sp.getSpanStart(urlSpan);
                        int end = sp.getSpanEnd(urlSpan);
                        try{
                            sp.removeSpan(urlSpan);
                        } catch (Exception ee) {

                        }
                        sp.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    e.onNext(sp);
                    String key = KeyGenerator.generateMD5(sp.toString());
                    stringMemoryCache.put(key, sp);
                    Logger.v(String.format("添加spannable到内存中，现在共有%d个spannable", stringMemoryCache.size()));
                }
            })
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SpannableString>() {
                @Override
                public void accept(SpannableString spannableString) throws Exception {
                    BlogTextView.super.setText(spannableString);
                }
            });
        }
        setClickable(false);
        setOnTouchListener(onTouchListener);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {

        ClickableTextViewMentionLinkOnTouchListener listener = new ClickableTextViewMentionLinkOnTouchListener();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return listener.onTouch(view, motionEvent);
        }
    };


}
