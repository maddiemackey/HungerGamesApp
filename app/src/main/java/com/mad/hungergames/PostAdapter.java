package com.mad.hungergames;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);

        if(post.getPicture() != null){
            Picasso.with(context)
                    .load(post.getPicture())
                    .into(holder.pictureIv);
        }
        holder.textTv.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView pictureIv;
        private TextView textTv;

        public ViewHolder(View itemView) {
            super(itemView);
            textTv = itemView.findViewById(R.id.post_text);
            pictureIv = itemView.findViewById(R.id.post_image);
        }
    }
}
