package com.mame.magnifying;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kosukeEndo on 2018/01/03.
 */

public class DeleteActionFragment  extends Fragment {

    /**
     * @return DeleteActionFragment
     */
    public static DeleteActionFragment newInstance() {
        final DeleteActionFragment fragment = new DeleteActionFragment();
        return fragment;
    }

    public DeleteActionFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_delete_action, container, false);
        // 削除ボタン
        final View clearFloatingButton = rootView.findViewById(R.id.clearDemo);
        clearFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Easy way to delete a service
                final Activity activity = getActivity();
//                activity.stopService(new Intent(activity, CustomFloatingViewService.class));
            }
        });
        return rootView;
    }
}
