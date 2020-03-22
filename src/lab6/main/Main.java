package lab6.main;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    Person[] people = new Person[0];

    public static void main(String[] args) {
        new Main().run();
    }

    public Person[] emptyArray() {
        return new Person[100];
    }

    private void run() {
        int selection;
        while ((selection = menu()) != 0) {
            switch (selection) {
                case 1:
                    people = readFromFile();
                    break;
                case 2:
                    writeToFile(people);
                    break;
                case 3:
                    printArray();
                    break;
                case 4:
                    writeToBinaryFile();
                    break;
                case 5:
                    people = readFromBinaryFile();
                    break;
                case 6:
                    System.out.println("Year: ");
                    int year = new Scanner(System.in).nextInt();
                    Person[] find = findByYear(people,year);
                    printArray(find);
                    break;
            }
        }
    }

    private Person[] findByYear(Person[] people, int year) {
        Person[] array = emptyArray();
        int count = 0;
        for (Person person : people) {
            if (person.getBirthday().isAfter(LocalDate.of(year, 12, 31))) {
                array[count] = person;
                count++;
            }
        }
        return Arrays.copyOf(array, count);
    }

    private void printArray(Person[] people) {
        System.out.println("--- Found people ---");
        for (Person person : people) {
            System.out.println(person);
        }
    }

    private Person[] readFromBinaryFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("people.dat")))) {
            return (Person[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Person[0];
        }
    }

    private void writeToBinaryFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("people.dat"))) {
            oos.writeObject(people);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(Person[] people) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d M yyyy");
        try (PrintWriter out = new PrintWriter("people1.txt")) {
            for (Person person : people) {
                out.print(person.getId()+" "+person.getName()+" ");
                out.print(formatter.format(person.getBirthday()));
                out.println(" "+person.getRating());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Person[] readFromFile() {
        Person[] tmp = emptyArray();
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("people.txt"))) {
            String line;
            while ((line = reader.readLine())!=null) {
                String[] s = line.split(" ");
                int id = Integer.parseInt(s[0]);
                String name = s[1];
                int day = Integer.parseInt(s[2]);
                int month = Integer.parseInt(s[3]);
                int year = Integer.parseInt(s[4]);
                double rating = Double.parseDouble(s[5]);
                Person p = new Person(id, name, LocalDate.of(year, month, day), rating);
                tmp[count] = p;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.copyOf(tmp, count);
    }

    private void printArray() {
        System.out.println("--- All people in array ---");
        for (Person person : people) {
            System.out.println(person);
        }
    }

    private int menu() {
        System.out.println("1. Чтение из файла");
        System.out.println("2. Запись в файл");
        System.out.println("3. Вывод массива на экран");
        System.out.println("4. Запись в бинарный файл");
        System.out.println("5. Чтение из бинарного файла");
        System.out.println("6. Родившиеся после заданного года");
        System.out.println("--------------------------");
        System.out.println("0. Выход");
        return new Scanner(System.in).nextInt();
    }
}
