package com.mad.hungergames;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private List<Game> games;
    private Context context;

    private String DATE = "Date: ";
    private String DASH = "-";
    private String WINNER = "Winner: ";


    public GameAdapter(List<Game> games, Context context) {
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Game game = games.get(position);

        holder.nameTv.setText(game.getName());
        String startDateTime = DATE + String.valueOf(game.getStartTime().getDate()) + DASH +
                String.valueOf(game.getStartTime().getMonth() + 1) + DASH +
                String.valueOf(game.getStartTime().getYear()).substring(1);
        holder.dateTv.setText(startDateTime);


        if(game.isRegistrationActive()){
            holder.statusTv.setText(R.string.registration_open);
        }
        else {
            if(game.getEndTime() != null){
                String winnerText = WINNER + (game.getWinnerName());
                holder.statusTv.setText(winnerText);
            }
            else{
                holder.statusTv.setText(R.string.in_progress);
            }
        }

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTv;
        private TextView dateTv;
        private TextView statusTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.game_name);
            dateTv = itemView.findViewById(R.id.date);
            statusTv = itemView.findViewById(R.id.status);
        }
    }
}
