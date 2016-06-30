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


import fr.marconnet.acp.models.Movie;

import fr.marconnet.colisee.R;
import fr.marconnet.colisee.tools.MovieSerializer;
import fr.marconnet.colisee.tools.FormattingTools;

public class FilmActivity extends AppCompatActivity {

    public final static String INTENT_TRANSITION_ELEMENT = "cover";
    public final static String EXTRA_MOVIE = "extra_movie";

    private String coverURL;
    private ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        String sMovie = (String) getIntent().getExtras().get(EXTRA_MOVIE);
        Movie movie = MovieSerializer.deserialize(sMovie);

        if (isPortrait()) {
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

        setViewsContent(movie);
    }


    @Override
    public void onBackPressed() {
        Glide.with(this).load(coverURL).into(cover);
        super.onBackPressed();
    }

    private void setViewsContent(Movie movie) {
        TextView title = (TextView) findViewById(R.id.title);
        TextView subtitle = (TextView) findViewById(R.id.subtitle);
        TextView directors = (TextView) findViewById(R.id.directors);
        TextView actors = (TextView) findViewById(R.id.actors);
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        TextView showings = (TextView) findViewById(R.id.showings);

        RatingBar ratingsJournalists = (RatingBar) findViewById(R.id.ratingsJournalists);
        RatingBar ratingViewers = (RatingBar) findViewById(R.id.ratingViewers);

        title.setText(movie.getTitle());
        subtitle.setText(movie.getDuration());
        directors.setText(FormattingTools.formatDirectors(movie.getRealisateurs()));
        actors.setText(FormattingTools.formatActeurs(movie.getActeurs()));
        synopsis.setText(movie.getSynopsis());
        showings.setText(FormattingTools.formatShowingDetails(movie.getShowings()));


        if (movie.getRatingJournalists() > -1 && movie.getRatingViewers() > -1) {
            ratingsJournalists.setRating(movie.getRatingJournalists());
            ratingViewers.setRating(movie.getRatingViewers());
        }
        else {
            findViewById(R.id.ratings).setVisibility(View.GONE);
            findViewById(R.id.ratingsLayout).setVisibility(View.GONE);
        }

        setCover(movie.getCoverURL());
    }


    private void setCover(String url) {
        cover = (ImageView) findViewById(R.id.cover);
        coverURL = url;
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(this).load(url);

        if (isPortrait()) {
            drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE).override(720, 720).centerCrop().into(cover);
        } else {
            drawableTypeRequest.placeholder(R.color.app_blue).into(cover);
        }
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}

