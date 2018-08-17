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



/*
class to polish  review adapter
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private String[][] mMoviesReview;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String author=mMoviesReview[position][0];
        String content=mMoviesReview[position][1];
        holder.author.setText(author);
        holder.review.setText(content);
    }



    @Override
    public int getItemCount() {
        if (null == mMoviesReview) return 0;
        return mMoviesReview.length;
    }


    public  void setReviews(String[][] array){
        mMoviesReview=array;
        notifyDataSetChanged();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView author;
        public TextView review;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            author=(TextView) itemView.findViewById(R.id.author);
            review=(TextView) itemView.findViewById(R.id.review);
        }
    }
}
