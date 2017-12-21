package tfn.lollipops.library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity基类
 *
 * Created by tangfunan on 2017/12/19.
 */

public class BaseActivity extends AppCompatActivity {

        /*
         * 解决Vector兼容性问题
         *
         * First up, this functionality was originally released in 23.2.0,
         * but then we found some memory usage and Configuration updating
         * issues so we it removed in 23.3.0. In 23.4.0 (technically a fix
         * release) we’ve re-added the same functionality but behind a flag
         * which you need to manually enable.
         *
         * http://www.jianshu.com/p/e3614e7abc03
         */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
