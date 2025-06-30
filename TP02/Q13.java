import java.io.*;
import java.util.*;


class Show {
    private String id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private String date;
    private int year;
    private String rating;
    private String duration;
    private String[] listed;
    
    public String getId() { return id; } 
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public String[] getCast() { return cast; }
    public String getCountry() { return country; }
    public String getDate() { return date; }
    public int getYear() { return year; }
    public String getRating() { return rating; }
    public String getDuration() { return duration; }
    public String[] getListed() { return listed; }
    
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
    public void setDirector(String director) { this.director = director; }
    public void setCast(String[] cast) { this.cast = cast; }
    public void setCountry(String country) { this.country = country; }
    public void setDate(String date) { this.date = date; }
    public void setYear(int year) { this.year = year; }
    public void setRating(String rating) { this.rating = rating; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setListed(String[] listed) { this.listed = listed; }
    
    public Show(String id, String type, String title, String director, String[] cast, String country, String date, int year, String rating, String duration, String[] listed) {
        setId(id);
        setType(type);
        setTitle(title);
        setDirector(director);
        setCast(cast);
        setCountry(country);
        setDate(date);
        setYear(year);
        setRating(rating);
        setDuration(duration);
        setListed(listed);
    }
    
    public Show() {}
    
    
    public static String[] splitCSV(String line) {
        List<String> campos = new ArrayList<>();
        boolean entreAspas = false;
        StringBuilder campo = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                entreAspas = !entreAspas;
            } else if (c == ',' && !entreAspas) {
                campos.add(campo.toString().trim());
                campo.setLength(0); // resetar stringbuilder
            } else {
                campo.append(c);
            }
        }
        campos.add(campo.toString().trim());
        
        return campos.toArray(new String[0]);
    }
    
    public void ler(String csv) {
        String[] campos = splitCSV(csv);
        
        if (campos.length >= 11) {
            setId(campos[0].isEmpty() ? "NaN" : campos[0]);
            setType(campos[1].isEmpty() ? "NaN" : campos[1]);
            setTitle(campos[2].isEmpty() ? "NaN" : campos[2]);
            setDirector(campos[3].replace("\"", "").isEmpty() ? "NaN" : campos[3].replace("\"", ""));
            
            String castStr = campos[4].replace("\"", "");
            String[] castArray = castStr.isEmpty() ? new String[]{"NaN"} : castStr.split(", ");
            if (!castStr.isEmpty()) Arrays.sort(castArray);
            setCast(castArray);
            
            setCountry(campos[5].replace("\"", "").isEmpty() ? "NaN" : campos[5].replace("\"", ""));
            
            String dateStr = campos[6].replace("\"", "").trim();
            setDate(dateStr.isEmpty() ? "NaN" : dateStr);
            
            setYear(campos[7].isEmpty() ? 0 : Integer.parseInt(campos[7]));
            setRating(campos[8].isEmpty() ? "NaN" : campos[8]);
            setDuration(campos[9].isEmpty() ? "NaN" : campos[9]);
            
            String[] listedArray = campos[10].isEmpty() ? new String[]{"NaN"} : campos[10].split(", ");
            setListed(listedArray);
        }
    }
    
    public void imprimir() {
        System.out.printf("=> %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ##\n",
            id, title, type, director, Arrays.toString(cast), country,
            date, // Imprime a string diretamente
            year == 0 ? "NaN" : String.valueOf(year),
            rating, duration, Arrays.toString(listed));
        }

        public Show clone() {
        Show clone = new Show();
        clone.id = this.id != null ? new String(this.id) : null;
        clone.type = this.type != null ? new String(this.type) : null;
        clone.title = this.title != null ? new String(this.title) : null;
        clone.director = this.director != null ? new String(this.director) : null;
        clone.country = this.country != null ? new String(this.country) : null;
        clone.date = this.date != null ? new String(this.date) : null;
        clone.year = this.year;
        clone.rating = this.rating != null ? new String(this.rating) : null;
        clone.duration = this.duration != null ? new String(this.duration) : null;
        
        if (this.cast != null) {
            clone.cast = new String[this.cast.length];
            for (int i = 0; i < this.cast.length; i++) {
                clone.cast[i] = this.cast[i] != null ? new String(this.cast[i]) : null;
            }
        } else {
            clone.cast = null;
        }
        
        if (this.listed != null) {
            clone.listed = new String[this.listed.length];
            for (int i = 0; i < this.listed.length; i++) {
                clone.listed[i] = this.listed[i] != null ? new String(this.listed[i]) : null;
            }
        } else {
            clone.listed = null;
        }

        return clone;
    }
}

class Q13T2 {
    public static void intercalar(Show[] lista, int esq, int meio, int dir) {
        int nEsq = meio - esq + 1;
        int nDir = dir - meio;
    
        Show[] listaEsq = new Show[nEsq];
        Show[] listaDir = new Show[nDir];
    
        for (int i = 0; i < nEsq; i++) {
            listaEsq[i] = lista[esq + i];
        }
        for (int j = 0; j < nDir; j++) {
            listaDir[j] = lista[meio + 1 + j];
        }
    
        int iEsq = 0, iDir = 0, k = esq;
    
        while (iEsq < nEsq && iDir < nDir) {
            int cmpDuration = listaEsq[iEsq].getDuration().compareTo(listaDir[iDir].getDuration());
            if (cmpDuration < 0 || (cmpDuration == 0 && listaEsq[iEsq].getTitle().toLowerCase().compareTo(listaDir[iDir].getTitle().toLowerCase()) < 0)) {
                lista[k++] = listaEsq[iEsq++];
            } else {
                lista[k++] = listaDir[iDir++];
            }
        }
    
        while (iEsq < nEsq) {
            lista[k++] = listaEsq[iEsq++];
        }
    
        while (iDir < nDir) {
            lista[k++] = listaDir[iDir++];
        }
    }

    public static void sort(Show[] lista, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            sort(lista, esq, meio);
            sort(lista, meio+1, dir);
            intercalar(lista, esq, meio, dir);
        }
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        String path = "/tmp/disneyplus.csv";

        Show[] lista = new Show[1369];
        int i = 0;

        String lerId = sc.nextLine();
        while (!lerId.equals("FIM"))  {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line = br.readLine(); // ignorar primeira linha
                // br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] campos = Show.splitCSV(line);

                    if (campos.length > 0 && campos[0].equals(lerId)) {
                        Show show = new Show();
                        show.ler(line);
                        lista[i++] = show;
                        break;
                    }

                }
            } catch (Exception e) {}
            
            lerId = sc.nextLine();
        }

        sort(lista, 0, i-1);
        for (int j = 0; j < i; j++) {
            lista[j].imprimir();
        }

        sc.close();
    }
}