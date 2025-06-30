import java.util.Scanner;

public class SubstringSemRepeticao {

    public static int comprimentoSubstringSemRepeticao(String str) {
        int[] indiceCaracter = new int[256]; // Array para armazenar a última posição de cada caractere
        for (int i = 0; i < 256; i++) {
            indiceCaracter[i] = -1; // Inicializa todas as posições como -1 (não visto)
        }

        int maxLength = 0; // Tamanho máximo da substring sem repetição
        int esquerda = 0;  // Posição inicial da janela deslizante

        for (int direita = 0; direita < str.length(); direita++) {
            char atual = str.charAt(direita);

            // Se o caractere já apareceu, move a posição inicial (esquerda) para frente
            if (indiceCaracter[atual] >= esquerda) {
                esquerda = indiceCaracter[atual] + 1;
            }

            // Atualiza a última posição do caractere
            indiceCaracter[atual] = direita;

            // Atualiza o tamanho máximo da substring
            int tamanhoAtual = direita - esquerda + 1;
            if (tamanhoAtual > maxLength) {
                maxLength = tamanhoAtual;
            }
        }

        return maxLength;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String palavra = sc.nextLine();

        while(!(palavra.length() == 3 && palavra.charAt(0) == 'F' && palavra.charAt(1) == 'I' && palavra.charAt(2) == 'M')) {
            System.out.println(comprimentoSubstringSemRepeticao(palavra));
            palavra = sc.nextLine();
        }

        sc.close();
    }
}
