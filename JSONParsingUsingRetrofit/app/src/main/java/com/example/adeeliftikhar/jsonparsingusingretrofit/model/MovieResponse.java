package com.example.adeeliftikhar.jsonparsingusingretrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//      This will get Data from server...

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieModel> resultsList;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

//    This will get all data into List from Response (server)...
    public List<MovieModel> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<MovieModel> results) {
        this.resultsList = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}