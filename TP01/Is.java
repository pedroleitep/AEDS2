// Is - Crie um método iterativo que recebe uma string e retorna true se a mesma é composta
// somente por vogais. Crie outro método iterativo que recebe uma string e retorna true se a
// mesma é composta somente por consoantes. Crie um terceiro método iterativo que recebe uma
// string e retorna true se a mesma corresponde a um número inteiro. Crie um quarto método
// iterativo que recebe uma string e retorna true se a mesma corresponde a um número real. Na
// saída padrão, para cada linha de entrada, escreva outra de saída da seguinte forma X1 X2 X3
// X4 onde cada Xi é um booleano indicando se a é entrada é: composta somente por vogais (X1);
// composta somente somente por consoantes (X2); um número inteiro (X3); um número real (X4).
// Se Xi for verdadeiro, seu valor será SIM, caso contrário, NAO. 

import java.util.*;

public class Is {

    private static boolean soVogais (String palavra) {
        int tam = palavra.length() - 1;
        boolean vogais = true;

        for (int i = 0 ; i <= tam ; i++) {
            if(!(palavra.charAt(i) == 'a' || palavra.charAt(i) == 'A' || palavra.charAt(i) == 'e' || palavra.charAt(i) 
            == 'E' || palavra.charAt(i) == 'i' || palavra.charAt(i) == 'I' || palavra.charAt(i) == 'o' || 
            palavra.charAt(i) == 'O' || palavra.charAt(i) == 'u' || palavra.charAt(i) == 'U')) {
                vogais = false;
                i = tam;
            }
        }

        return vogais;
    } //funcao que retorna true or false para a string que possui apenas vogais

    private static boolean soConsoantes (String palavra) {
        int tam = palavra.length() - 1;
        boolean consoantes = true;

        for (int i = 0 ; i <= tam ; i++) {
            if((palavra.charAt(i) == 'a' || palavra.charAt(i) == 'A' || palavra.charAt(i) == 'e' || palavra.charAt(i) == 'E' || palavra.charAt(i) == 'i' || palavra.charAt(i) == 'I' || palavra.charAt(i) == 'o' || palavra.charAt(i) == 'O' || palavra.charAt(i) == 'u' || palavra.charAt(i) == 'U') || (palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9' ) || (palavra.charAt(i) == ',' || palavra.charAt(i) == '.')) {
                consoantes = false;
                i = tam;
            }
        }

        return consoantes;
    } //funcao que retorna true or false para a string que possui apenas consoantes

    private static boolean inteiros (String palavra){
        int tam = palavra.length() - 1;
        boolean inteiro = true;

        for(int i = 0; i <= tam; i++){
            if(!(palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9')){
                inteiro = false;
                i = tam;
            }
        }
        return inteiro;
    }

    private static boolean reais (String palavra){
        int contarPontos = 0;
        int tam = palavra.length() - 1;
        boolean real = true;

        for(int i = 0; i <= tam; i++){
            if((palavra.charAt(i) == ',' || palavra.charAt(i) == '.')){
                contarPontos++;
                if(contarPontos > 1){
                    real = false;
                    i = tam;
                }
            }
            else if(!(palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9')){
                real = false;
                i = tam;
            }
        }
        return real;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String linha = sc.nextLine();
        String x1, x2, x3, x4;

        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            if(soVogais(linha)) x1 = "SIM";
            else x1 = "NAO";
            if(soConsoantes(linha)) x2 = "SIM";
            else x2 = "NAO";
            if(inteiros(linha)) x3 = "SIM";
            else x3 = "NAO";
            if(reais(linha)) x4 = "SIM";
            else x4 = "NAO";

            System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);

            linha = sc.nextLine();
        }
        sc.close();
    }
}
