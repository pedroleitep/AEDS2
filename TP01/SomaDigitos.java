import java.util.*;

public class SomaDigitos {
    public static int somaDigitos(String str, int i) {
        if (i == str.length()) {
            return 0;
        }
        //pega cada caracter na posição i, subtrai de '0' para conversão de inteiro, depois soma cada caracter com o próximo
        return (str.charAt(i) - '0') + somaDigitos(str, i + 1);
    }
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String linha = sc.nextLine();

        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            int soma = somaDigitos(linha, 0);
            System.out.println(soma);
            linha = sc.nextLine();
        }

        sc.close();
    }
}
