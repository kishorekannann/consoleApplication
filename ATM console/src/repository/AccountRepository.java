package repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import model.Account;
import model.Transaction;
import model.TransactionType;

public class AccountRepository {
    private static final Path DATA_FILE = Path.of("data", "accounts.ser");
    private static final Map<String, Account> accounts = new HashMap<>();

    static {
        loadOrSeedAccounts();
    }

    private static void loadOrSeedAccounts() {
        if (Files.exists(DATA_FILE)) {
            loadAccounts();
            if (!accounts.isEmpty()) {
                return;
            }
        }

        seedAccounts();
        persistAccounts();
    }

    private static void seedAccounts() {
        Account primary = new Account("12345", "Kishore Kumar", new BigDecimal("15000.00"), "1111");
        primary.addTransaction(new Transaction(
                TransactionType.DEPOSIT,
                new BigDecimal("15000.00"),
                primary.getBalance(),
                "Initial cash load",
                LocalDateTime.now().minusDays(2)));

        Account savings = new Account("67890", "Priya Raman", new BigDecimal("24500.00"), "2222");
        savings.addTransaction(new Transaction(
                TransactionType.DEPOSIT,
                new BigDecimal("24500.00"),
                savings.getBalance(),
                "Initial cash load",
                LocalDateTime.now().minusDays(1)));

        Account travel = new Account("54321", "Arun Das", new BigDecimal("9000.00"), "3333");
        travel.addTransaction(new Transaction(
                TransactionType.DEPOSIT,
                new BigDecimal("9000.00"),
                travel.getBalance(),
                "Initial cash load",
                LocalDateTime.now().minusHours(12)));

        accounts.put(primary.getAccountNumber(), primary);
        accounts.put(savings.getAccountNumber(), savings);
        accounts.put(travel.getAccountNumber(), travel);
    }

    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(DATA_FILE))) {
            Object storedData = inputStream.readObject();
            if (storedData instanceof Map<?, ?> storedAccounts) {
                accounts.clear();
                accounts.putAll((Map<String, Account>) storedAccounts);
            }
        } catch (IOException | ClassNotFoundException ex) {
            accounts.clear();
        }
    }

    private static void persistAccounts() {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(DATA_FILE))) {
                outputStream.writeObject(accounts);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to persist account data.", ex);
        }
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public void update(Account account) {
        accounts.put(account.getAccountNumber(), account);
        persistAccounts();
    }
}
