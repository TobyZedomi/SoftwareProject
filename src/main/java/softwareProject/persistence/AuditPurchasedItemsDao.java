package softwareProject.persistence;

import softwareProject.business.AuditPurchasedItems;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AuditPurchasedItemsDao {
    public Map<LocalDate, List<AuditPurchasedItems>> searchForPurchasesGroupedByMonth(LocalDateTime startDate, LocalDateTime endDate);
    public ArrayList<AuditPurchasedItems> purchasedItemsUser(String username);
}
