import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;


    class Show {
        private String id, type, title, director, country, dateAdded, rating, duration;
        private int releaseYear;
        private String[] cast, listedIn;

        // Construtor padrão
        public Show() {
            this("NaN", "NaN", "NaN", "NaN", new String[]{"NaN"}, "NaN", "NaN", -1, "NaN", "NaN", new String[]{"NaN"});
        }

        // Construtor completo
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

        // Getters e Setters
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

        // clone()
        public Show clone() {
            return new Show(getId(), getType(), getTitle(), getDirector(), getCast().clone(), getCountry(),
                    getDateAdded(), getReleaseYear(), getRating(), getDuration(), getListedIn().clone());
        }

        // ler()
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

        // imprimir()
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

        // Utilitários
        private String campoOuNaN(String[] campos, int i) {
            return (i >= campos.length || campos[i].isEmpty()) ? "NaN" : campos[i];
        }

        private String[] dividirCampo(String campo) {
            if (campo.equals("NaN")) return new String[]{"NaN"};
            return campo.split(", ");
        }

        // Ordenação
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

        // Comparação de strings 
        private boolean maiorQue(String a, String b) {
            int minLen = a.length() < b.length() ? a.length() : b.length();
            for (int i = 0; i < minLen; i++) {
                char c1 = a.charAt(i);
                char c2 = b.charAt(i);
                if (c1 < c2) return false;
                if (c1 > c2) return true;
            }
            return a.length() > b.length();
        }

        // Separar os campos do CSV 
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




public class Q3 {
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
                lista[total] = s;
                total++;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        

        Show[] selecionados = new Show[1000];
        int qtdSelecionados = 0;
    
        // Entrada para selecionar IDs e armazenar no novo vetor
        //System.out.println("Digite os IDs dos shows para armazenar (digite FIM para encerrar):");
        String entradaID;
        do {
            entradaID = sc.nextLine();
            if (!entradaID.equals("FIM")) {
                boolean achou = false;
                for (int i = 0; i < total; i++) {
                    if (lista[i].getId().equals(entradaID)) {
                        if (qtdSelecionados < selecionados.length) {
                            selecionados[qtdSelecionados++] = lista[i].clone();
                            //System.out.println("Show " + entradaID + " adicionado à lista de selecionados.");
                            achou = true;
                        } else {
                            //System.out.println("Vetor de selecionados cheio.");
                        }
                    }
                }
                if (!achou) {
                    //System.out.println("Show ID " + entradaID + " não encontrado.");
                }
            }
        } while (!entradaID.equals("FIM"));

        String titulo;
        do {
            titulo = sc.nextLine();
            if (!titulo.equals("FIM")) {
                boolean achou = false;
                for (int i = 0; i < qtdSelecionados; i++) {
                    if (selecionados[i].getTitle().equals(titulo)) {
                        System.out.println("SIM");
                        achou = true;
                    }
                }
                if (!achou) {
                    System.out.println("NAO");
                }
            }
        } while (!titulo.equals("FIM"));

        
        sc.close();
    }
    
}

