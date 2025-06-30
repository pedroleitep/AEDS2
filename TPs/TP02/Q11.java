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

public class Q11 {
    public static void main(String[] args) {
        String arquivo = "/tmp/disneyplus.csv";
        Scanner sc = new Scanner(System.in);
        Show[] lista = new Show[1400];
        int total = 0;

        // Leitura do CSV
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            String linha;
            br.readLine(); // cabeçalho
            while ((linha = br.readLine()) != null && total < lista.length) {
                Show s = new Show();
                String[] campos = s.separarCampos(linha);
                s.ler(campos);
                lista[total++] = s;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Adição dos Shows selecionados no novo array
        Show[] selecionados = new Show[1000];
        int qtdSelecionados = 0;

        String entradaID;
        do {
            entradaID = sc.nextLine();
            if (!entradaID.equals("FIM")) {
                for (int i = 0; i < total; i++) {
                    if (lista[i].getId().equals(entradaID)) {
                        if (qtdSelecionados < selecionados.length) {
                            selecionados[qtdSelecionados++] = lista[i].clone();
                        }
                    }
                }
            }
        } while (!entradaID.equals("FIM"));
        
        // Medição de tempo e contagem de comparações
        long tempoInicio = System.nanoTime();
        int comparacoes = 0;

        // Counting Sort por releaseYear com desempate por title (usando Counting Sort)

        int minAno = Integer.MAX_VALUE, maxAno = Integer.MIN_VALUE;

        // Encontra menor e maior releaseYear
        for (int i = 0; i < qtdSelecionados; i++) {
            int ano = selecionados[i].getReleaseYear();
            if (ano < minAno) minAno = ano;
            if (ano > maxAno) maxAno = ano;
        }

        // Cria e preenche contagem dos anos
        int rangeAno = maxAno - minAno + 1;
        int[] countAno = new int[rangeAno];
        for (int i = 0; i < qtdSelecionados; i++) {
            countAno[selecionados[i].getReleaseYear() - minAno]++;
            comparacoes++;
        }

        // Acumula contagem dos anos
        for (int i = 1; i < rangeAno; i++) {
            countAno[i] += countAno[i - 1];
        }

        // Ordena por releaseYear (Counting Sort)
        Show[] ordenadoPorAno = new Show[qtdSelecionados];
        for (int i = qtdSelecionados - 1; i >= 0; i--) {
            int pos = selecionados[i].getReleaseYear() - minAno;
            int idx = --countAno[pos];
            ordenadoPorAno[idx] = selecionados[i];
            comparacoes++;
        }

        // Agora fazemos Counting Sort por title, dentro de cada grupo de releaseYear
        int inicio = 0;
        while (inicio < qtdSelecionados) {
            int fim = inicio + 1;
            while (fim < qtdSelecionados && 
                ordenadoPorAno[fim].getReleaseYear() == ordenadoPorAno[inicio].getReleaseYear()) {
                fim++;
            }

        // Insertion Sort por título dentro do grupo de mesmo releaseYear
        for (int i = inicio + 1; i < fim; i++) {
            Show tmp = ordenadoPorAno[i];
            int j = i - 1;
            while (j >= inicio && ordenadoPorAno[j].getTitle().compareToIgnoreCase(tmp.getTitle()) > 0) {
                ordenadoPorAno[j + 1] = ordenadoPorAno[j];
                j--;
                comparacoes++;
            }
            ordenadoPorAno[j + 1] = tmp;
            comparacoes++;
        }

            inicio = fim;
        }

        // Copia final para o vetor selecionados
        for (int i = 0; i < qtdSelecionados; i++) {
            selecionados[i] = ordenadoPorAno[i];
        }

        

        long fim = System.nanoTime();
        double tempo = (fim - tempoInicio) / 1e6; // tempo em milissegundos

        for (int i = 0; i < qtdSelecionados; i++) {
            selecionados[i].imprimir();
        }

        try {
            PrintWriter log = new PrintWriter(new FileWriter("843275_countingsort.txt"));
            log.printf("843275\t%.3f\t%d\n", tempo, comparacoes);
            log.close();
        } catch (Exception e) {
            System.out.println("Erro ao escrever o log: " + e.getMessage());
        }

        sc.close();
    }

}

