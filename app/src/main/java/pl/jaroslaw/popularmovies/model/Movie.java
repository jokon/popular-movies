package pl.jaroslaw.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by jokonski on 21.03.18.
 */
/*

{"vote_count":1020,
"id":337167,
"video":false,
"vote_average":6.2,
"title":"Fifty Shades Freed",
"popularity":592.721736,
"poster_path":"\/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg",
"original_language":"en",
"original_title":"Fifty Shades Freed",
"genre_ids":[18,10749],
"backdrop_path":"\/9ywA15OAiwjSTvg3cBs9B7kOCBF.jpg",
"adult":false,
"overview":"Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins.",
"release_date":"2018-02-07"},


{
"belongs_to_collection":{
    "id":344830,
    "name":"Fifty Shades Collection",
    "poster_path":"/oJrMaAhQlV5K9QFhulFehTn7JVn.jpg",
    "backdrop_path":"/23fRJTMFRG2lMvvpHcIZ0ZU77I1.jpg"},
"budget":55000000,
"genres":[{
    "id":18,
    "name":"Drama"},
    {
    "id":10749,
    "name":"Romance"}],
"homepage":"http://www.fiftyshadesmovie.com",
"imdb_id":"tt4477536",
"production_companies":[{
    "id":33,
    "logo_path":"/8lvHyhjr8oUKOOy2dKXoALWKdp0.png",
    "name":"Universal Pictures",
    "origin_country":"US"}],
"production_countries":[{
    "iso_3166_1":"US",
    "name":"United States of America"}],
"revenue":136906000,
"runtime":106,
"spoken_languages":[{
    "iso_639_1":"en",
    "name":"English"}],
"status":"Released",
"tagline":"Don't miss the climax"}
 */

@Data
public class Movie {
    @SerializedName("id")
    private Long id;

    @SerializedName("vote_count")
    private Long voteCount;

    @SerializedName("video")
    private boolean isVideo;

    @SerializedName("vote_average")
    private double voteAvarage;

    @SerializedName("title")
    private String title;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    //@SerializedName("genre_ids")
    //private List<Long> genreIds;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("adult")
    private Boolean isAdult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

}
