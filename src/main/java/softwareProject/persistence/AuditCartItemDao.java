package softwareProject.persistence;

import softwareProject.business.AuditCartItem;
import softwareProject.business.AuditCartItem2;

import java.util.ArrayList;

public interface AuditCartItemDao {


    public ArrayList<AuditCartItem2> getMovieIdsInDescOrderOfCount();

}
