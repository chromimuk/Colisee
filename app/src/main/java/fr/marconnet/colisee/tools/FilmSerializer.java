package fr.marconnet.colisee.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.marconnet.acp.models.Film;

public class FilmSerializer {

	public static String serialize(Film film) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(film);
	}
	
	public static Film deserialize(String sFilm) {
		Gson gson = new Gson();
		return gson.fromJson(sFilm, Film.class);
	}
	
}
