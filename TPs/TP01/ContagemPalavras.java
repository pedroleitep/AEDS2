import java.util.Scanner;

public class ContagemPalavras {

    private static int contaPalavras(String linha){
        int palavras = 0;
        int tam = linha.length() - 1;

        //for para contar os espaços na string
        for(int i = 0; i <= tam; i++){
            if(linha.charAt(i) == ' '){
                palavras++;
            }
        }
        //retorna a quantidade de espaços mais um que seria a última palavra
        return palavras+1;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String linha = sc.nextLine();

        while (!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            System.out.println(contaPalavras(linha));
            linha = sc.nextLine();
        }
        sc.close();
    }
}
