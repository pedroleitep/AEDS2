// Inclua esse código todo no seu arquivo principal .c

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>

#define MAX_SHOWS 1400
#define MAX_STR 200

typedef struct {
    char id[MAX_STR], type[MAX_STR], title[MAX_STR], director[MAX_STR], country[MAX_STR];
    char dateAdded[MAX_STR], rating[MAX_STR], duration[MAX_STR];
    int releaseYear;
    char **cast;
    int castSize;
    char **listedIn;
    int listedInSize;
} Show;

typedef struct No {
    Show show;
    struct No *esq, *dir;
    int altura;
} No;

No* novoNo(Show s) {
    No* no = malloc(sizeof(No));
    no->show = s;
    no->esq = no->dir = NULL;
    no->altura = 1;
    return no;
}

int max(int a, int b) { return (a > b) ? a : b; }

int altura(No* no) { return (no ? no->altura : 0); }

int fatorBalanceamento(No* no) {
    return (no ? altura(no->esq) - altura(no->dir) : 0);
}

No* rotacaoDir(No* y) {
    No* x = y->esq;
    No* T2 = x->dir;
    x->dir = y;
    y->esq = T2;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    return x;
}

No* rotacaoEsq(No* x) {
    No* y = x->dir;
    No* T2 = y->esq;
    y->esq = x;
    x->dir = T2;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    return y;
}

No* inserirAVL(No* raiz, Show s) {
    if (!raiz) return novoNo(s);

    int cmp = strcasecmp(s.title, raiz->show.title);
    if (cmp < 0)
        raiz->esq = inserirAVL(raiz->esq, s);
    else if (cmp > 0)
        raiz->dir = inserirAVL(raiz->dir, s);
    else
        return raiz; // não insere duplicado

    raiz->altura = 1 + max(altura(raiz->esq), altura(raiz->dir));
    int fb = fatorBalanceamento(raiz);

    if (fb > 1 && strcasecmp(s.title, raiz->esq->show.title) < 0)
        return rotacaoDir(raiz);
    if (fb < -1 && strcasecmp(s.title, raiz->dir->show.title) > 0)
        return rotacaoEsq(raiz);
    if (fb > 1 && strcasecmp(s.title, raiz->esq->show.title) > 0) {
        raiz->esq = rotacaoEsq(raiz->esq);
        return rotacaoDir(raiz);
    }
    if (fb < -1 && strcasecmp(s.title, raiz->dir->show.title) < 0) {
        raiz->dir = rotacaoDir(raiz->dir);
        return rotacaoEsq(raiz);
    }

    return raiz;
}

void pesquisarAVL(No* raiz, char* title) {
    printf("raiz ");
    while (raiz != NULL) {
        int cmp = strcasecmp(title, raiz->show.title);
        if (cmp == 0) {
            printf("SIM\n");
            return;
        } else if (cmp < 0) {
            printf("esq ");
            raiz = raiz->esq;
        } else {
            printf("dir ");
            raiz = raiz->dir;
        }
    }
    printf("NAO\n");
}

// ------------------- Funções auxiliares anteriores -------------------

char* trim(char* str) {
    while (isspace(*str)) str++;
    if (*str == 0) return str;
    char* end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) end--;
    *(end + 1) = 0;
    return str;
}

char** split(char* str, const char* delim, int* size) {
    char* token = strtok(str, delim);
    char** result = NULL;
    *size = 0;
    while (token) {
        while (isspace(*token)) token++;
        result = realloc(result, (*size + 1) * sizeof(char*));
        result[*size] = strdup(token);
        (*size)++;
        token = strtok(NULL, delim);
    }
    return result;
}

void ordenarCampos(char** array, int size) {
    for (int i = 0; i < size-1; i++) {
        for (int j = i+1; j < size; j++) {
            if (strcmp(array[i], array[j]) > 0) {
                char* tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
    }
}

void lerCSV(char* path, Show* lista, int* total) {
    FILE* file = fopen(path, "r");
    if (!file) { perror("Erro ao abrir o arquivo"); exit(1); }

    char linha[1000];
    fgets(linha, sizeof(linha), file);
    while (fgets(linha, sizeof(linha), file) && *total < MAX_SHOWS) {
        Show s;
        char* campos[11] = {NULL};
        int col = 0, aspas = 0, b = 0;
        char buffer[1000];
        for (int i = 0; linha[i]; i++) {
            char c = linha[i];
            if (c == '"') aspas = !aspas;
            else if (c == ',' && !aspas) {
                buffer[b] = '\0';
                campos[col++] = strdup(trim(buffer));
                b = 0;
            } else buffer[b++] = c;
        }
        buffer[b] = '\0'; campos[col] = strdup(trim(buffer));
        strcpy(s.id, campos[0] ? campos[0] : "NaN");
        strcpy(s.type, campos[1] ? campos[1] : "NaN");
        strcpy(s.title, campos[2] ? campos[2] : "NaN");
        strcpy(s.director, campos[3] ? campos[3] : "NaN");

        if (!campos[4]) { s.cast = malloc(sizeof(char*)); s.cast[0] = strdup("NaN"); s.castSize = 1; }
        else { s.cast = split(campos[4], ",", &s.castSize); ordenarCampos(s.cast, s.castSize); }

        strcpy(s.country, campos[5] ? campos[5] : "NaN");
        strcpy(s.dateAdded, campos[6] ? campos[6] : "NaN");
        s.releaseYear = (campos[7] && strlen(campos[7]) > 0) ? atoi(campos[7]) : -1;
        strcpy(s.rating, campos[8] ? campos[8] : "NaN");
        strcpy(s.duration, campos[9] ? campos[9] : "NaN");

        if (!campos[10]) { s.listedIn = malloc(sizeof(char*)); s.listedIn[0] = strdup("NaN"); s.listedInSize = 1; }
        else { s.listedIn = split(campos[10], ",", &s.listedInSize); ordenarCampos(s.listedIn, s.listedInSize); }

        lista[(*total)++] = s;
        for (int i = 0; i <= col; i++) free(campos[i]);
    }

    fclose(file);
}

// ------------------- MAIN -------------------

int main() {
    Show lista[MAX_SHOWS];
    int total = 0;
    lerCSV("/tmp/disneyplus.csv", lista, &total);

    No* raiz = NULL;
    char entrada[MAX_STR];

    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        for (int i = 0; i < total; i++) {
            if (strcmp(lista[i].id, entrada) == 0) {
                raiz = inserirAVL(raiz, lista[i]);
                break;
            }
        }
    }

    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        pesquisarAVL(raiz, entrada);
    }

    return 0;
}
