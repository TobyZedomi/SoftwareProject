package softwareProject.persistence;

import softwareProject.business.FavoriteList;

import java.util.ArrayList;

public interface FavoriteListDao {

    public int addFavouriteList(FavoriteList favoriteList);

    public ArrayList<FavoriteList> getAllFavouriteListByUsername(String username);
}
