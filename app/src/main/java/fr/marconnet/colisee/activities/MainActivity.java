package fr.marconnet.colisee.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import fr.marconnet.acp.Config;
import fr.marconnet.acp.models.Movie;
import fr.marconnet.acp.parseur.Parseur;
import fr.marconnet.colisee.tools.MovieCardViewAdapter;
import fr.marconnet.colisee.tools.MovieSerializer;
import fr.marconnet.colisee.R;


public class MainActivity extends AppCompatActivity {

    private List<Movie> movies;
    private RecyclerView.Adapter adapter;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (ProgressBar) findViewById(R.id.loading);
        adapter = new MovieCardViewAdapter(new ArrayList<Movie>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (isNetworkAvailable()) {
            loading.setVisibility(View.VISIBLE);
            new DownloadFilmsTask().execute();
        }
        else {
            setSnackbarNoInternet();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MovieCardViewAdapter) adapter).setOnItemClickListener(
                new MovieCardViewAdapter.MovieClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        Movie movie = movies.get(position);
                        String sFilm = MovieSerializer.serialize(movie);
                        Intent intent = new Intent(MainActivity.this, FilmActivity.class);
                        intent.putExtra(FilmActivity.EXTRA_MOVIE, sFilm);

                        if (Build.VERSION.SDK_INT > 21) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                                    MainActivity.this, v, FilmActivity.INTENT_TRANSITION_ELEMENT);
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }
                });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void setSnackbarNoInternet() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.noInternet, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(getIntent());
                    }
                });
        snackbar.show();
    }


    private class DownloadFilmsTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... empty) {
            try {
                Parseur p = new Parseur();
                p.getDocumentFromUrl(Config.URL);
                return p.getMovies();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result == null) {
                movies = new ArrayList<>();
            }
            else {
                for (int i = 0; i < result.size(); i++) {
                    ((MovieCardViewAdapter) adapter).addItem(result.get(i), i);
                }
                movies = result;
                loading.setVisibility(View.GONE);
            }
        }

    }

}
