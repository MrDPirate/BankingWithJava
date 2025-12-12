package com.ga.Banking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String[] CARD_TYPES = {"Mastercard", "Mastercard Titanium", "Mastercard Platinum"};
    private static final ITrans TRANSACTION_SERVICE = new Trans();

    public static void main(String[] args) {
        List<String> existingUsers = dbHelper.getFileNamesIn_db();
        boolean firstBoot = existingUsers == null || existingUsers.isEmpty();

        if (firstBoot) {
            runFirstBootSetup();
        } else {
            System.out.println("Existing users detected. Starting application in regular mode.");
            runApplication();
        }
    }

    private static void runFirstBootSetup() {
        System.out.println("==== First Boot Detected ====");
        System.out.println("No users were found. Create an initial banker to continue.\n");

        String username = promptForUniqueUsername();
        String password = promptForPassword();
        String name = promptNonEmpty("Banker full name: ");
        boolean hasSaving = promptYesNo("Create a saving account for the banker? (y/n): ");
        boolean hasChecking = promptYesNo("Create a checking account for the banker? (y/n): ");

        double savingBalance = hasSaving ? promptNonNegativeAmount("Initial saving balance: ") : 0.0;
        double checkingBalance = hasChecking ? promptNonNegativeAmount("Initial checking balance: ") : 0.0;

        String savingCardType = hasSaving
                ? promptCardType("Saving card type (Mastercard, Mastercard Titanium, Mastercard Platinum): ")
                : CARD_TYPES[0];
        String checkingCardType = hasChecking
                ? promptCardType("Checking card type (Mastercard, Mastercard Titanium, Mastercard Platinum): ")
                : CARD_TYPES[0];

        Auth.register(
                username,
                password,
                "Banker",
                name,
                String.valueOf(hasSaving),
                String.valueOf(hasChecking),
                String.valueOf(savingBalance),
                String.valueOf(checkingBalance),
                savingCardType,
                checkingCardType,
                "0"
        );

        System.out.println("\nInitial banker created successfully. Entering regular mode.\n");
        runApplication();
    }

    private static void runApplication() {
        boolean running = true;
        Users currentUser = null;

        while (running) {
            if (currentUser == null) {
                System.out.println("==== Banking Console ====");
                System.out.println("1) Login");
                System.out.println("2) Register Customer");
                System.out.println("3) Exit");
                System.out.print("Select an option: ");
                String choice = SCANNER.nextLine().trim();

                switch (choice) {
                    case "1" -> currentUser = handleLogin();
                    case "2" -> handleRegistration("Customer");
                    case "3" -> running = false;
                    default -> System.out.println("Invalid option, please try again.\n");
                }
            } else {
                currentUser = runUserSession(currentUser);
            }
        }

        System.out.println("Goodbye!");
    }

    private static Users handleLogin() {
        System.out.println("\n==== Login ====");
        String username = promptNonEmpty("Username: ");

        if (!dbHelper.checkUserExist(username)) {
            System.out.println("No user found with that username.\n");
            return null;
        }

        Users user = buildUserFromStore(username);
        if (user == null) {
            return null;
        }
        String password = promptNonEmpty("Password: ");

        boolean loggedIn = Auth.login(user, username, password);
        if (loggedIn) {
            System.out.println("You are now logged in as " + user.getName() + ".");
            return user;
        }
        return null;
    }

    private static void handleRegistration(String role) {
        System.out.println("\n==== " + role + " Registration ====");
        String username = promptForUniqueUsername();
        String password = promptForPassword();
        String name = promptNonEmpty("Full name: ");
        boolean hasSaving = promptYesNo("Add a saving account? (y/n): ");
        boolean hasChecking = promptYesNo("Add a checking account? (y/n): ");

        double savingBalance = hasSaving ? promptNonNegativeAmount("Initial saving balance: ") : 0.0;
        double checkingBalance = hasChecking ? promptNonNegativeAmount("Initial checking balance: ") : 0.0;

        String savingCardType = hasSaving
                ? promptCardType("Saving card type (Mastercard, Mastercard Titanium, Mastercard Platinum): ")
                : CARD_TYPES[0];
        String checkingCardType = hasChecking
                ? promptCardType("Checking card type (Mastercard, Mastercard Titanium, Mastercard Platinum): ")
                : CARD_TYPES[0];

        Auth.register(
                username,
                password,
                role,
                name,
                String.valueOf(hasSaving),
                String.valueOf(hasChecking),
                String.valueOf(savingBalance),
                String.valueOf(checkingBalance),
                savingCardType,
                checkingCardType,
                "0"
        );

        System.out.println(role + " account created successfully.\n");
    }

    private static Users runUserSession(Users user) {
        Users activeUser = user;
        boolean inSession = true;

        while (inSession) {
            boolean banker = isBanker(activeUser);
            System.out.println("==== Logged in as " + activeUser.getName() + " (" + activeUser.getRole() + ") ====");
            if (banker) {
                System.out.println("1) View Account Summary");
                System.out.println("2) Change Password");
                System.out.println("3) View Transactions");
                System.out.println("4) Register Customer");
                System.out.println("5) Register Banker");
                System.out.println("6) Deposit");
                System.out.println("7) Withdraw");
                System.out.println("8) Transfer Between Own Accounts");
                System.out.println("9) Transfer To Another User");
                System.out.println("10) Logout");
            } else {
                System.out.println("1) View Account Summary");
                System.out.println("2) Change Password");
                System.out.println("3) View Transactions");
                System.out.println("4) Deposit");
                System.out.println("5) Withdraw");
                System.out.println("6) Transfer Between Own Accounts");
                System.out.println("7) Transfer To Another User");
                System.out.println("8) Logout");
            }

            System.out.print("Select an option: ");
            String choice = SCANNER.nextLine().trim();

            if (banker) {
                switch (choice) {
                    case "1" -> showAccountSummary(activeUser);
                    case "2" -> handleChangePassword(activeUser);
                    case "3" -> handleTransactionFilters(activeUser);
                    case "4" -> handleRegistration("Customer");
                    case "5" -> handleRegistration("Banker");
                    case "6" -> handleDeposit(activeUser);
                    case "7" -> handleWithdraw(activeUser);
                    case "8" -> handleTransferLocal(activeUser);
                    case "9" -> handleTransferToUser(activeUser);
                    case "10" -> inSession = false;
                    default -> System.out.println("Invalid option, please try again.\n");
                }
            } else {
                switch (choice) {
                    case "1" -> showAccountSummary(activeUser);
                    case "2" -> handleChangePassword(activeUser);
                    case "3" -> handleTransactionFilters(activeUser);
                    case "4" -> handleDeposit(activeUser);
                    case "5" -> handleWithdraw(activeUser);
                    case "6" -> handleTransferLocal(activeUser);
                    case "7" -> handleTransferToUser(activeUser);
                    case "8" -> inSession = false;
                    default -> System.out.println("Invalid option, please try again.\n");
                }
            }

            if (inSession) {
                Users refreshed = reloadUser(activeUser);
                if (refreshed == null) {
                    System.out.println("Session ended due to user data issues.\n");
                    Auth.logout(activeUser);
                    return null;
                }
                activeUser = refreshed;
            }
        }

        Auth.logout(activeUser);
        System.out.println("Logged out of " + activeUser.getUsername() + ".\n");
        return null;
    }

    private static void handleDeposit(Users user) {
        System.out.println("\n==== Deposit ====");
        Card card = promptForCard(user, "Deposit to which account (saving/checking): ");
        if (card == null) {
            return;
        }
        double amount = promptPositiveAmount("Amount to deposit: ");
        TRANSACTION_SERVICE.deposit(user, card, amount);
    }

    private static void handleWithdraw(Users user) {
        System.out.println("\n==== Withdraw ====");
        Card card = promptForCard(user, "Withdraw from which account (saving/checking): ");
        if (card == null) {
            return;
        }
        double amount = promptPositiveAmount("Amount to withdraw: ");
        TRANSACTION_SERVICE.withdraw(user, card, amount);
    }

    private static void handleTransferLocal(Users user) {
        System.out.println("\n==== Transfer Between Own Accounts ====");
        if (!user.getHasSavingAccount() || !user.getHasCheckingAccount()) {
            System.out.println("You need both saving and checking accounts for this operation.\n");
            return;
        }
        Card fromCard = promptForCard(user, "Transfer from which account (saving/checking): ");
        if (fromCard == null) {
            return;
        }
        String toAccountType = fromCard.getAccountType().equalsIgnoreCase("Saving") ? "Checking" : "Saving";
        double amount = promptPositiveAmount("Amount to transfer: ");
        TRANSACTION_SERVICE.transferLocal(user, fromCard, toAccountType, amount);
    }

    private static void handleTransferToUser(Users user) {
        System.out.println("\n==== Transfer To Another User ====");
        String recipient = promptNonEmpty("Recipient username: ");
        if (recipient.equalsIgnoreCase(user.getUsername())) {
            System.out.println("Use the local transfer option to move money between your accounts.\n");
            return;
        }
        if (!dbHelper.checkUserExist(recipient)) {
            System.out.println("No user found with username '" + recipient + "'.\n");
            return;
        }

        Users recipientUser = buildUserFromStore(recipient);
        if (recipientUser == null) {
            System.out.println("Recipient data is unavailable or corrupt.\n");
            return;
        }

        Card fromCard = promptForCard(user, "Transfer from which account (saving/checking): ");
        if (fromCard == null) {
            return;
        }
        String toAccountType = promptAccountTypeForUser(recipientUser,
                "Recipient account type (saving/checking): ");
        if (toAccountType == null) {
            return;
        }
        double amount = promptPositiveAmount("Amount to transfer: ");
        TRANSACTION_SERVICE.transfer(user, recipient, fromCard, toAccountType, amount);
    }

    private static void handleChangePassword(Users user) {
        System.out.println("\n==== Change Password ====");
        String current = promptNonEmpty("Current password: ");
        String newPass = promptNonEmpty("New password: ");
        String confirm = promptNonEmpty("Confirm new password: ");
        Auth.changePass(user, current, newPass, confirm);
    }

    private static void handleTransactionFilters(Users user) {
        boolean viewing = true;
        while (viewing) {
            System.out.println("\n==== Transaction History ====");
            System.out.println("1) Show All");
            System.out.println("2) Today");
            System.out.println("3) Yesterday");
            System.out.println("4) Same Day Last Week");
            System.out.println("5) Custom Date Range");
            System.out.println("6) Filter by Transaction Type");
            System.out.println("7) Filter by Account Type");
            System.out.println("8) Back");
            System.out.print("Select an option: ");
            String choice = SCANNER.nextLine().trim();

            switch (choice) {
                case "1" -> displayTransactions("All Transactions",
                        TransDB.getUserData(user.getUsername()));
                case "2" -> displayTransactions("Today's Transactions",
                        TransDB.filterByDay(user.getUsername(), LocalDateTime.now()));
                case "3" -> displayTransactions("Yesterday's Transactions",
                        TransDB.filterByYesterday(user.getUsername()));
                case "4" -> displayTransactions("Same Day Last Week",
                        TransDB.filterByLastWeek(user.getUsername()));
                case "5" -> handleCustomDateRangeFilter(user);
                case "6" -> {
                    String tranType = promptNonEmpty("Transaction type to match: ");
                    displayTransactions("Transactions of type '" + tranType + "'",
                            TransDB.filterByTranType(user.getUsername(), tranType));
                }
                case "7" -> {
                    String accountType = promptAccountTypeChoice("Account type to filter (saving/checking): ");
                    displayTransactions("Transactions on " + accountType + " account",
                            TransDB.filterByAccountType(user.getUsername(), accountType));
                }
                case "8" -> viewing = false;
                default -> System.out.println("Invalid option, please try again.\n");
            }
        }
    }

    private static void handleCustomDateRangeFilter(Users user) {
        LocalDate fromDate = promptDate("Start date (yyyy-MM-dd): ");
        LocalDate toDate = promptDate("End date (yyyy-MM-dd): ");
        if (toDate.isBefore(fromDate)) {
            System.out.println("End date must be on or after the start date.\n");
            return;
        }
        LocalDateTime fromDateTime = fromDate.minusDays(1).atStartOfDay();
        LocalDateTime toDateTime = toDate.plusDays(1).atStartOfDay();
        displayTransactions("Transactions from " + fromDate + " to " + toDate,
                TransDB.filterByCustomDays(user.getUsername(), fromDateTime, toDateTime));
    }

    private static Card promptForCard(Users user, String message) {
        boolean hasSaving = user.getHasSavingAccount();
        boolean hasChecking = user.getHasCheckingAccount();

        if (!hasSaving && !hasChecking) {
            System.out.println("No active accounts found for this user.\n");
            return null;
        }

        while (true) {
            String choice = promptNonEmpty(message).toLowerCase();
            if (choice.startsWith("s")) {
                if (hasSaving) {
                    return user.getSaveCard();
                }
                System.out.println("Saving account is not available for this user.\n");
            } else if (choice.startsWith("c")) {
                if (hasChecking) {
                    return user.getCheckCard();
                }
                System.out.println("Checking account is not available for this user.\n");
            } else {
                System.out.println("Please choose 'saving' or 'checking'.\n");
            }
        }
    }

    private static double promptPositiveAmount(String message) {
        while (true) {
            System.out.print(message);
            String input = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // handled below
            }
            System.out.println("Please enter a number greater than zero.\n");
        }
    }

    private static void showAccountSummary(Users user) {
        System.out.println("\n---- Account Summary ----");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Name: " + user.getName());
        System.out.println("Role: " + user.getRole());
        System.out.println("Logged in: " + user.getIsLoggedIn());
        System.out.println("Failed login attempts: " + user.getFailed_login_attempts());
        System.out.println("Account locked until: " + user.getAccount_locked_until());
        System.out.println("Created at: " + user.getCreated_at());
        System.out.println("Last updated at: " + user.getUpdated_at());
        System.out.println("Has saving account: " + user.getHasSavingAccount());
        if (user.getHasSavingAccount()) {
            System.out.println("  Saving card: " + user.getSavingCard());
            System.out.println("  Saving balance: " + user.getSavingAmount());
        }
        System.out.println("Has checking account: " + user.getHasCheckingAccount());
        if (user.getHasCheckingAccount()) {
            System.out.println("  Checking card: " + user.getCheckingCard());
            System.out.println("  Checking balance: " + user.getCheckingAmount());
        }
        System.out.println("Overdraft attempts: " + user.getOverdraftAttempts());
        System.out.println();
    }

    private static Users reloadUser(Users user) {
        return buildUserFromStore(user.getUsername());
    }

    private static boolean isBanker(Users user) {
        return "Banker".equalsIgnoreCase(user.getRole());
    }

    private static void displayTransactions(String title, List<String[]> data) {
        System.out.println("\n---- " + title + " ----");
        if (data == null || data.isEmpty()) {
            System.out.println("No transactions found for this filter.\n");
            return;
        }
        TransDB.printTrans(data);
        System.out.println();
    }

    private static LocalDate promptDate(String message) {
        while (true) {
            String input = promptNonEmpty(message);
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException ex) {
                System.out.println("Please use the yyyy-MM-dd format.\n");
            }
        }
    }

    private static String promptAccountTypeChoice(String message) {
        while (true) {
            String choice = promptNonEmpty(message).toLowerCase();
            if (choice.startsWith("s")) {
                return "Saving";
            }
            if (choice.startsWith("c")) {
                return "Checking";
            }
            System.out.println("Please choose 'saving' or 'checking'.\n");
        }
    }

    private static String promptAccountTypeForUser(Users user, String message) {
        boolean hasSaving = user.getHasSavingAccount();
        boolean hasChecking = user.getHasCheckingAccount();
        if (!hasSaving && !hasChecking) {
            System.out.println("The selected user does not have any active accounts.\n");
            return null;
        }

        while (true) {
            String choice = promptNonEmpty(message).toLowerCase();
            if (choice.startsWith("s")) {
                if (hasSaving) {
                    return "Saving";
                }
                System.out.println("That user does not have a saving account.\n");
            } else if (choice.startsWith("c")) {
                if (hasChecking) {
                    return "Checking";
                }
                System.out.println("That user does not have a checking account.\n");
            } else {
                System.out.println("Please choose 'saving' or 'checking'.\n");
            }
        }
    }

    private static Users buildUserFromStore(String username) {
        String[] userData = dbHelper.getUserData(username);
        if (!isUserDataValid(userData)) {
            System.out.println("Unable to load user data for '" + username + "'.\n");
            return null;
        }
        try {
            return new Users(userData);
        } catch (RuntimeException ex) {
            System.out.println("User data for '" + username + "' is malformed.\n");
            return null;
        }
    }

    private static boolean isUserDataValid(String[] userData) {
        return userData != null && userData.length == 16;
    }

    private static String promptForUniqueUsername() {
        while (true) {
            String username = promptNonEmpty("Username: ");
            if (!dbHelper.checkUserExist(username)) {
                return username;
            }
            System.out.println("That username already exists. Please choose another one.\n");
        }
    }

    private static String promptForPassword() {
        while (true) {
            String password = promptNonEmpty("Password: ");
            String confirmation = promptNonEmpty("Confirm password: ");
            if (password.equals(confirmation)) {
                return password;
            }
            System.out.println("Passwords do not match. Please try again.\n");
        }
    }

    private static String promptNonEmpty(String message) {
        while (true) {
            System.out.print(message);
            String input = SCANNER.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.\n");
        }
    }

    private static boolean promptYesNo(String message) {
        while (true) {
            System.out.print(message);
            String input = SCANNER.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please respond with 'y' or 'n'.\n");
        }
    }

    private static double promptNonNegativeAmount(String message) {
        while (true) {
            System.out.print(message);
            String input = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // handled below
            }
            System.out.println("Please enter a non-negative number.\n");
        }
    }

    private static String promptCardType(String message) {
        while (true) {
            String input = promptNonEmpty(message);
            if (Arrays.stream(CARD_TYPES).anyMatch(type -> type.equalsIgnoreCase(input))) {
                for (String type : CARD_TYPES) {
                    if (type.equalsIgnoreCase(input)) {
                        return type;
                    }
                }
            }
            System.out.println("Invalid card type. Valid options are: " + String.join(", ", CARD_TYPES) + "\n");
        }
    }
}
