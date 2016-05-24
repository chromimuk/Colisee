package fr.marconnet.colisee.activities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import fr.marconnet.acp.models.Film;

import fr.marconnet.colisee.R;
import fr.marconnet.colisee.tools.FilmSerializer;
import fr.marconnet.colisee.tools.FormattingTools;

public class FilmActivity extends AppCompatActivity {

    public final static String INTENT_TRANSITION_ELEMENT = "affiche";
    public final static String EXTRA_FILM = "extra_film";

    private String afficheURL;
    private ImageView affiche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        String sFilm = (String) getIntent().getExtras().get(EXTRA_FILM);
        Film film = FilmSerializer.deserialize(sFilm);

        if (estPortrait()) {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            int toolbarColor = ContextCompat.getColor(this, R.color.app_blue);
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");
            collapsingToolbar.setContentScrimColor(toolbarColor);
            collapsingToolbar.setStatusBarScrimColor(toolbarColor);

            if (Build.VERSION.SDK_INT > 19) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

        setViewsContent(film);
    }


    @Override
    public void onBackPressed() {
        Glide.with(this).load(afficheURL).into(affiche);
        super.onBackPressed();
    }

    private void setViewsContent(Film film) {
        TextView titre = (TextView) findViewById(R.id.titre);
        TextView soustitre = (TextView) findViewById(R.id.soustitre);
        TextView realisateurs = (TextView) findViewById(R.id.realisateurs);
        TextView acteurs = (TextView) findViewById(R.id.acteurs);
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        TextView seances = (TextView) findViewById(R.id.seances);

        RatingBar notePresse = (RatingBar) findViewById(R.id.notePresse);
        RatingBar noteSpectateurs = (RatingBar) findViewById(R.id.noteSpectateurs);

        titre.setText(film.getTitre());
        soustitre.setText(film.getDuree());
        realisateurs.setText(FormattingTools.formatRealisateurs(film.getRealisateurs()));
        acteurs.setText(FormattingTools.formatActeurs(film.getActeurs()));
        synopsis.setText(film.getSynopsis());
        seances.setText(FormattingTools.formatSeancesDetail(film.getSeances()));


        if (film.getNotePresse() > -1 && film.getNoteSpectateurs() > -1) {
            notePresse.setRating(film.getNotePresse());
            noteSpectateurs.setRating(film.getNoteSpectateurs());
        }
        else {
            findViewById(R.id.notes).setVisibility(View.GONE);
            findViewById(R.id.notesLayout).setVisibility(View.GONE);
        }

        setAffiche(film.getImageURL());
    }


    private void setAffiche(String url) {
        affiche = (ImageView) findViewById(R.id.affiche);
        afficheURL = url;
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(this).load(url);
        if (estPortrait()) {
            drawableTypeRequest.placeholder(R.color.app_blue).diskCacheStrategy(DiskCacheStrategy.SOURCE).override(720, 720).centerCrop().into(affiche);
        } else {
            drawableTypeRequest.placeholder(R.color.app_blue).into(affiche);
        }
    }

    private boolean estPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}

