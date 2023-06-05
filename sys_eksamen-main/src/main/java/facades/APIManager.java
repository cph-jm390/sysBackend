package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AnimalDTO;
import dtos.FactDTO;
import entities.Animal;
import entities.Fact;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class APIManager {

    public static FactDTO createFactDTOFromAPI() throws Exception {

        Gson GSON = new GsonBuilder().create();
        String urlString = "https://random-facts1.p.rapidapi.com/fact/search";
        URL yahoo = new URL(urlString);
        URLConnection urlConnection = yahoo.openConnection();

        urlConnection.setRequestProperty("X-RapidAPI-Key", "d5dd72de82mshcf6fc0cf7c28a79p1bdbf0jsn5189c1b81892");
        urlConnection.setRequestProperty("X-RapidAPI-Host", "random-facts1.p.rapidapi.com");
        urlConnection.setRequestProperty("subcategory", "placeholder til metode der fetcher fra frontend"); //OBS

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
        System.out.println(sb);
        Fact fact = GSON.fromJson(sb.toString(), Fact.class);
        return new FactDTO(fact);

        //return new FactDTO(new Fact(1L, "test"));
    }

    public static AnimalDTO createAnimalDTOFromAPI() throws Exception {

        Gson GSON = new GsonBuilder().create();
        String urlString = "https://rapidapi.com/apininjas/api/animals-by-api-ninjas";
        URL yahoo = new URL(urlString);
        URLConnection urlConnection = yahoo.openConnection();

        urlConnection.setRequestProperty("X-RapidAPI-Key", "d5dd72de82mshcf6fc0cf7c28a79p1bdbf0jsn5189c1b81892");
        urlConnection.setRequestProperty("X-RapidAPI-Host", "animals-by-api-ninjas.p.rapidapi.com");
        urlConnection.setRequestProperty("subcategory", "placeholder til metode der fetcher fra frontend"); //OBS

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
        System.out.println(sb);
        Animal animal = GSON.fromJson(sb.toString(), Animal.class);
        return new AnimalDTO(animal);

        //return new AnimalDTO(new Animal(1L, "test"));

    }
}
