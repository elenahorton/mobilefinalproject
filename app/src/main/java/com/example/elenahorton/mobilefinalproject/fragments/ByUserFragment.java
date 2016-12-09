package com.example.elenahorton.mobilefinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elenahorton.mobilefinalproject.PostsActivity;
import com.example.elenahorton.mobilefinalproject.R;

/**
 * Created by elenahorton on 12/9/16.
 */
public class ByUserFragment extends Fragment {
    PostsActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_byuser, null);
        parentActivity = (PostsActivity) getActivity();
        RecyclerView recyclerViewPosts = (RecyclerView) rootView.findViewById(R.id.recyclerUserPostList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewPosts.setLayoutManager(layoutManager);
        recyclerViewPosts.setAdapter(parentActivity.getPostsUserAdapter());
        return rootView;
    }


}