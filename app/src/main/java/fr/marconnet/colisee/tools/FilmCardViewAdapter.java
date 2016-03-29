package fr.marconnet.colisee.tools;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.marconnet.acp.models.Film;
import fr.marconnet.colisee.R;

public class FilmCardViewAdapter extends RecyclerView.Adapter<FilmCardViewAdapter.FilmObjectHolder> {

    private final ArrayList<Film> films;
    private static FilmClickListener filmClickListener;

    public interface FilmClickListener {
        void onItemClick(int position, View v);
    }

    public FilmCardViewAdapter(ArrayList<Film> lstFilms) {
        films = lstFilms;
    }

    public void setOnItemClickListener(FilmClickListener clickListener) {
        filmClickListener = clickListener;
    }

    public static class FilmObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView affiche;
        final TextView titre;
        final TextView realisateurs;
        final TextView soustitre;
        final TextView seances;

        public FilmObjectHolder(View itemView) {
            super(itemView);
            affiche = (ImageView) itemView.findViewById(R.id.image);
            titre = (TextView) itemView.findViewById(R.id.titre);
            realisateurs = (TextView) itemView.findViewById(R.id.realisateurs);
            soustitre = (TextView) itemView.findViewById(R.id.soustitre);
            seances = (TextView) itemView.findViewById(R.id.seances);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmClickListener.onItemClick(getAdapterPosition(),
            itemView.findViewById(R.id.image));
        }
    }

    @Override
    public FilmObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_film, parent, false);
        return new FilmObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmObjectHolder holder, int position) {
        holder.titre.setText(films.get(position).getTitre());
        holder.realisateurs.setText(FormattingTools.formatRealisateurs(films.get(position).getRealisateurs()));
        holder.soustitre.setText(films.get(position).getDuree());
        holder.seances.setText(FormattingTools.formatSeancesCards(films.get(position).getSeances()));
        Glide.with(holder.affiche.getContext()).load(films.get(position).getImageURL()).placeholder(R.color.app_blue_darker).into(holder.affiche);
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public void addItem(Film film, int index) {
        films.add(index, film);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        films.remove(index);
        notifyItemRemoved(index);
    }
}
