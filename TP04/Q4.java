import java.io.*;
import java.util.*;

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

class NoAN {
    public Show elemento;
    public boolean cor;
    public NoAN esq, dir;

    public NoAN(Show elemento) {
        this(elemento, false);
    }

    public NoAN(Show elemento, boolean cor) {
        this.elemento = elemento;
        this.cor = cor;
        this.esq = this.dir = null;
    }
}

class ArvoreAlvinegra {
    private NoAN raiz;
    private int comparacoes = 0;

    public ArvoreAlvinegra() {
        raiz = null;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    // MÉTODO DE PESQUISA COM IMPRESSÃO DO CAMINHO
    public boolean pesquisar(String title) {
        System.out.print("=>raiz  ");
        boolean achou = pesquisar(title, raiz);
        System.out.println(achou ? "SIM" : "NAO");
        return achou;
    }

    private boolean pesquisar(String title, NoAN i) {
        comparacoes++;
        if (i == null) {
            return false;
        }

        int cmp = title.compareToIgnoreCase(i.elemento.getTitle());

        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            System.out.print("esq ");
            return pesquisar(title, i.esq);
        } else {
            System.out.print("dir ");
            return pesquisar(title, i.dir);
        }
    }

    public void inserir(Show s) throws Exception {
        if (raiz == null) {
            raiz = new NoAN(s);
        } else if (raiz.esq == null && raiz.dir == null) {
            if (s.getTitle().compareTo(raiz.elemento.getTitle()) < 0) {
                raiz.esq = new NoAN(s);
            } else {
                raiz.dir = new NoAN(s);
            }
        } else if (raiz.esq == null) {
            if (s.getTitle().compareTo(raiz.elemento.getTitle()) < 0) {
                raiz.esq = new NoAN(s);
            } else if (s.getTitle().compareTo(raiz.dir.elemento.getTitle()) < 0) {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = s;
            } else {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = raiz.dir.elemento;
                raiz.dir.elemento = s;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else if (raiz.dir == null) {
            if (s.getTitle().compareTo(raiz.elemento.getTitle()) > 0) {
                raiz.dir = new NoAN(s);
            } else if (s.getTitle().compareTo(raiz.esq.elemento.getTitle()) > 0) {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = s;
            } else {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = raiz.esq.elemento;
                raiz.esq.elemento = s;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else {
            inserir(s, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    private void inserir(Show s, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception {
        if (i == null) {
            if (s.getTitle().compareTo(pai.elemento.getTitle()) < 0) {
                i = pai.esq = new NoAN(s, true);
            } else {
                i = pai.dir = new NoAN(s, true);
            }
            if (pai.cor == true) {
                balancear(bisavo, avo, pai, i);
            }
        } else {
            if (i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true) {
                i.cor = true;
                i.esq.cor = i.dir.cor = false;
                if (i == raiz) {
                    i.cor = false;
                } else if (pai.cor == true) {
                    balancear(bisavo, avo, pai, i);
                }
            }

            if (s.getTitle().compareTo(i.elemento.getTitle()) < 0) {
                inserir(s, avo, pai, i, i.esq);
            } else if (s.getTitle().compareTo(i.elemento.getTitle()) > 0) {
                inserir(s, avo, pai, i, i.dir);
            } else {
                throw new Exception("Erro inserir (elemento repetido)!");
            }
        }
    }

    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {
        if (pai.cor == true) {
            if (pai.elemento.getTitle().compareTo(avo.elemento.getTitle()) > 0) {
                if (i.elemento.getTitle().compareTo(pai.elemento.getTitle()) > 0) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else {
                if (i.elemento.getTitle().compareTo(pai.elemento.getTitle()) < 0) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }

            if (bisavo == null) {
                raiz = avo;
            } else if (avo.elemento.getTitle().compareTo(bisavo.elemento.getTitle()) < 0) {
                bisavo.esq = avo;
            } else {
                bisavo.dir = avo;
            }

            avo.cor = false;
            avo.esq.cor = avo.dir.cor = true;
        }
    }

    private NoAN rotacaoDir(NoAN no) {
        NoAN noEsq = no.esq;
        NoAN noEsqDir = noEsq.dir;

        noEsq.dir = no;
        no.esq = noEsqDir;

        return noEsq;
    }

    private NoAN rotacaoEsq(NoAN no) {
        NoAN noDir = no.dir;
        NoAN noDirEsq = noDir.esq;

        noDir.esq = no;
        no.dir = noDirEsq;
        return noDir;
    }

    private NoAN rotacaoDirEsq(NoAN no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private NoAN rotacaoEsqDir(NoAN no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }
}

public class Q4 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String arquivo = "/tmp/disneyplus.csv";
        Show[] lista = new Show[1400];
        int total = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null && total < lista.length) {
                Show s = new Show();
                String[] campos = s.separarCampos(linha);
                s.ler(campos);
                lista[total++] = s;
            }
        }

        ArvoreAlvinegra arvore = new ArvoreAlvinegra();

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
            arvore.pesquisar(titulo);
            titulo = sc.nextLine();
        }

        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e6;

        try (PrintWriter log = new PrintWriter(new FileWriter("843275_avinegra.txt"))) {
            log.printf("843275\t%.3f\t%d\n", tempo, arvore.getComparacoes());
        }

        sc.close();
    }
}