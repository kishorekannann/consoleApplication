package ui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import model.Account;
import model.Transaction;
import service.ATMService;
import service.AuthenticationResult;
import service.OperationResult;

public class ConsoleUI {
    private static final int MINI_STATEMENT_LIMIT = 5;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    private final ATMService service = new ATMService();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("========================================");
        System.out.println("        ATM Console Application");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printWelcomeMenu();
            int choice = readMenuChoice("Choose an option: ", 1, 2);
            if (choice == -1) {
                System.out.println("Session closed.");
                return;
            }

            switch (choice) {
                case 1:
                    Account account = loginFlow();
                    if (account != null) {
                        running = runSession(account);
                    }
                    break;
                case 2:
                    running = false;
                    System.out.println("Session closed.");
                    break;
                default:
                    break;
            }
        }
    }

    private void printWelcomeMenu() {
        System.out.println();
        System.out.println("1. Login");
        System.out.println("2. Exit");
    }

    private Account loginFlow() {
        System.out.println();
        String accNo = readLine("Enter Account Number: ");
        if (accNo == null) {
            return null;
        }

        String pin = readLine("Enter PIN: ");
        if (pin == null) {
            return null;
        }

        AuthenticationResult result = service.login(accNo.trim(), pin.trim());
        System.out.println(result.getMessage());
        if (!result.isSuccess()) {
            return null;
        }

        Account account = result.getAccount();
        System.out.println("Welcome, " + account.getHolderName() + ".");
        return account;
    }

    private boolean runSession(Account account) {
        boolean loggedIn = true;
        while (loggedIn) {
            printSessionMenu(account);
            int choice = readMenuChoice("Choose an option: ", 1, 7);

            switch (choice) {
                case 1:
                    showBalance(account);
                    break;
                case 2:
                    handleDeposit(account);
                    break;
                case 3:
                    handleWithdrawal(account);
                    break;
                case 4:
                    handleTransfer(account);
                    break;
                case 5:
                    showMiniStatement(account);
                    break;
                case 6:
                    handlePinChange(account);
                    break;
                case 7:
                    loggedIn = false;
                    break;
                default:
                    break;
            }
        }

        String answer = readLine("Return to main menu? (y/n): ");
        if (answer == null) {
            return false;
        }
        return !answer.equalsIgnoreCase("n");
    }

    private void printSessionMenu(Account account) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Cash");
        System.out.println("3. Withdraw Cash");
        System.out.println("4. Transfer Funds");
        System.out.println("5. Mini Statement");
        System.out.println("6. Change PIN");
        System.out.println("7. Logout");
    }

    private void showBalance(Account account) {
        System.out.println("Available balance: " + formatAmount(service.checkBalance(account)));
    }

    private void handleDeposit(Account account) {
        try {
            BigDecimal amount = readAmount("Enter deposit amount: ");
            OperationResult result = service.deposit(account, amount);
            printSuccess(result);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleWithdrawal(Account account) {
        try {
            BigDecimal amount = readAmount("Enter withdrawal amount: ");
            OperationResult result = service.withdraw(account, amount);
            printSuccess(result);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleTransfer(Account account) {
        try {
            String destinationAccount = readLine("Enter destination account number: ");
            if (destinationAccount == null) {
                return;
            }
            destinationAccount = destinationAccount.trim();
            if (!service.accountExists(destinationAccount)) {
                System.out.println("Destination account does not exist.");
                return;
            }

            BigDecimal amount = readAmount("Enter transfer amount: ");
            OperationResult result = service.transfer(account, destinationAccount, amount);
            printSuccess(result);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showMiniStatement(Account account) {
        List<Transaction> transactions = service.getMiniStatement(account);
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        System.out.println();
        System.out.println("Recent transactions:");
        int startIndex = Math.max(0, transactions.size() - MINI_STATEMENT_LIMIT);
        for (int index = transactions.size() - 1; index >= startIndex; index--) {
            Transaction transaction = transactions.get(index);
            System.out.println(
                    DATE_FORMAT.format(transaction.getTimestamp())
                            + " | "
                            + transaction.getType()
                            + " | "
                            + formatAmount(transaction.getAmount())
                            + " | Bal: "
                            + formatAmount(transaction.getBalanceAfterTransaction())
                            + " | "
                            + transaction.getDescription());
        }
    }

    private void handlePinChange(Account account) {
        try {
            String currentPin = readLine("Enter current PIN: ");
            String newPin = readLine("Enter new 4-digit PIN: ");
            String confirmPin = readLine("Confirm new PIN: ");

            if (currentPin == null || newPin == null || confirmPin == null) {
                return;
            }

            currentPin = currentPin.trim();
            newPin = newPin.trim();
            confirmPin = confirmPin.trim();

            if (!newPin.equals(confirmPin)) {
                System.out.println("New PIN and confirmation do not match.");
                return;
            }

            OperationResult result = service.changePin(account, currentPin, newPin);
            printSuccess(result);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            String value = readLine(prompt);
            if (value == null) {
                return -1;
            }

            value = value.trim();
            try {
                int choice = Integer.parseInt(value);
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException ex) {
                // Fall through to the validation message below.
            }
            System.out.println("Enter a number between " + min + " and " + max + ".");
        }
    }

    private BigDecimal readAmount(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value == null) {
                throw new IllegalArgumentException("Input ended before amount was provided.");
            }

            value = value.trim();
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid numeric amount.");
            }
        }
    }

    private void printSuccess(OperationResult result) {
        System.out.println(result.getMessage());
        System.out.println("Updated balance: " + formatAmount(result.getBalance()));
    }

    private String formatAmount(BigDecimal amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) {
            return null;
        }
        return sc.nextLine();
    }
}
