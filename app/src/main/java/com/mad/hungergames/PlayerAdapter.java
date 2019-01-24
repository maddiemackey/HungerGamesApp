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
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.util.List;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private List<Player> players;
    private Context context;

    public PlayerAdapter(List<Player> players, Context context) {
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);

        Picasso.with(context)
                .load(player.getAvatar())
                .resize(300, 300)
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 25, RoundedCornersTransformation.CornerType.ALL))
                .into(holder.avatarIv);
        holder.nameTv.setText(player.getName());
        holder.roleTv.setText(player.getRole());
        holder.killcodeTv.setText(player.getKillcode());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTv;
        private TextView roleTv;
        private TextView killcodeTv;
        private ImageView avatarIv;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.avatar);
            nameTv = itemView.findViewById(R.id.player_name);
            roleTv = itemView.findViewById(R.id.role);
            killcodeTv = itemView.findViewById(R.id.killcode);
        }
    }
}
