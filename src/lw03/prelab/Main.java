package lw03.prelab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Main {


    private static Scanner openFile(String name) {
        InputStream in = Main.class.getResourceAsStream(name);
        if (in != null) {
            return new Scanner(in);
        }
        String[] paths = {
            "src/lw03/prelab/" + name,
            "lw03/prelab/" + name,
            name
        };
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                try {
                    return new Scanner(f);
                } catch (FileNotFoundException e) {
                    // try next path
                }
            }
        }
        System.out.println("ERROR: cannot find " + name
                + ". Put it in src/lw03/prelab/ next to Main.java.");
        System.exit(1);
        return null;
    }

    public static void main(String[] args) {
        problem1();
        System.out.println();
        problem2();
        System.out.println();
        problem3();
    }

    // ===== Problem 1: Playlist (List) =====
    private static void problem1() {
        Scanner sc = openFile("playlist.txt");
        List<String> playList = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            int sp = line.indexOf(" ");
            String operation = line.substring(0, sp);
            String details = line.substring(sp + 1).trim();

            if (operation.equals("ADD")) {
                playList.add(details);
            } else if (operation.equals("INSERT")) {
                int sp2 = details.indexOf(" ");
                int index = Integer.parseInt(details.substring(0, sp2));
                String song = details.substring(sp2 + 1).trim();
                if (index >= 0 && index <= playList.size()) {
                    playList.add(index, song);
                } else {
                    playList.add(song); // index out of range -> put at the end
                }
            } else { // REMOVE
                playList.remove(details); // does nothing if song not found
            }
        }
        sc.close();

        System.out.println("===== Problem 1 =====");
        System.out.println("Total: " + playList.size());
        int number = 1;
        for (String p : playList) {
            System.out.println(number + ": " + p);
            number++;
        }
    }

    // ===== Problem 2: Participants (Set) =====
    private static void problem2() {
        Scanner sc = openFile("participants.txt");
        Set<String> students = new LinkedHashSet<>();
        int duplicate = 0;

        while (sc.hasNextLine()) {
            String name = sc.nextLine().trim();
            if (name.isEmpty()) continue;

            if (!students.add(name)) { // add() returns false if already there
                duplicate++;
            }
        }
        sc.close();

        System.out.println("===== Problem 2 =====");
        System.out.println("Unique participants: " + students.size());
        int no = 1;
        for (String s : students) {
            System.out.println(no + ". " + s);
            no++;
        }
        System.out.println("Duplicate registrations: " + duplicate);
    }

    // ===== Problem 3: Inventory (Map) =====
    private static void problem3() {
        Scanner sc = openFile("inventory.txt");
        Map<String, Integer> products = new LinkedHashMap<>();
        int failedTrx = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String type = parts[0];
            String product = parts[1];
            int qty = Integer.parseInt(parts[2]);

            if (type.equals("ADD")) {
                products.put(product, products.getOrDefault(product, 0) + qty);
            } else { // SELL
                if (products.containsKey(product) && products.get(product) >= qty) {
                    products.put(product, products.get(product) - qty);
                } else {
                    failedTrx++;
                }
            }
        }
        sc.close();

        System.out.println("===== Problem 3 =====");
        for (String p : products.keySet()) {
            System.out.println(p + ": " + products.get(p));
        }
        System.out.println("Failed sales: " + failedTrx);
    }
}
