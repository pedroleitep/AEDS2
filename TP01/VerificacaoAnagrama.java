import java.util.*;

public class VerificacaoAnagrama {

    
    // Função para verificar se duas partes de uma string são anagramas
    static boolean ehAnagrama(String linha) {
        boolean anagrama = true;
        String palavra2 = ""; // Guarda a palvra depois do '-'
        int n = linha.length();
        int posicao = 0; // Posição do caractere '-' na linha
        
        // Encontra a posição do caractere '-' na linha
        for (int i = 0; i < n; i++) {
            if (linha.charAt(i) == '-') {
                posicao = i; // Guarda a posição do '-'
                i = n; // Encerra o loop
            }
        }
        
        // Copia a parte da frase após o '-' para palavra2
        for (int i = posicao + 2; i < n; i++) {
            char letra = linha.charAt(i);
            palavra2 += letra; // Atribui cada character em palavra2
        }
        
        // Guarda a palvra antes do '-'
        String palavra1 = ""; 
        for (int i = 0; i < posicao - 1; i++) {
            palavra1 += linha.charAt(i);
        }
        
        // Converte as duas partes para minúsculas
        palavra1 = palavra1.toLowerCase();
        palavra2 = palavra2.toLowerCase();
        
        n = palavra1.length(); // Atualiza o tamanho da primeira parte
        int cont = 0; // Contador de caracteres correspondentes
        
        // Se as duas partes não tiverem o mesmo tamanho, não são anagramas
        if (n != palavra2.length()) {
            anagrama = false;
        }
        
        // Verifica se as duas partes são anagramas
        if (anagrama == true) {
            // Compara cada caractere da palavra1 com os caracteres da palavra2
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (palavra1.charAt(i) == palavra2.charAt(j)) {
                        cont++;
                        j = n;
                    }
                }
                // Se não houver correspondência, não são anagramas
                if (cont < 1) {
                    anagrama = false;
                }
                cont = 0; // Reseta o contador
            }
        }
        
        return anagrama; 
    }
    
    static public void main(String[] args) {
        
        Scanner sc = new Scanner(System.in); 
        String linha = sc.nextLine();
        
        // Loop que continua até que o usuário digite "FIM"
        while(!(linha.length() == 3 && linha.charAt(0) == 'F' && linha.charAt(1) == 'I' && linha.charAt(2) == 'M')) {
            
            // Verifica se a frase é um anagrama
            if (ehAnagrama(linha)) {
                System.out.println("SIM"); 
            } else {
                System.out.println("N" + (char) ('a' + 'b') + "O");
            }
            
            linha = sc.nextLine();
        }
        
        sc.close(); 
    }
}