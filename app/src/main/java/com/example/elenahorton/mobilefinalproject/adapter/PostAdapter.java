package com.example.elenahorton.mobilefinalproject.adapter;

/**
 * Created by elenahorton on 12/9/16.
 */
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.elenahorton.mobilefinalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.elenahorton.mobilefinalproject.model.Post;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
        implements PostTouchHelperAdapter  {

    @Override
    public void onItemDismiss(int position) {
        // delete from Firebase
        postKeys.remove(position);
        postList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        postList.add(toPosition, postList.get(fromPosition));
        postList.remove(fromPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvCategory;
        public TextView tvDescription;
        public ImageView imageImage;
        public Button btnDeletePost;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            imageImage = (ImageView) itemView.findViewById(R.id.imageImage);
            btnDeletePost = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    private Context context;
    private List<Post> postList;
    private List<String> postKeys;
    private String uId;
    private int lastPosition = -1;
    private DatabaseReference postsRef;

    public PostAdapter(Context context, String uId) {
        this.context = context;
        this.uId = uId;
        this.postList = new ArrayList<Post>();
        this.postKeys = new ArrayList<String>();


        postsRef = FirebaseDatabase.getInstance().getReference("posts");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_post, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Post tmpPost = postList.get(position);
        viewHolder.tvAuthor.setText(tmpPost.getAuthor());
        viewHolder.tvCategory.setText(tmpPost.getCategory());
        viewHolder.tvDescription.setText(tmpPost.getDescription());
        Glide.with(context).load(tmpPost.getImage()).into(viewHolder.imageImage);

        if(uId.equals(tmpPost.getUid())) {
            //then make it visible
            viewHolder.btnDeletePost.setVisibility(View.VISIBLE);
            viewHolder.btnDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePost(viewHolder.getAdapterPosition());
                }
            });
        }


        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addPost(Post place, String key) {
        postList.add(place);
        postKeys.add(key);
        notifyDataSetChanged();
    }

    public void removePost(int index) {
        postsRef.child(postKeys.get(index)).removeValue();
        postList.remove(index);
        postKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePostByKey(String key) {
        int index = postKeys.indexOf(key);
        if (index != -1) {
            postList.remove(index);
            postKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}