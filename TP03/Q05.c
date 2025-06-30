#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAX_SHOWS 1400
#define MAX_STR 200
#define MAX_LEN 100

typedef struct Show {
    char id[MAX_STR], type[MAX_STR], title[MAX_STR], director[MAX_STR], country[MAX_STR];
    char dateAdded[MAX_STR], rating[MAX_STR], duration[MAX_STR];
    int releaseYear;
    char **cast;
    int castSize;
    char **listedIn;
    int listedInSize;
} Show;

typedef struct Node {
    Show data;
    struct Node* next;
} Node;

typedef struct {
    Node* head;
    int size;
} ListaFlexivel;

char* trim(char* str) {
    while (isspace(*str)) str++;
    if (*str == 0) return str;
    char* end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) end--;
    *(end+1) = 0;
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

int maiorQue(const char* a, const char* b) {
    return strcmp(a, b) > 0;
}

void ordenarCampos(char** array, int size) {
    for (int i = 0; i < size-1; i++) {
        for (int j = i+1; j < size; j++) {
            if (maiorQue(array[i], array[j])) {
                char* tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
    }
}

void lerCSV(char* path, Show* lista, int* total) {
    FILE* file = fopen(path, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        exit(1);
    }

    char linha[1000];
    fgets(linha, sizeof(linha), file); // cabeçalho

    while (fgets(linha, sizeof(linha), file) && *total < MAX_SHOWS) {
        Show s;
        char* campos[11] = {NULL};
        char* ptr = linha;
        int col = 0;
        int aspas = 0;
        char buffer[1000];
        int b = 0;

        for (int i = 0; linha[i]; i++) {
            char c = linha[i];
            if (c == '"') {
                aspas = !aspas;
            } else if (c == ',' && !aspas) {
                buffer[b] = '\0';
                campos[col++] = strdup(trim(buffer));
                b = 0;
            } else {
                buffer[b++] = c;
            }
        }
        buffer[b] = '\0';
        campos[col] = strdup(trim(buffer));

        // Preencher campos ausentes com "NaN"
        for (int i = 0; i <= 10; i++) {
            if (campos[i] == NULL || strlen(campos[i]) == 0) {
                campos[i] = strdup("NaN");
            }
        }


        strcpy(s.id, campos[0] ? campos[0] : "NaN");
        strcpy(s.type, campos[1] ? campos[1] : "NaN");
        strcpy(s.title, campos[2] ? campos[2] : "NaN");
        strcpy(s.director, campos[3] ? campos[3] : "NaN");

        if (!campos[4] || strlen(campos[4]) == 0) {
            s.cast = malloc(sizeof(char*));
            s.cast[0] = strdup("NaN");
            s.castSize = 1;
        } else {
            s.cast = split(campos[4], ",", &s.castSize);
            ordenarCampos(s.cast, s.castSize);
        }

        strcpy(s.country, campos[5] ? campos[5] : "NaN");
        strcpy(s.dateAdded, campos[6] ? campos[6] : "NaN");
        s.releaseYear = campos[7] ? atoi(campos[7]) : -1;
        strcpy(s.rating, campos[8] ? campos[8] : "NaN");
        strcpy(s.duration, campos[9] ? campos[9] : "NaN");

        if (!campos[10] || strlen(campos[10]) == 0) {
            s.listedIn = malloc(sizeof(char*));
            s.listedIn[0] = strdup("NaN");
            s.listedInSize = 1;
        } else {
            s.listedIn = split(campos[10], ",", &s.listedInSize);
            ordenarCampos(s.listedIn, s.listedInSize);
        }

        lista[(*total)++] = s;

        for (int i = 0; i <= col; i++) free(campos[i]);
    }

    fclose(file);
}

void imprimir(Show* s) {
    printf("=> %s ## %s ## %s ## %s ## [", s->id, s->title, s->type, s->director);
    for (int i = 0; i < s->castSize; i++) {
        printf("%s", s->cast[i]);
        if (i < s->castSize - 1) printf(", ");
    }
    printf("] ## %s ## %s ## %d ## %s ## %s ## [", s->country, s->dateAdded, s->releaseYear, s->rating, s->duration);
    for (int i = 0; i < s->listedInSize; i++) {
        printf("%s", s->listedIn[i]);
        if (i < s->listedInSize - 1) printf(", ");
    }
    printf("] ##\n");
}

Show cloneShow(Show* s) {
    Show c = *s;
    c.cast = malloc(s->castSize * sizeof(char*));
    for (int i = 0; i < s->castSize; i++) c.cast[i] = strdup(s->cast[i]);
    c.listedIn = malloc(s->listedInSize * sizeof(char*));
    for (int i = 0; i < s->listedInSize; i++) c.listedIn[i] = strdup(s->listedIn[i]);
    return c;
}

// Lista encadeada: operações

void inserirInicio(ListaFlexivel* lista, Show s) {
    Node* novo = malloc(sizeof(Node));
    novo->data = s;
    novo->next = lista->head;
    lista->head = novo;
    lista->size++;
}

void inserirFim(ListaFlexivel* lista, Show s) {
    Node* novo = malloc(sizeof(Node));
    novo->data = s;
    novo->next = NULL;

    if (lista->head == NULL) {
        lista->head = novo;
    } else {
        Node* temp = lista->head;
        while (temp->next != NULL)
            temp = temp->next;
        temp->next = novo;
    }
    lista->size++;
}

void inserir(ListaFlexivel* lista, Show s, int pos) {
    if (pos == 0) {
        inserirInicio(lista, s);
    } else {
        Node* novo = malloc(sizeof(Node));
        novo->data = s;
        Node* temp = lista->head;
        for (int i = 0; i < pos - 1 && temp != NULL; i++) {
            temp = temp->next;
        }
        novo->next = temp->next;
        temp->next = novo;
        lista->size++;
    }
}

Show removerInicio(ListaFlexivel* lista) {
    Node* temp = lista->head;
    lista->head = lista->head->next;
    Show r = temp->data;
    free(temp);
    lista->size--;
    return r;
}

Show removerFim(ListaFlexivel* lista) {
    Node* atual = lista->head;
    Node* anterior = NULL;

    while (atual->next != NULL) {
        anterior = atual;
        atual = atual->next;
    }

    Show r = atual->data;
    if (anterior == NULL) {
        lista->head = NULL;
    } else {
        anterior->next = NULL;
    }

    free(atual);
    lista->size--;
    return r;
}

Show remover(ListaFlexivel* lista, int pos) {
    if (pos == 0) return removerInicio(lista);

    Node* anterior = lista->head;
    for (int i = 0; i < pos - 1; i++) {
        anterior = anterior->next;
    }

    Node* temp = anterior->next;
    anterior->next = temp->next;
    Show r = temp->data;
    free(temp);
    lista->size--;
    return r;
}

void mostrarLista(ListaFlexivel* lista) {
    Node* atual = lista->head;
    int i = 0;
    while (atual != NULL) {
        imprimir(&atual->data);
        atual = atual->next;
    }
}

int main() {
    Show lista[MAX_SHOWS];
    int total = 0;
    ListaFlexivel selecionados = {NULL, 0};

    lerCSV("/tmp/disneyplus.csv", lista, &total);

    char entrada[MAX_STR];
    do {
        scanf(" %[^\n]", entrada);
        if (strcmp(entrada, "FIM") != 0) {
            for (int i = 0; i < total; i++) {
                if (strcmp(lista[i].id, entrada) == 0) {
                    inserirFim(&selecionados, lista[i]);
                    break;
                }
            }
        }
    } while (strcmp(entrada, "FIM") != 0);

    int nComandos;
    scanf("%d", &nComandos);
    getchar();

    for (int i = 0; i < nComandos; i++) {
        fgets(entrada, MAX_STR, stdin);
        if (strncmp(entrada, "II", 2) == 0) {
            char id[MAX_LEN];
            sscanf(entrada + 3, "%s", id);
            for (int j = 0; j < total; j++) {
                if (strcmp(lista[j].id, id) == 0) {
                    inserirInicio(&selecionados, lista[j]);
                    break;
                }
            }
        } else if (strncmp(entrada, "IF", 2) == 0) {
            char id[MAX_LEN];
            sscanf(entrada + 3, "%s", id);
            for (int j = 0; j < total; j++) {
                if (strcmp(lista[j].id, id) == 0) {
                    inserirFim(&selecionados, cloneShow(&lista[j]));
                    break;
                }
            }
        } else if (strncmp(entrada, "I*", 2) == 0) {
            int pos;
            char id[MAX_LEN];
            sscanf(entrada + 3, "%d %s", &pos, id);
            for (int j = 0; j < total; j++) {
                if (strcmp(lista[j].id, id) == 0) {
                    inserir(&selecionados, lista[j], pos);
                    break;
                }
            }
        } else if (strncmp(entrada, "RI", 2) == 0) {
            Show r = removerInicio(&selecionados);
            printf("(R) %s\n", r.title);
        } else if (strncmp(entrada, "R*", 2) == 0) {
            int pos;
            sscanf(entrada + 3, "%d", &pos);
            Show r = remover(&selecionados, pos);
            printf("(R) %s\n", r.title);
        } else if (strncmp(entrada, "R", 1) == 0) {
            Show r = removerFim(&selecionados);
            printf("(R) %s\n", r.title);
        }
    }

    mostrarLista(&selecionados);

    return 0;
}
