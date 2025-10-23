import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Account Class: Represents a single bank account.
 * Key OOP Concept: Encapsulation (private data, public methods).
 */
class Account {
    // Private attributes (Encapsulation: data hiding)
    private final String accountNumber; // Final since it won't change after creation
    private final String accountHolderName;
    private double balance;

    /**
     * Constructor to initialize a new Account object.
     */
    public Account(String accountHolderName, double initialDeposit) {
        this.accountNumber = generateAccountNumber();
        this.accountHolderName = accountHolderName;
        this.balance = Math.max(0, initialDeposit); 
        System.out.println("\nSUCCESS: Account created for " + accountHolderName + ".\nAccount No: " + this.accountNumber);
    }

    // --- Accessor (Getter) Methods ---

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    // --- Mutator (Behavior) Methods ---

    /**
     * Deposits money into the account.
     */
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    /**
     * Withdraws money from the account.
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Private helper method for generating a random account number.
     */
    private String generateAccountNumber() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(1000000000));
    }

    @Override
    public String toString() {
        return String.format("Account No: %s | Holder: %s | Balance: $%.2f", 
                             accountNumber, accountHolderName, balance);
    }
}

// ---------------------------------------------------

/**
 * Bank Class: Manages a collection of accounts.
 * Key OOP Concept: Aggregation (Bank has a List of Accounts).
 */
class Bank {
    // List to store all accounts (Aggregation)
    private final List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    /**
     * Adds a new account to the bank's list.
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     * Finds an account by its account number.
     */
    public Account findAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Displays details for all accounts.
     */
    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("The bank currently has no registered accounts.");
            return;
        }
        System.out.println("\n--- All Bank Accounts Summary (" + accounts.size() + ") ---");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
        System.out.println("-----------------------------------------\n");
    }
}

// ---------------------------------------------------

/**
 * Main Application Class: Handles user interface logic.
 */
public class BankingSystemApp {
    private static final Bank bank = new Bank();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("|| Welcome to the Simple Banking System ||");
        System.out.println("=========================================");
        
        int choice;
        do {
            displayMenu();
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                handleUserChoice(choice);
            } else {
                System.out.println("\nERROR: Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
                choice = -1;
            }
        } while (choice != 6);

        System.out.println("\nThank you for using the Banking System. Goodbye!");
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Create New Account");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Check Balance");
        System.out.println("5. View All Accounts");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                doTransaction("deposit");
                break;
            case 3:
                doTransaction("withdraw");
                break;
            case 4:
                checkBalance();
                break;
            case 5:
                bank.displayAllAccounts();
                break;
            case 6:
                // Handled in main loop
                break;
            default:
                System.out.println("\nInvalid choice. Please select a number between 1 and 6.");
        }
    }

    private static void createAccount() {
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        
        double initialDeposit = -1;
        while (initialDeposit < 0) {
            System.out.print("Enter Initial Deposit Amount (minimum $0.00): $");
            if (scanner.hasNextDouble()) {
                initialDeposit = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (initialDeposit < 0) {
                    System.out.println("Deposit must be non-negative.");
                }
            } else {
                System.out.println("Invalid amount entered.");
                scanner.nextLine(); // consume invalid input
                initialDeposit = -1;
            }
        }
        
        Account newAccount = new Account(name, initialDeposit);
        bank.addAccount(newAccount);
    }

    private static Account findAndValidateAccount() {
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();
        Account account = bank.findAccount(accNum);

        if (account == null) {
            System.out.println("\nERROR: Account not found with number " + accNum);
            return null;
        }
        return account;
    }

    private static void doTransaction(String type) {
        Account account = findAndValidateAccount();
        if (account == null) return;

        double amount = -1;
        String prompt = (type.equals("deposit")) ? "Enter Deposit Amount: $" : "Enter Withdrawal Amount: $";

        while (amount <= 0) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (amount <= 0) {
                    System.out.println("Amount must be positive.");
                }
            } else {
                System.out.println("Invalid amount entered.");
                scanner.nextLine(); // consume invalid input
                amount = -1;
            }
        }

        boolean success = false;
        if (type.equals("deposit")) {
            success = account.deposit(amount);
            if (success) {
                System.out.printf("\nSUCCESS: Deposited $%.2f. New balance: $%.2f\n", amount, account.getBalance());
            } else {
                System.out.println("\nTRANSACTION FAILED: Deposit error.");
            }
        } else { // withdraw
            success = account.withdraw(amount);
            if (success) {
                System.out.printf("\nSUCCESS: Withdrew $%.2f. New balance: $%.2f\n", amount, account.getBalance());
            } else {
                System.out.println("\nTRANSACTION FAILED: Insufficient funds or invalid amount.");
                System.out.printf("Current balance: $%.2f\n", account.getBalance());
            }
        }
    }

    private static void checkBalance() {
        Account account = findAndValidateAccount();
        if (account != null) {
            System.out.printf("\n*** Balance Check ***\nAccount Holder: %s\nCurrent Balance: $%.2f\n", 
                              account.getAccountHolderName(), account.getBalance());
        }
    }
}
