import java.util.Scanner;

public class CifraDeCesarRec {
    public static String cifrar(String palavra){
        return cifrar(palavra, "", 0);
    }
    public static String cifrar(String palavra, String resultado, int i){
    
        // Caso o i seja maior que o tamanho da palavra é porquê toda a string foi percorrida
        if(i < palavra.length()){
            
            // If para verificar se o caracter está dentro da tabela ASCII padrão
            if (palavra.charAt(i) >= 0 && palavra.charAt(i) <= 127){
                char caractere = palavra.charAt(i);
                resultado += (char) (caractere + 3);
                
                resultado = cifrar(palavra, resultado, i + 1);
            }
            // Caso o caracter não esteja dentro da tabela ASCII padrão, ele é apenas somado ao resto da palavra
            else{
                char caractere = palavra.charAt(i);
                resultado += (char) (caractere);
                
                resultado = cifrar(palavra, resultado, i + 1);
            } 
        }
        return resultado;
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            System.out.println(cifrar(linha));
            linha = sc.nextLine();
        }

        sc.close();
    }
}
