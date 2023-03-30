/**
 * grp3 - This is a rental application program for cars. The program will account for rentals, returns, required servicing, finished servicing. The program will work with a predefined car class and account for car specifications.
 * 
 * @author Christopher Ashe, Drew Temple-Smith, Toby Sun
 * @version Due day Dec 12, 2021
 */
// Standard import for the Scanner class
import java.util.*;
import java.io.*;

public class group_Assignment3 {
    public static void main (String [] args) throws IOException {
        // Create a Scanner object attached to the keyboard
        Scanner in = new Scanner (System.in);
        //create verables for rentals and revenue
        int totalRentals = 0, totalRevenue = 0;
        char selection = ' ';
        // Create an array list for cars
        ArrayList<Car> cars = new ArrayList<Car>();
        // print out welcome banner
        System.out.println("*** Welcome to Mo's Classic Car Rentals ***");
        //get file name from user
        System.out.print("Enter car data filename: ");
        String carFile = in.nextLine();
        //invoke the loadCars method
        loadCars(carFile, cars);

        //display cars array
        do {
            displayCars(cars);
            //invoke getOption
            selection = getOption(in);
            switch(selection) {
                    //option A (Rent a car)
                case 'A':
                    System.out.print("Rent a car. Enter a car selection (by number): ");
                    int pick = in.nextInt() - 1;
                    if(cars.get(pick).getNeedsService()){
                        System.out.println("The " + cars.get(pick).getMake() + " " + cars.get(pick).getModel() + " is not available to rent.");
                        System.out.println("Press [Enter] to continue...");
                        try{System.in.read();}
                        catch(Exception e){}
                    } else if(cars.get(pick).getIsRented()) {
                        System.out.println("The " + cars.get(pick).getMake() + " " + cars.get(pick).getModel() + " is not available to rent.");
                        System.out.println("Press [Enter] to continue...");
                        try{System.in.read();}
                        catch(Exception e){}
                    }
                    else{
                        in = new Scanner(System.in);
                        System.out.print("Enter renter's name: ");
                        String renterName = in.nextLine();
                        System.out.print("Enter renter's phone: ");
                        String rentersNumber = in.nextLine();

                        // increase rentals and revenue
                        totalRentals++;
                        totalRevenue += cars.get(pick).getRate();
                        //add renters name and number to file
                        cars.get(pick).setRented(renterName, rentersNumber);
                    }
                    break;
                    //option B (return a car)
                case 'B':
                    System.out.print("Return a car. Enter a car selection (by number): ");
                    pick = in.nextInt() - 1;
                    if(!cars.get(pick).getIsRented()) {
                        System.out.println("The " + cars.get(pick).getMake() + " " + cars.get(pick).getModel() + " is not rented and cannot be returned.");
                        System.out.println("Press [Enter] to continue...");
                        try{System.in.read();}
                        catch(Exception e){}
                    }else {
                        cars.get(pick).setReturned();
                    }
                    break;
                    //option C (Mark car for servicing)
                case 'C':
                    System.out.print("Flag car for servicing. Enter a car selection (by number): ");
                    pick = in.nextInt() - 1;
                    if(cars.get(pick).getNeedsService()){
                        System.out.println("The " + cars.get(pick).getMake() + " " + cars.get(pick).getModel() + " is not flagged as needing service.");
                    }else {
                        cars.get(pick).setNeedsService(true);
                    }
                    break;
                    //option D (clear servicing for a car)
                case 'D':
                    System.out.print("Clear car for servicing. Enter a car selection (by number): ");
                    // Pick vehicle from array list, -1 for proper index
                    pick = in.nextInt() -1;
                    if(!cars.get(pick).getNeedsService()){
                        System.out.print("The " + cars.get(pick).getMake() + " " + cars.get(pick).getModel() + " is not flagged as needing service.");
                    }else {
                        cars.get(pick).setNeedsService(false);
                    }
                    break;
            }
            saveCars(carFile, cars);
            //Finalize program with E selection and print results
        } while (selection != 'E');
        System.out.println("Number of rentals initiated in this session: " + totalRentals);
        System.out.println("Total rental rate revenue from this session: $" + totalRevenue);
        System.out.println("\nGood bye!");
    }
    //loadCars method
    public static void loadCars(String carFile, ArrayList<Car> cars) throws IOException {
        //create a scanner for car file
        File file = new File(carFile);
        Scanner inFile = new Scanner (file);

        //used to input a whole line of data
        String data = "";
        String [] items = null;

        while(inFile.hasNext()) {
            //read entire line
            data = inFile.nextLine();
            //split line into component parts
            items = data.split(",");
            //get car data
            String year = (items[0]);
            String make = (items[1]);
            String modle = (items[2]);
            String rate = (items[3]);
            String needService = (items[4]);
            String rented = (items[5]);

            //add to cars array, if not rented add void strings for ited 6 and 7
            if(Boolean.parseBoolean(items[5])){
                cars.add(new Car(items[1],items[2], Integer.parseInt(items[0]), Integer.parseInt(items[3]), Boolean.parseBoolean(items[4]), 
                        Boolean.parseBoolean(items[5]), items[6], items[7]));
            }
            else {cars.add(new Car(items[1],items[2], Integer.parseInt(items[0]), Integer.parseInt(items[3]), Boolean.parseBoolean(items[4]), 
                        Boolean.parseBoolean(items[5]), "", ""));
            }
        }
        inFile.close();
    }
    //dispayCars method
    public static void displayCars(ArrayList<Car> cars) {
        System.out.println("");
        System.out.println("                        *   *   *");
        System.out.println("Cars");
        for(int i = 0; i < cars.size(); i++) {
            System.out.println((i + 1) + ". " + cars.get(i).toString());
        }
    }
    //getOption method
    public static char getOption(Scanner in) {
        System.out.println("");
        System.out.println("Options");
        System.out.println("A. Rent a car");
        System.out.println("B. Return a car");
        System.out.println("C. Flag car for servicing");
        System.out.println("D. Clear car from servicing");
        System.out.println("E. To Exit program");
        System.out.println("");
        System.out.print("Enter your option (by letter): ");
        char choice = in.next().toUpperCase().charAt(0);
        System.out.println("");
        return choice;
    }
    //saveCars method
    public static void saveCars(String carFile, ArrayList<Car> cars) throws IOException {
        File file = new File(carFile);
        FileWriter writer = new FileWriter(file);
        String x = "";
        for(int i = 0; i < cars.size(); i++) {
            x += cars.get(i).getYear() + "," + cars.get(i).getMake() + "," + cars.get(i).getModel() + "," + cars.get(i).getRate() + ","
            + cars.get(i).getNeedsService() + "," + cars.get(i).getIsRented() + "," + cars.get(i).getRenterName() + "," + cars.get(i).getRenterPhone() + "\n";
        }
        writer.write(x);
        writer.close();
    }
}
