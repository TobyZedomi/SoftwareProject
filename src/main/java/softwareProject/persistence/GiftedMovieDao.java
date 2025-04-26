package softwareProject.persistence;

import softwareProject.business.GiftedMovie;

import java.util.List;

public interface GiftedMovieDao {
    void giftMovie(String senderUsername, String recipientUsername, int movieId);
    List<GiftedMovie> getGiftsReceived(String recipientUsername);
}