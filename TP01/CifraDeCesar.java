import java.util.Scanner;

public class CifraDeCesar {
    public static String cifrar(String texto) {
        String resultado = "";
        
        for (int i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);
            
            // Soma 3 na tabela ascii e converte para retornar um caracter
            resultado += (char) (caractere + 3);
        }
        
        return resultado;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            System.out.println(cifrar(linha));
            linha = sc.nextLine();
        }
        
        sc.close();
    }
}