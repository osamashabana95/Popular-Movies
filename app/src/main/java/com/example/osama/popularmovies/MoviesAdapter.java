package com.example.osama.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private final MoviesOnClickHandler mClickHandler;
    Context mContext;
    private String[] mMoviesPosters;

    public MoviesAdapter(Context context,MoviesOnClickHandler handler){
      mContext=context;
      mClickHandler=handler;

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
       MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

           String url = mMoviesPosters[position];

            Picasso.with(mContext)
                    .load(url)
                    .into(holder.imageView);


    }

    @Override
    public int getItemCount() {


            if (null == mMoviesPosters) {
                return 0;
            }
            return mMoviesPosters.length;

        }


    /*to set the array of posters path which will be adapted to list*/
    public  void setMoviesPosters (String[] array){
        mMoviesPosters=array;
        notifyDataSetChanged();

    }



    public interface MoviesOnClickHandler{
        void onListItemClck(int position);
    }

    /*holder class for the recyclerView*/
    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public   ImageView imageView;
        public MoviesViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView) itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.onListItemClck(position);

        }
    }

}
