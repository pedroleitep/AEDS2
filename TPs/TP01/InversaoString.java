// 7. Invers˜ao de String - Crie um m´etodo iterativo que recebe uma string como parˆametro e
// retorna a string invertida. Na sa´ıda padr˜ao, para cada linha de entrada, escreva uma linha de
// sa´ıda com a string invertida. Por exemplo, se a entrada for “abcde”, a sa´ıda deve ser “edcba”.

import java.util.*;

public class InversaoString{

    private static String inverteString (String palavra){
        int tam = palavra.length() - 1;
        String invertida = "";

        //for começando do final da palavra, colocando os caracteres em ordem invertida
        for(int i = tam; i >= 0; i--){
            invertida += palavra.charAt(i);
        }
        return invertida;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String palavra = sc.nextLine();

        while(!(palavra.length() == 3 && palavra.charAt(0) == 'F' && palavra.charAt(1) == 'I' && palavra.charAt(2) == 'M')) {
            System.out.println(inverteString(palavra));
            palavra = sc.nextLine();
        }
        sc.close();
    }
}