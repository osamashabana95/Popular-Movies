package com.example.osama.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.osama.popularmovies.R;

/**
 * Created by osama on 3/29/2018.
 */

/* to adapt and polish trailer recycler view

 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final TrailerOnClickHandler mClickHandler;
    private String[][] mMoviesTrailer;
    public TrailerAdapter(TrailerOnClickHandler handler){

        mClickHandler=handler;
    }
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
       TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

      String name =mMoviesTrailer[position][1];
      holder.name.setText(name);

    }

    @Override
    public int getItemCount() {
        if (null == mMoviesTrailer) return 0;
        return mMoviesTrailer.length;
    }
    public  void setTrailers (String[][] array){
        mMoviesTrailer=array;
        notifyDataSetChanged();
    }

    public interface TrailerOnClickHandler{
        void onListItemClck(String s);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.trailer_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String key =mMoviesTrailer[position][0];
            mClickHandler.onListItemClck(key);

        }
    }
}

