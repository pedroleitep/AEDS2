import java.util.*;
import java.io.*;

class Show {
    private String id, type, title, director, country, dateAdded, rating, duration;
    private int releaseYear;
    private String[] cast, listedIn;

    public Show() {
        this("NaN", "NaN", "NaN", "NaN", new String[]{"NaN"}, "NaN", "NaN", -1, "NaN", "NaN", new String[]{"NaN"});
    }

    public Show(String id, String type, String title, String director, String[] cast, String country,
                String dateAdded, int releaseYear, String rating, String duration, String[] listedIn) {
        setId(id);
        setType(type);
        setTitle(title);
        setDirector(director);
        setCast(cast);
        setCountry(country);
        setDateAdded(dateAdded);
        setReleaseYear(releaseYear);
        setRating(rating);
        setDuration(duration);
        setListedIn(listedIn);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String[] getCast() { return cast; }
    public void setCast(String[] cast) { this.cast = ordenar(cast); }

    public String[] getListedIn() { return listedIn; }
    public void setListedIn(String[] listedIn) { this.listedIn = ordenar(listedIn); }

    public Show clone() {
        return new Show(getId(), getType(), getTitle(), getDirector(), getCast().clone(), getCountry(),
                getDateAdded(), getReleaseYear(), getRating(), getDuration(), getListedIn().clone());
    }

    public void ler(String[] campos) {
        setId(campoOuNaN(campos, 0));
        setType(campoOuNaN(campos, 1));
        setTitle(campoOuNaN(campos, 2));
        setDirector(campoOuNaN(campos, 3));
        setCast(dividirCampo(campoOuNaN(campos, 4)));
        setCountry(campoOuNaN(campos, 5));
        setDateAdded(campoOuNaN(campos, 6));
        setReleaseYear(campoOuNaN(campos, 7).equals("NaN") ? -1 : Integer.parseInt(campos[7]));
        setRating(campoOuNaN(campos, 8));
        setDuration(campoOuNaN(campos, 9));
        setListedIn(dividirCampo(campoOuNaN(campos, 10)));
    }

    public void imprimir() {
        System.out.print("=> " + getId() + " ## " + getTitle() + " ## " + getType() + " ## " + getDirector() + " ## [");
        String[] elenco = getCast();
        for (int i = 0; i < elenco.length; i++) {
            System.out.print(elenco[i]);
            if (i < elenco.length - 1) System.out.print(", ");
        }
        System.out.print("] ## " + getCountry() + " ## " + getDateAdded() + " ## " + getReleaseYear());
        System.out.print(" ## " + getRating() + " ## " + getDuration() + " ## [");

        String[] categorias = getListedIn();
        for (int i = 0; i < categorias.length; i++) {
            System.out.print(categorias[i]);
            if (i < categorias.length - 1) System.out.print(", ");
        }
        System.out.print("]");
        System.out.println(" ##");
    }

    private String campoOuNaN(String[] campos, int i) {
        return (i >= campos.length || campos[i].isEmpty()) ? "NaN" : campos[i];
    }

    private String[] dividirCampo(String campo) {
        if (campo.equals("NaN")) return new String[]{"NaN"};
        return campo.split(", ");
    }

    private String[] ordenar(String[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (maiorQue(array[i], array[j])) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }

    private boolean maiorQue(String a, String b) {
        int minLen = Math.min(a.length(), b.length());
        for (int i = 0; i < minLen; i++) {
            char c1 = a.charAt(i);
            char c2 = b.charAt(i);
            if (c1 < c2) return false;
            if (c1 > c2) return true;
        }
        return a.length() > b.length();
    }

    public String[] separarCampos(String linha) {
        String[] atributos = new String[11];
        StringBuilder campo = new StringBuilder();
        boolean aspas = false;
        int col = 0;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '"') {
                aspas = !aspas;
            } else if (c == ',' && !aspas) {
                if (col < 11) {
                    atributos[col++] = campo.toString().trim();
                }
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }

        if (col < 11) {
            atributos[col] = campo.toString().trim();
        }

        return atributos;
    }
}

class HashRehash {
    private Show[] tabela;
    private final int m = 21;
    private final int NULO = -1;
    private int comparacoes = 0;

    public HashRehash() {
        tabela = new Show[m];
    }

    private int h(String titulo) {
        return asciiSum(titulo) % m;
    }

    private int reh(String titulo) {
        return (asciiSum(titulo) + 1) % m;
    }

    private int asciiSum(String str) {
        int soma = 0;
        for (char c : str.toCharArray()) soma += c;
        return soma;
    }

    public void inserir(Show show) {
        String titulo = show.getTitle();
        int pos = h(titulo);

        if (tabela[pos] == null) {
            tabela[pos] = show;
        } else if (tabela[pos].getTitle().equals(titulo)) {
            return; // já existe
        } else {
            int posReh = reh(titulo);
            if (tabela[posReh] == null) {
                tabela[posReh] = show;
            }
        }
    }

    public String pesquisar(String titulo) {
        int pos = h(titulo);
        comparacoes++;
        if (tabela[pos] != null && tabela[pos].getTitle().equals(titulo)) {
            return " (Posicao: " + pos + ") SIM";
        }

        int posReh = reh(titulo);
        comparacoes++;
        if (tabela[posReh] != null && tabela[posReh].getTitle().equals(titulo)) {
            return " (Posicao: " + posReh + ") SIM";
        }

        return " (Posicao: " + pos + ") NAO";
    }

    public int getComparacoes() {
        return comparacoes;
    }
}

public class Q6 {
    public static void main(String[] args) {
        String arquivo = "/tmp/disneyplus.csv";
        Scanner sc = new Scanner(System.in);
        Show[] lista = new Show[1400];
        int total = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null && total < lista.length) {
                Show s = new Show();
                s.ler(s.separarCampos(linha));
                lista[total++] = s;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler CSV: " + e.getMessage());
        }

        HashRehash hash = new HashRehash();

        // Inserção por ID
        while (true) {
            String entrada = sc.nextLine();
            if (entrada.equals("FIM")) break;
            for (int i = 0; i < total; i++) {
                if (lista[i].getId().equals(entrada)) {
                    hash.inserir(lista[i]);
                    break;
                }
            }
        }

        // Pesquisas por título
        List<String> resultados = new ArrayList<>();
        long inicio = System.nanoTime();
        while (true) {
            String entrada = sc.nextLine();
            if (entrada.equals("FIM")) break;
            resultados.add(hash.pesquisar(entrada));
        }
        long fim = System.nanoTime();

        // Impressão
        for (String res : resultados) {
            System.out.println(res);
        }

        // Log
        try {
            PrintWriter log = new PrintWriter("843275_hashRehash.txt");
            log.printf("843275\t%.3f\t%d\n", (fim - inicio) / 1e6, hash.getComparacoes());
            log.close();
        } catch (IOException e) {
            System.out.println("Erro ao gerar log.");
        }

        sc.close();
    }
}
