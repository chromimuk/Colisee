package fr.marconnet.colisee.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import fr.marconnet.acp.Config;
import fr.marconnet.acp.models.Film;
import fr.marconnet.acp.parseur.Parseur;
import fr.marconnet.colisee.tools.FilmCardViewAdapter;
import fr.marconnet.colisee.tools.FilmSerializer;
import fr.marconnet.colisee.R;


public class MainActivity extends AppCompatActivity {

    private List<Film> films;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new FilmCardViewAdapter(new ArrayList<Film>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        new DownloadFilmsTask().execute();
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
                        }
                        else {
                            startActivity(intent);
                        }
                    }
                });
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
            }
        }

    }

}
