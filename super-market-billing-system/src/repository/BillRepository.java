package repository;

import data.DataStore;
import model.Bill;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillRepository {
    public void save(Bill bill) {
        DataStore.bills.add(bill);
    }

    public List<Bill> findAll() {
        return new ArrayList<>(DataStore.bills);
    }

    public List<Bill> findByCustomerId(int customerId) {
        return DataStore.bills.stream()
                .filter(b -> b.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public List<Bill> findByAdminId(int adminId) {
        return DataStore.bills.stream()
                .filter(b -> b.getBilledByAdminId() == adminId)
                .collect(Collectors.toList());
    }
}
