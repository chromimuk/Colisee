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
import android.widget.TextView;

import fr.marconnet.acp.Config;
import fr.marconnet.acp.models.Film;
import fr.marconnet.acp.parseur.Parseur;
import fr.marconnet.colisee.tools.FilmCardViewAdapter;
import fr.marconnet.colisee.tools.FilmSerializer;
import fr.marconnet.colisee.R;


public class MainActivity extends AppCompatActivity {

    private List<Film> films;
    private RecyclerView.Adapter adapter;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (ProgressBar) findViewById(R.id.loading);
        adapter = new FilmCardViewAdapter(new ArrayList<Film>());
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

        ((FilmCardViewAdapter) adapter).setOnItemClickListener(
                new FilmCardViewAdapter.FilmClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        Film film = films.get(position);
                        String sFilm = FilmSerializer.serialize(film);
                        Intent intent = new Intent(MainActivity.this, FilmActivity.class);
                        intent.putExtra(FilmActivity.EXTRA_FILM, sFilm);

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
                .make(coordinatorLayout, "Pas de connexion Internet", Snackbar.LENGTH_INDEFINITE)
                .setAction("REESSAYER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(getIntent());
                    }
                });
        snackbar.show();
    }


    private class DownloadFilmsTask extends AsyncTask<Void, Void, List<Film>> {

        @Override
        protected List<Film> doInBackground(Void... empty) {
            try {
                Parseur p = new Parseur();
                p.getDocumentFromUrl(Config.URL);
                return p.getFilms();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Film> result) {
            if (result == null) {
                films = new ArrayList<>();
            }
            else {
                for (int i = 0; i < result.size(); i++) {
                    ((FilmCardViewAdapter) adapter).addItem(result.get(i), i);
                }
                films = result;
                loading.setVisibility(View.GONE);
            }
        }

    }

}
