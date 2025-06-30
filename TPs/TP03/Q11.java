import java.util.Scanner;

class Matriz {
    int linhas, colunas;
    int[][] dados;

    public Matriz(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        dados = new int[linhas][colunas];
    }

    public void preencher(Scanner sc) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                dados[i][j] = sc.nextInt();
            }
        }
    }

    public void mostrarDiagonalPrincipal() {
        int limite = Math.min(linhas, colunas);
        for (int i = 0; i < limite; i++) {
            System.out.print(dados[i][i] + " ");
        }
        System.out.println();
    }

    public void mostrarDiagonalSecundaria() {
        int limite = Math.min(linhas, colunas);
        for (int i = 0; i < limite; i++) {
            System.out.print(dados[i][colunas - 1 - i] + " ");
        }
        System.out.println();
    }

    public Matriz soma(Matriz m) {
        if (linhas != m.linhas || colunas != m.colunas)
            throw new IllegalArgumentException("Dimensões incompatíveis para soma");

        Matriz res = new Matriz(linhas, colunas);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                res.dados[i][j] = dados[i][j] + m.dados[i][j];
            }
        }
        return res;
    }

    public Matriz multiplicacao(Matriz m) {
        if (colunas != m.linhas)
            throw new IllegalArgumentException("Dimensões incompatíveis para multiplicação");

        Matriz res = new Matriz(linhas, m.colunas);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < m.colunas; j++) {
                for (int k = 0; k < colunas; k++) {
                    res.dados[i][j] += dados[i][k] * m.dados[k][j];
                }
            }
        }
        return res;
    }

    public void imprimir() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(dados[i][j] + " ");
            }
            System.out.println();
        }
    }
}

public class Q11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int casos = sc.nextInt();

        for (int t = 0; t < casos; t++) {
            // Primeira matriz
            int l1 = sc.nextInt();
            int c1 = sc.nextInt();
            Matriz m1 = new Matriz(l1, c1);
            m1.preencher(sc);

            // Segunda matriz
            int l2 = sc.nextInt();
            int c2 = sc.nextInt();
            Matriz m2 = new Matriz(l2, c2);
            m2.preencher(sc);

            // Diagonais da primeira matriz
            m1.mostrarDiagonalPrincipal();
            m1.mostrarDiagonalSecundaria();

            // Soma
            Matriz soma = m1.soma(m2);
            soma.imprimir();

            // Multiplicação
            Matriz mult = m1.multiplicacao(m2);
            mult.imprimir();
        }
    }
}
