package com.example.osama.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by osama on 4/1/2018.
 */


/*
class to polish recycler list with data from database in case of favorite
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {


    private final FavoriteAdapter.FavoriteOnClickHandler mClickHandler;
    Context mContext;
    private Cursor mCursor;



    public FavoriteAdapter(Context context, FavoriteAdapter.FavoriteOnClickHandler handler){
        mContext=context;
        mClickHandler=handler;
    }



    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
       FavoriteViewHolder viewHolder = new FavoriteAdapter.FavoriteViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGEPATH);
        String imagePath = mCursor.getString(index);
        StringBuilder imageUrl = new StringBuilder();
        imageUrl.append("http://image.tmdb.org/t/p/");
        imageUrl.append("w500");
        imageUrl.append("/");
        imageUrl.append(imagePath);
        String url = imageUrl.toString();


        Picasso.with(mContext)
                .load(url)
                .into(holder.imageView);

    }



    @Override
    public int getItemCount() {

            if (mCursor == null) {
                return 0;
            }
            return mCursor.getCount();

    }




    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }




    public interface FavoriteOnClickHandler{
        void onListItemClck(String title,String date,String image,String overview,String voteAverage);
    }



    /*holder class for the recyclerView*/
    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView) itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int title_Index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            String title = mCursor.getString(title_Index);
            int date_Index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE);
            String date = mCursor.getString(date_Index);
            int image_Index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP);
            String image = mCursor.getString(image_Index);
            int overview_Index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            String overview = mCursor.getString(overview_Index);
            int voteAverage_Index = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE);
            String voteAverage = mCursor.getString(voteAverage_Index);
            mClickHandler.onListItemClck(title,date,image,overview,voteAverage);

        }
    }

}
