import java.util.Scanner;

public class PalindromoRec {
    
    public static boolean ehPalindromo(String str){
        return ehPalindromo(str, 0, str.length() - 1);
    }
    public static boolean ehPalindromo(String str, int esquerda, int direita) {
        boolean palindromo;
        if (esquerda >= direita) {
            palindromo = true; // Caso base: se os índices se cruzam ou são iguais, é um palíndromo
        }
        else if (str.charAt(esquerda) != str.charAt(direita)) {
            palindromo = false; // Se os caracteres das extremidades não forem iguais, não é palíndromo
        }
        else {
            palindromo = ehPalindromo(str, esquerda + 1, direita - 1); // Chamada recursiva para os próximos caracteres
        }
        return palindromo;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String linha = sc.nextLine();
        
        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            if(ehPalindromo(linha)){
                System.out.println("SIM"); 
            }else{
                System.out.println("NAO");
            }
            
            linha = sc.nextLine();
        }
        
        sc.close();
    }
}
