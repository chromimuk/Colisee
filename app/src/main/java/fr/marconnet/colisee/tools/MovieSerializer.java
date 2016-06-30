package fr.marconnet.colisee.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.marconnet.acp.models.Movie;

public class MovieSerializer {

	public static String serialize(Movie movie) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(movie);
	}
	
	public static Movie deserialize(String sFilm) {
		Gson gson = new Gson();
		return gson.fromJson(sFilm, Movie.class);
	}
	
}
