package com.morax.metalytics.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.morax.metalytics.fragment.GuidesFragment;
import com.morax.metalytics.fragment.NewsFragment;
import com.morax.metalytics.fragment.VideosFragment;

public class TabLayoutAdapter extends FragmentStateAdapter {

    public TabLayoutAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new GuidesFragment();
            case 2:
                return new VideosFragment();
        }
        return new NewsFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}