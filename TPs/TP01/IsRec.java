import java.util.Scanner;

public class IsRec {

    private static boolean soVogais (String palavra){
        return soVogais (palavra, 0);
    }
    // método para verificar se a string possui apenas vogais
    private static boolean soVogais (String palavra, int i){
        boolean vogais;
        int tam = palavra.length();

        if (i < tam) {
            if (!(palavra.charAt(i) == 'a' || palavra.charAt(i) == 'A' || palavra.charAt(i) == 'e' || palavra.charAt(i) == 'E' || palavra.charAt(i) == 'i' || palavra.charAt(i) == 'I' || palavra.charAt(i) == 'o' || palavra.charAt(i) == 'O' || palavra.charAt(i) == 'u' || palavra.charAt(i) == 'U')) {
                vogais = false;
                i = tam;
            }else{
                vogais = soVogais(palavra, i + 1);
            }

        } else {
            vogais = true;
        }
        return vogais;
    }

//----------------------------------------------------------------------------------------------------------------------

    private static boolean soConsoantes(String palavra){
        return soConsoantes(palavra, 0);
    }
    // método para verificar se a string possui apenas consoantes
    private static boolean soConsoantes(String palavra, int i){
        boolean consoantes;
        int tam = palavra.length();
        
        if(i < tam){
            
            // if para caso o caracter seja uma vogal, retornar falso
            if(palavra.charAt(i) == 'a' || palavra.charAt(i) == 'A' || palavra.charAt(i) == 'e' || palavra.charAt(i) == 
            'E' || palavra.charAt(i) == 'i' || palavra.charAt(i) == 'I' || palavra.charAt(i) == 'o' || palavra.charAt(i) == 
            'O' || palavra.charAt(i) == 'u' || palavra.charAt(i) == 'U'){
                consoantes = false;
                i = tam;
            }
            
            // if para analisar se possui algum número na string
            else if(palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9'){
                consoantes = false;
                i = tam;
            }
            else{
                // chamada recursiva para análise do próximo caracter
                consoantes = soConsoantes(palavra, i + 1);
            }
        }else{
            consoantes = true;
        }

        return consoantes;
    }

//----------------------------------------------------------------------------------------------------------------------

    private static boolean inteiros(String palavra){
        return inteiros(palavra, 0);
    }
    // método para verificar se a string é um número inteiro
    private static boolean inteiros(String palavra, int i){
        boolean inteiros;
        int tam = palavra.length();

        // se entrar nesse if, a string foi completamente percorrida e está tudo certo
        if(i < tam){
            
            // if para caso o caracter não seja um número de 0 a 9, retorne falso
            if(!(palavra.charAt(i) >= '0' && palavra.charAt(i) <= '9')){
                inteiros = false;
            }else{
                // chamada recursiva para análise do próximo caracter
                inteiros = inteiros(palavra, i + 1);
            }
        }else {
            inteiros = true;
        }

        return inteiros;
    }

//----------------------------------------------------------------------------------------------------------------------

    private static boolean reais(String palavra) {
        return reais(palavra, 0, 0);
    }

    private static boolean reais(String palavra, int i, int contarPontos) {
        boolean real = true;
        int tam = palavra.length();

        if (i < tam) {
            char c = palavra.charAt(i);

            if (c == '.' || c == ',') {
                contarPontos++;
                if (contarPontos > 1) {
                    real = false;
                }
            } else if (c < '0' || c > '9') {
                real = false;
            }

            // Se ainda for um número real válido, continua verificando os próximos caracteres
            if (real) {
                real = reais(palavra, i + 1, contarPontos);
            }
        }

        return real;
    }


//----------------------------------------------------------------------------------------------------------------------

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
