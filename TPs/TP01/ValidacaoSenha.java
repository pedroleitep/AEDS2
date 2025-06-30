import java.util.Scanner;

public class ValidacaoSenha {

    private static boolean senhaValida(String senha){
        int maiuscula = 0;
        int minuscula = 0;
        int numero = 0;
        int especial = 0;

        if(senha.length() < 8){
            return false;
        }else{
            for(int i = 0; i < senha.length(); i++){
                // verifica se possui letras maiusculas
                if(senha.charAt(i) >= 'A' && senha.charAt(i) <= 'Z'){
                    maiuscula++;
                }// verifica se possui letras minusculas
                else if(senha.charAt(i) >= 'a' && senha.charAt(i) <= 'z'){
                    minuscula++;
                }// verifica se possui numeros
                else if(senha.charAt(i) >= '0' && senha.charAt(i) <= '9'){
                    numero++;
                }else especial++; // verifica se possui caracteres especiais

            }
            // verifica se possui todos os atributos e retorna true caso seja verdadeiro
            if(maiuscula > 0 && minuscula > 0 && numero > 0 && especial > 0){
                return true;
            }else return false;
        }
    }

    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        String senha = sc.nextLine();

        while(!(senha.length() == 3 && senha.charAt(0) == 'F' && senha.charAt(1) == 'I' && senha.charAt(2) == 'M')) {
            if(senhaValida(senha)) System.out.println("SIM");
            else System.out.println("NAO");
            
            senha = sc.nextLine();
        }

        sc.close();
    }
}
