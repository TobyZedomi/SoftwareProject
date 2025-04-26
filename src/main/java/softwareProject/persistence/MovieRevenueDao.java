package softwareProject.persistence;

import softwareProject.business.MovieRevenue;

import java.util.ArrayList;

public interface MovieRevenueDao {
    public ArrayList<MovieRevenue> getTotalMovieRevenue();
}
