package fr.marconnet.colisee.tools;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.marconnet.acp.models.Movie;
import fr.marconnet.colisee.R;

public class MovieCardViewAdapter extends RecyclerView.Adapter<MovieCardViewAdapter.MovieObjectHolder> {

    private final ArrayList<Movie> movies;
    private static MovieClickListener movieClickListener;

    public interface MovieClickListener {
        void onItemClick(int position, View v);
    }

    public MovieCardViewAdapter(ArrayList<Movie> lstMovies) {
        movies = lstMovies;
    }

    public void setOnItemClickListener(MovieClickListener clickListener) {
        movieClickListener = clickListener;
    }

    public static class MovieObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView cover;
        final TextView title;
        final TextView directors;
        final TextView subtitle;
        final TextView showings;

        public MovieObjectHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            directors = (TextView) itemView.findViewById(R.id.directors);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            showings = (TextView) itemView.findViewById(R.id.showings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            movieClickListener.onItemClick(getAdapterPosition(),
            itemView.findViewById(R.id.cover));
        }
    }

    @Override
    public MovieObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_film, parent, false);
        return new MovieObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieObjectHolder holder, int position) {
        holder.title.setText(movies.get(position).getTitle());
        holder.directors.setText(FormattingTools.formatDirectors(movies.get(position).getRealisateurs()));
        holder.subtitle.setText(movies.get(position).getDuration());
        holder.showings.setText(FormattingTools.formatShowingCards(movies.get(position).getShowings()));
        Glide.with(holder.cover.getContext()).load(movies.get(position).getCoverURL()).placeholder(R.color.app_blue_darker).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addItem(Movie movie, int index) {
        movies.add(index, movie);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        movies.remove(index);
        notifyItemRemoved(index);
    }
}
