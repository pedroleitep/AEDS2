// ArvoreArvoreDisney.java

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

    private String campoOuNaN(String[] campos, int i) {
        return (i >= campos.length || campos[i].isEmpty()) ? "NaN" : campos[i];
    }

    private String[] dividirCampo(String campo) {
        if (campo.equals("NaN")) return new String[]{"NaN"};
        return campo.split(", ");
    }

    private String[] ordenar(String[] array) {
        Arrays.sort(array, String::compareToIgnoreCase);
        return array;
    }

    public String[] separarCampos(String linha) {
        String[] atributos = new String[11];
        StringBuilder campo = new StringBuilder();
        boolean aspas = false;
        int col = 0;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '"') aspas = !aspas;
            else if (c == ',' && !aspas) {
                if (col < 11) atributos[col++] = campo.toString().trim();
                campo.setLength(0);
            } else campo.append(c);
        }

        if (col < 11) atributos[col] = campo.toString().trim();

        return atributos;
    }
}

class NoExterno {
    Show elemento;
    NoExterno esq, dir;

    public NoExterno(Show elemento) {
        this.elemento = elemento;
        esq = dir = null;
    }
}

class NoInterno {
    int chave;
    NoInterno esq, dir;
    NoExterno raizSubArvore;

    public NoInterno(int chave) {
        this.chave = chave;
        esq = dir = null;
        raizSubArvore = null;
    }
}

class ArvoreArvore {
    private NoInterno raiz;
    private int comparacoes = 0;

    public ArvoreArvore() {
        int[] ordem = {7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14};
        for (int chave : ordem) raiz = inserirNoInterno(chave, raiz);
    }

    private NoInterno inserirNoInterno(int chave, NoInterno no) {
        if (no == null) return new NoInterno(chave);
        if (chave < no.chave) no.esq = inserirNoInterno(chave, no.esq);
        else if (chave > no.chave) no.dir = inserirNoInterno(chave, no.dir);
        return no;
    }

    public void inserir(Show s) {
        int chave = s.getReleaseYear() % 15;
        raiz = inserirShow(s, raiz, chave);
    }

    private NoInterno inserirShow(Show s, NoInterno no, int chave) {
        if (no == null) return null;
        if (chave < no.chave) no.esq = inserirShow(s, no.esq, chave);
        else if (chave > no.chave) no.dir = inserirShow(s, no.dir, chave);
        else no.raizSubArvore = inserirSubArvore(s, no.raizSubArvore);
        return no;
    }

    private NoExterno inserirSubArvore(Show s, NoExterno no) {
        if (no == null) return new NoExterno(s);
        comparacoes++;
        int cmp = s.getTitle().compareToIgnoreCase(no.elemento.getTitle());
        if (cmp < 0) no.esq = inserirSubArvore(s, no.esq);
        else if (cmp > 0) no.dir = inserirSubArvore(s, no.dir);
        return no;
    }

    public boolean pesquisar(String title) {
        System.out.print("raiz ");
        return pesquisar(title, raiz);
    }

    private boolean pesquisar(String title, NoInterno no) {
        if (no == null) return false;
        boolean achou = pesquisarSub(title, no.raizSubArvore);
        System.out.print(" ESQ ");
        if (achou) return true;
        achou = pesquisar(title, no.esq);
        if (achou) return true;
        System.out.print(" DIR ");
        return pesquisar(title, no.dir);
    }

    private boolean pesquisarSub(String title, NoExterno no) {
        if (no == null) return false;
        comparacoes++;
        int cmp = title.compareToIgnoreCase(no.elemento.getTitle());
        if (cmp == 0) return true;
        else if (cmp < 0) {
            System.out.print("esq ");
            return pesquisarSub(title, no.esq);
        } else {
            System.out.print("dir ");
            return pesquisarSub(title, no.dir);
        }
    }

    public int getComparacoes() { return comparacoes; }
}

public class Q2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String arquivo = "/tmp/disneyplus.csv";
        Show[] lista = new Show[1400];
        int total = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null && total < lista.length) {
                Show s = new Show();
                s.ler(s.separarCampos(linha));
                lista[total++] = s;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        ArvoreArvore arvore = new ArvoreArvore();

        String entrada;
        do {
            entrada = sc.nextLine();
            if (!entrada.equals("FIM")) {
                for (int i = 0; i < total; i++) {
                    if (lista[i].getId().equals(entrada)) {
                        arvore.inserir(lista[i].clone());
                        break;
                    }
                }
            }
        } while (!entrada.equals("FIM"));

        long inicio = System.nanoTime();

        String titulo = sc.nextLine();
        while (!titulo.equals("FIM")) {
            boolean achou = arvore.pesquisar(titulo);
            System.out.println(achou ? " SIM" : " NAO");
            titulo = sc.nextLine();
        }

        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e6;

        try {
            PrintWriter log = new PrintWriter(new FileWriter("843275_arvoreArvore.txt"));
            log.printf("843275\t%.3f\t%d\n", tempo, arvore.getComparacoes());
            log.close();
        } catch (Exception e) {
            System.out.println("Erro ao escrever o log: " + e.getMessage());
        }

        sc.close();
    }
}
