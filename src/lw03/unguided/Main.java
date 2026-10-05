package lw03.unguided;

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
            "src/lw03/unguided/" + name,
            "lw03/unguided/" + name,
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
                + ". Put it in src/lw03/unguided/ next to Main.java.");
        System.exit(1);
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = openFile("enrollment.txt");

        Map<String, Integer> enrollment = new HashMap<>(); // course code -> current students
        List<String> courseOrder = new ArrayList<>();      // order each course first appeared
        List<String> checkResults = new ArrayList<>();     // CHECK results in input order
        int rejected = 0;

        while (sc.hasNext()) {
            String operation = sc.next();
            String course = sc.next();

            if (operation.equals("CHECK")) {
                // CHECK never creates a course record
                if (enrollment.containsKey(course)) {
                    checkResults.add(course + ": " + enrollment.get(course) + " students");
                } else {
                    checkResults.add(course + ": Not found");
                }
            } else {
                int count = sc.nextInt();

                if (count <= 0) {
                    // zero or negative count -> invalid operation
                    rejected++;
                } else if (operation.equals("REGISTER")) {
                    if (enrollment.containsKey(course)) {
                        enrollment.put(course, enrollment.get(course) + count);
                    } else {
                        enrollment.put(course, count);
                        courseOrder.add(course);
                    }
                } else if (operation.equals("WITHDRAW")) {
                    if (enrollment.containsKey(course) && enrollment.get(course) >= count) {
                        enrollment.put(course, enrollment.get(course) - count);
                    } else {
                        // course does not exist or not enough students
                        rejected++;
                    }
                } else {
                    // unknown operation
                    rejected++;
                }
            }
        }
        sc.close();

        System.out.println("===== Enrollment Checks =====");
        for (int i = 0; i < checkResults.size(); i++) {
            System.out.println(checkResults.get(i));
        }

        System.out.println("===== Final Enrollment =====");
        for (int i = 0; i < courseOrder.size(); i++) {
            String course = courseOrder.get(i);
            System.out.println(course + ": " + enrollment.get(course) + " students");
        }
        System.out.println("Rejected operations: " + rejected);
    }
}
