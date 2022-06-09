package Testas0609;

import Testas0609.Entities.User;
import Testas0609.Entities.UserTransactions;
import Testas0609.Utilities.NotEnoughMoneyException;
import Testas0609.Utilities.UserNotFoundException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import java.time.LocalDateTime;
import java.util.Scanner;

import static Testas0609.Client.ClientProvider.getObjectMongoClient;
import static Testas0609.Utilities.Utilities.notEmptyDouble;
import static Testas0609.Utilities.Utilities.stringNotEmpty;

public class Main {
    MongoCollection<User> userCollection;
    MongoCollection<UserTransactions> transactionCollection;
    FindIterable<User> users;
    FindIterable<UserTransactions> uTrans;

    public static void main(String[] args) {
        Main m = new Main();

        MongoClient mc = getObjectMongoClient();

        MongoDatabase mDB = mc.getDatabase("LBA_DB");
        m.userCollection = mDB.getCollection("Users", User.class);
        m.transactionCollection = mDB.getCollection("UserTransactions", UserTransactions.class);

        m.users = m.userCollection.find();
        m.uTrans = m.transactionCollection.find();

        Scanner sc = new Scanner(System.in);
        m.menu(sc);
    }

    private void menu(Scanner sc) {
        String input;
        do {
            System.out.println("""
                    [1] Prisijunti
                    [2] Registruotis
                    [3] " "  
                    [0] Atsijungti
                    """);
            input = sc.nextLine();
            switch (input) {
                case "1" -> prisijunti(sc);
                case "2" -> registruotis(sc);
                case "3" -> System.out.print("");
                case "0" -> System.out.println("Viso gero");
            }
        } while (!input.equals("0"));
    }

    private void registruotis(Scanner sc) {
        String name = stringNotEmpty("Įveskite vardą", sc);
        String surname = stringNotEmpty("Įveskite pavardę", sc);
        String username = stringNotEmpty("Įveskite vartotojo prisijungimą", sc);
        String password = stringNotEmpty("Įveskite slaptažodį", sc);
        //Password.repeatcheck(password);

        User newUser = new User(name, surname, username, password);
        userCollection.insertOne(newUser);
    }

    private void prisijunti(Scanner sc) {
        String username = stringNotEmpty("Įveskite vartotoją", sc);
        String password = stringNotEmpty("Įveskite slaptažodį", sc);
        User loginUser = checkLogin(username, password);
        if (loginUser != null) {
            userMenu(loginUser, sc);
        } else {
            System.out.println("neteisingi prisijungimo duomenys");
        }
    }


    private User checkLogin(String username, String password) {
        for (User u : users) {
            if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                return u;
            }
        }
        return null;
    }

    private void userMenu(User user, Scanner sc) {
        System.out.printf("Laba diena, %s %s", user.getName(), user.getSurname());
        String input;
        do {
            System.out.println("""
                    [1] Pervesti pinigus
                    [2] Pažiūrėti likutį
                    [3] Pridėti pinigų į sąskaitą
                    [0] Atsijungti 
                    """);
            input = sc.nextLine();
            switch (input) {
                case "1" -> pervedimas(user, sc);
                case "2" -> System.out.printf("Sąskaitoje yra %.2f €", user.getMoney());
                case "2" -> addMoney(user, sc);
                case "0" -> System.out.println("Viso gero");
            }
        } while (!input.equals("0"));

    }

    private void addMoney(User user, Scanner sc) {
    }

    private void pervedimas (User user, Scanner sc) {
        Double suma = notEmptyDouble("Pasirinkite kiek norite pervesti", sc);
        String gavėjoVardas = stringNotEmpty("Įrašykite gavėjo vardą", sc);
        String gavėjoPavardė = stringNotEmpty("Įrašykite gavėjo pavardę", sc);
        User gavėjas = findUserByFullName(gavėjoVardas, gavėjoPavardė);
        try {
            LocalDateTime time = LocalDateTime.now();
            UserTransactions newTrans = new UserTransactions(user, gavėjas, suma, time, false);
            transactionCollection.insertOne(newTrans);
            if (gavėjas==null){
                throw UserNotFoundException();
            }
            if (user.getMoney()-suma>=0){
                user.setMoney(user.getMoney()-suma);
                gavėjas.setMoney(gavėjas.getMoney()+suma);
            } else {
                throw NotEnoughMoneyException();
            }
            transactionCollection.updateOne(newTrans, Updates.set("isComplete"),true);
        } catch (NotEnoughMoneyException e){
            System.out.println("Sąskaitoje neužtenka pinigų");
        } catch (UserNotFoundException e){
            System.out.println("Tokio gavėjo nėra");
        }


    }
}
