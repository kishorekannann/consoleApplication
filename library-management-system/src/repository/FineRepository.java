package repository;

import data.DataStore;
import model.FineRecord;

import java.util.List;
import java.util.stream.Collectors;

public class FineRepository {
    public void save(FineRecord fine) {
        DataStore.fines.add(fine);
    }

    public List<FineRecord> findByBorrower(int borrowerId) {
        return DataStore.fines.stream()
                .filter(f -> f.getBorrowerId() == borrowerId)
                .collect(Collectors.toList());
    }
}
