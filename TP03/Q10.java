import java.io.*;
import java.util.*;

class Show {
    String id, type, title, director, country;
    String dateAdded, rating, duration;
    int releaseYear;
    List<String> cast = new ArrayList<>();
    List<String> listedIn = new ArrayList<>();

    public Show clone() {
        Show s = new Show();
        s.id = id;
        s.type = type;
        s.title = title;
        s.director = director;
        s.country = country;
        s.dateAdded = dateAdded;
        s.rating = rating;
        s.duration = duration;
        s.releaseYear = releaseYear;
        s.cast = new ArrayList<>(cast);
        s.listedIn = new ArrayList<>(listedIn);
        return s;
    }

    public void print() {
        System.out.print("=> " + id + " ## " + title + " ## " + type + " ## " + director + " ## [");
        System.out.print(String.join(", ", cast));
        System.out.print("] ## " + country + " ## " + dateAdded + " ## " + releaseYear + " ## " + rating + " ## " + duration + " ## [");
        System.out.print(String.join(", ", listedIn));
        System.out.println("] ##");
    }
}

class Node {
    Show show;
    Node prev, next;

    Node(Show s) {
        this.show = s;
    }
}

public class Q10T {
    static final int MAX_SHOWS = 1400;
    static List<Show> shows = new ArrayList<>();
    static int comparisons = 0;

    public static void main(String[] args) throws Exception {
        readCSV("/tmp/disneyplus.csv");

        Scanner sc = new Scanner(System.in);
        String line;
        Node head = null, tail = null;

        while (!(line = sc.nextLine()).equals("FIM")) {
            for (Show s : shows) {
                if (s.id.equals(line)) {
                    Show clone = s.clone();
                    Node node = new Node(clone);
                    if (head == null) {
                        head = tail = node;
                    } else {
                        tail.next = node;
                        node.prev = tail;
                        tail = node;
                    }
                    break;
                }
            }
        }

        long start = System.currentTimeMillis();
        quickSort(head, tail);
        long end = System.currentTimeMillis();

        for (Node cur = head; cur != null; cur = cur.next) {
            cur.show.print();
        }

        FileWriter fw = new FileWriter("843275_quicksort2.txt");
        fw.write("843275\t" + (end - start) + ".000\t" + comparisons + "\n");
        fw.close();

        sc.close();
    }

    public static void readCSV(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine(); // header

        while ((line = br.readLine()) != null && shows.size() < MAX_SHOWS) {
            String[] fields = splitCSVLine(line);

            Show s = new Show();
            s.id = get(fields, 0);
            s.type = get(fields, 1);
            s.title = get(fields, 2);
            s.director = get(fields, 3);
            s.country = get(fields, 5);
            s.dateAdded = get(fields, 6);
            s.releaseYear = parseInt(get(fields, 7), -1);
            s.rating = get(fields, 8);
            s.duration = get(fields, 9);

            s.cast = parseList(get(fields, 4));
            s.listedIn = parseList(get(fields, 10));

            shows.add(s);
        }

        br.close();
    }

    static String get(String[] arr, int i) {
        return i < arr.length && arr[i] != null && !arr[i].isEmpty() ? arr[i].trim() : "NaN";
    }

    static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    static List<String> parseList(String str) {
        List<String> list = new ArrayList<>();
        if (str.equals("NaN")) {
            list.add("NaN");
        } else {
            for (String part : str.split(",")) {
                list.add(part.trim());
            }
            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        }
        return list;
    }

    static String[] splitCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                parts.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        parts.add(sb.toString());
        return parts.toArray(new String[0]);
    }

    static int dateToInt(String date) {
        if (date.equals("NaN")) return 0;
        try {
            String[] parts = date.split(" ");
            String month = parts[0];
            int day = Integer.parseInt(parts[1].replace(",", ""));
            int year = Integer.parseInt(parts[2]);
            int monthNum = monthToNumber(month);
            return year * 10000 + monthNum * 100 + day;
        } catch (Exception e) {
            return 0;
        }
    }

    static int monthToNumber(String month) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(month)) return i + 1;
        }
        return 0;
    }

    static int compare(Show a, Show b) {
        comparisons++;
        int dateA = dateToInt(a.dateAdded);
        int dateB = dateToInt(b.dateAdded);
        if (dateA != dateB) return Integer.compare(dateA, dateB);
        return a.title.compareToIgnoreCase(b.title);
    }

    static void quickSort(Node low, Node high) {
        if (high != null && low != high && low != high.next) {
            Node p = partition(low, high);
            quickSort(low, p.prev);
            quickSort(p.next, high);
        }
    }

    static Node partition(Node low, Node high) {
        Show pivot = high.show;
        Node i = low.prev;

        for (Node j = low; j != high; j = j.next) {
            if (compare(j.show, pivot) <= 0) {
                i = (i == null) ? low : i.next;
                Show tmp = i.show;
                i.show = j.show;
                j.show = tmp;
            }
        }

        i = (i == null) ? low : i.next;
        Show tmp = i.show;
        i.show = high.show;
        high.show = tmp;
        return i;
    }
}
