package com.mame.magnifying;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by kosukeEndo on 2018/01/03.
 */

public class DeleteActionActivity extends Activity {

    /**
     * 設定フラグメントのタグ
     */
    private static final String FRAGMENT_TAG_DELETE_ACTION = "delete_action";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_action);

        if (savedInstanceState == null) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.container, DeleteActionFragment.newInstance(), FRAGMENT_TAG_DELETE_ACTION);
            ft.commit();
        }

    }
}
