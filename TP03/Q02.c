//TP03Q02 - Lista com Alocação Sequencial em C
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_SHOWS 1400
#define MAX_LINE 4096
#define MAX_LEN 100
#define MAX_CAST 10
#define MAX_LISTED 10

typedef struct {
    char id[MAX_LEN];
    char type[MAX_LEN];
    char title[MAX_LEN];
    char director[MAX_LEN];
    char cast[MAX_CAST][MAX_LEN];
    int castCount;
    char country[MAX_LEN];
    char dateAdded[MAX_LEN];
    int releaseYear;
    char rating[MAX_LEN];
    char duration[MAX_LEN];
    char listedIn[MAX_LISTED][MAX_LEN];
    int listedCount;
} Show;

typedef struct {
    Show array[1000];
    int n;
} ListaSequencial;

void ordenarArray(char arr[][MAX_LEN], int n);

char* trim(char* str) {
    while (*str == ' ') str++;
    char* end = str + strlen(str) - 1;
    while (end > str && *end == ' ') end--;
    *(end + 1) = '\0';
    return str;
}

void split(char* str, char arr[][MAX_LEN], int* count) {
    *count = 0;
    char* token = strtok(str, ",");
    
    if (!token) {
        strcpy(arr[0], "NaN");
        *count = 1;
        return;
    }

    while (token && *count < MAX_CAST) {
        char* trimmed = trim(token);
        if (strlen(trimmed) > 0) {
            strcpy(arr[(*count)++], trimmed);
        }
        token = strtok(NULL, ",");
    }

    if (*count == 0) {
        strcpy(arr[0], "NaN");
        *count = 1;
    }
}

void lerShow(Show* s, char* linha) {
    char* token;
    int i = 0;
    char campos[11][MAX_LINE] = {0};
    bool dentroAspas = false;
    int campoIdx = 0, pos = 0;

    for (int j = 0; linha[j] != '\0'; j++) {
        char c = linha[j];

        if (c == '"') {
            dentroAspas = !dentroAspas;
        } else if (c == ',' && !dentroAspas) {
            campos[campoIdx][pos] = '\0';
            campoIdx++;
            pos = 0;
        } else {
            campos[campoIdx][pos++] = c;
        }
    }
    campos[campoIdx][pos] = '\0';

    // Modificar a parte de atribuição dos campos
    strcpy(s->id, strlen(trim(campos[0])) > 0 ? trim(campos[0]) : "NaN");
    strcpy(s->type, strlen(trim(campos[1])) > 0 ? trim(campos[1]) : "NaN");
    strcpy(s->title, strlen(trim(campos[2])) > 0 ? trim(campos[2]) : "NaN");
    strcpy(s->director, strlen(trim(campos[3])) > 0 ? trim(campos[3]) : "NaN");

    // Tratar elenco
    if (strlen(trim(campos[4])) > 0) {
        char tempCast[MAX_LINE];
        strcpy(tempCast, campos[4]);
        split(tempCast, s->cast, &s->castCount);
        ordenarArray(s->cast, s->castCount);
    } else {
        strcpy(s->cast[0], "NaN");
        s->castCount = 1;
    }

    strcpy(s->country, strlen(trim(campos[5])) > 0 ? trim(campos[5]) : "NaN");
    strcpy(s->dateAdded, strlen(trim(campos[6])) > 0 ? trim(campos[6]) : "NaN");
    s->releaseYear = (strlen(trim(campos[7])) > 0) ? atoi(campos[7]) : -1;
    strcpy(s->rating, strlen(trim(campos[8])) > 0 ? trim(campos[8]) : "NaN");
    strcpy(s->duration, strlen(trim(campos[9])) > 0 ? trim(campos[9]) : "NaN");

    // Tratar categorias
    if (strlen(trim(campos[10])) > 0) {
        char tempListed[MAX_LINE];
        strcpy(tempListed, campos[10]);
        split(tempListed, s->listedIn, &s->listedCount);
        ordenarArray(s->listedIn, s->listedCount);
    } else {
        strcpy(s->listedIn[0], "NaN");
        s->listedCount = 1;
    }
}

// Adicione esta função de comparação para ordenação
int compareStrings(const void* a, const void* b) {
    return strcmp((char*)a, (char*)b);
}

// Adicione esta função para ordenar arrays
void ordenarArray(char arr[][MAX_LEN], int n) {
    qsort(arr, n, MAX_LEN * sizeof(char), compareStrings);
}

void imprimirLista(char lista[][MAX_LEN], int count) {
    for (int i = 0; i < count; i++) {
        printf("%s", lista[i]);
        if (i < count - 1) printf(", ");
    }
}

void imprimirShow(Show* s) {
    // Re-ordenar arrays antes de imprimir (caso tenham sido modificados)
    if (s->castCount > 1) ordenarArray(s->cast, s->castCount);
    if (s->listedCount > 1) ordenarArray(s->listedIn, s->listedCount);

    printf("=> %s ## ", s->id);
    printf("%s ## ", s->title);
    printf("%s ## ", s->type);
    printf("%s ## [", s->director);
    imprimirLista(s->cast, s->castCount);
    printf("] ## %s ## ", s->country);
    printf("%s ## ", s->dateAdded);
    if (s->releaseYear != -1)
        printf("%d ## ", s->releaseYear);
    else
        printf("NaN ## ");
    printf("%s ## ", s->rating);
    printf("%s ## [", s->duration);
    imprimirLista(s->listedIn, s->listedCount);
    printf("] ##\n");
}


void inserirInicio(ListaSequencial* lista, Show s) {
    for (int i = lista->n; i > 0; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[0] = s;
    lista->n++;
}

void inserirFim(ListaSequencial* lista, Show s) {
    lista->array[lista->n++] = s;
}

void inserir(ListaSequencial* lista, Show s, int pos) {
    for (int i = lista->n; i > pos; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[pos] = s;
    lista->n++;
}

Show removerInicio(ListaSequencial* lista) {
    Show s = lista->array[0];
    for (int i = 0; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return s;
}

Show removerFim(ListaSequencial* lista) {
    return lista->array[--lista->n];
}

Show remover(ListaSequencial* lista, int pos) {
    Show s = lista->array[pos];
    for (int i = pos; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return s;
}

void mostrar(ListaSequencial* lista) {
    for (int i = 0; i < lista->n; i++) {
        imprimirShow(&lista->array[i]);
    }
}

int main() {
    Show base[MAX_SHOWS];
    int total = 0;
    ListaSequencial lista;
    lista.n = 0;

    FILE* fp = fopen("/tmp/disneyplus.csv", "r");
    if (!fp) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    char linha[MAX_LINE];
    fgets(linha, MAX_LINE, fp); // ignorar cabeçalho
    while (fgets(linha, MAX_LINE, fp) && total < MAX_SHOWS) {
        lerShow(&base[total++], linha);
    }
    fclose(fp);

    char entrada[20];
    while (true) {
        scanf(" %[^\n]", entrada);
        if (strcmp(entrada, "FIM") == 0) break;

        for (int i = 0; i < total; i++) {
            if (strcmp(base[i].id, entrada) == 0) {
                inserirFim(&lista, base[i]);
                break;
            }
        }
    }

    int n;
    scanf("%d", &n);
    getchar(); // consumir newline

    for (int i = 0; i < n; i++) {
        fgets(linha, MAX_LINE, stdin);
        if (strncmp(linha, "II", 2) == 0) {
            char id[MAX_LEN];
            sscanf(linha + 3, "%s", id);
            for (int j = 0; j < total; j++) {
                if (strcmp(base[j].id, id) == 0) {
                    inserirInicio(&lista, base[j]);
                    break;
                }
            }
        } else if (strncmp(linha, "IF", 2) == 0) {
            char id[MAX_LEN];
            sscanf(linha + 3, "%s", id);
            for (int j = 0; j < total; j++) {
                if (strcmp(base[j].id, id) == 0) {
                    inserirFim(&lista, base[j]);
                    break;
                }
            }
        } else if (strncmp(linha, "I*", 2) == 0) {
            int pos;
            char id[MAX_LEN];
            sscanf(linha + 3, "%d %s", &pos, id);
            for (int j = 0; j < total; j++) {
                if (strcmp(base[j].id, id) == 0) {
                    inserir(&lista, base[j], pos);
                    break;
                }
            }
        } else if (strncmp(linha, "RI", 2) == 0) {
            Show r = removerInicio(&lista);
            printf("(R) %s\n", r.title);
        } else if (strncmp(linha, "RF", 2) == 0) {
            Show r = removerFim(&lista);
            printf("(R) %s\n", r.title);
        } else if (strncmp(linha, "R*", 2) == 0) {
            int pos;
            sscanf(linha + 3, "%d", &pos);
            Show r = remover(&lista, pos);
            printf("(R) %s\n", r.title);
        }
    }

    mostrar(&lista);

    return 0;
}
