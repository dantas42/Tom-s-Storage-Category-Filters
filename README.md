# Tom's Storage Category Filters

English version: [README.en.md](README.en.md)

Código-fonte: [github.com/dantas42/Tom-s-Storage-Category-Filters](https://github.com/dantas42/Tom-s-Storage-Category-Filters/)
Autor: dantas42

Addon **client side** para o [Tom's Simple Storage Mod](https://www.curseforge.com/minecraft/mc-mods/toms-storage) que adiciona botões de categoria ao **Terminal de Armazenamento** e ao **Terminal de Crafting**. Com um clique você filtra os itens guardados por tipo, sem precisar digitar nada na barra de pesquisa.

## Categorias

Os botões ficam à direita da interface do terminal, em uma coluna. Passe o mouse sobre um botão para ver o nome e a descrição.

| Botão | Mostra |
|---|---|
| **Tudo** | Todos os itens (sem filtro de categoria) |
| **Armas** | Espadas, machados, arcos, bestas, tridentes, maças e outras armas com dano de ataque |
| **Armaduras** | Capacetes, peitorais, calças, botas e escudos |
| **Ferramentas** | Picaretas, pás, enxadas, tesouras, varas de pescar e similares |
| **Comida** | Alimentos comestíveis ou bebíveis |
| **Blocos** | Itens que colocam um bloco no mundo |
| **Materiais** | Lingotes, gemas, pós, placas e outros recursos, além de itens que não caem em nenhuma outra categoria |
| **Minérios** | Blocos de minério e minérios brutos |
| **Madeiras** | Troncos e tábuas |
| **Acessórios** | Itens de acessório do **Curios** (colares, anéis, cintos e outros, de qualquer mod) |
| **Apotheosis** | Equipamentos com afixos ou encaixes de gemas |

Quando o **Apotheosis** está instalado, uma segunda coluna aparece ao lado com sub-filtros:

- **Afixos**: itens com afixos.
- **Encaixes**: equipamentos com pelo menos um encaixe de gema, vazio ou com gema.
- **Gemas encaixadas**: equipamentos com uma gema instalada.
- **Gemas**: as gemas soltas (os próprios itens de gema).
- **Encantamento**: blocos que dão Quanta, Arcana ou Eterna à mesa de encantamento. Aparece com o **Apothic Enchanting** instalado.

Um item pode aparecer em mais de uma categoria. Um machado de diamante, por exemplo, aparece em **Armas** e em **Ferramentas**.

## Como usar

1. Abra o Terminal de Armazenamento ou de Crafting.
2. Clique em uma categoria para filtrar. A categoria escolhida fica com um contorno dourado.
3. Clique em **Tudo** para remover o filtro.

O filtro combina com a pesquisa de texto. Por exemplo, com **Armas** selecionado e `diamond` na pesquisa, aparecem só as armas que correspondem a "diamond". O texto digitado continua ali ao trocar de categoria.

## Segurança

O addon só muda a forma como a lista de itens é exibida. Ele não move, remove ou duplica itens, não altera quantidades e não mexe em afixos, gemas ou encantamentos. Todo o armazenamento continua funcionando como antes.

## Requisitos

| Mod | Versão | Obrigatório |
|---|---|---|
| Minecraft | 1.21.1 | Sim |
| NeoForge | 21.1.235 ou superior | Sim |
| Tom's Simple Storage Mod | 2.4.2 ou superior | Sim |
| Apotheosis | 8.9.0 ou superior | Não (libera os sub-filtros do Apotheosis) |
| Apothic Enchanting | 1.6.2 ou superior | Não (libera o sub-filtro Encantamento) |
| Curios | 9.5.1 ou superior | Não (libera a categoria Acessórios) |

O addon é **somente client side**: não precisa estar instalado no servidor.

## Instalação

1. Instale o NeoForge 1.21.1 e o Tom's Simple Storage Mod.
2. Coloque o arquivo `toms_storage_category_filters-1.0.0.jar` na pasta `mods` da sua instância.
3. Abra o jogo.

## Limitações conhecidas

- A integração depende de partes internas da tela do Tom's Simple Storage 2.4.2. Se uma atualização grande do Tom's mudar essa tela, o addon pode precisar de atualização.
- As categorias são baseadas em tags e componentes dos itens. Mods que não seguem esses padrões podem aparecer em uma categoria diferente da esperada.
- A categoria **Encantamento** cobre apenas blocos listados nos dados do Apothic Enchanting. Blocos que dão esses valores só pelo código não aparecem.

## Créditos

- Addon por dantas42.
- Tom's Simple Storage Mod por tom5454.
- Apotheosis por Shadows_of_Fire.

---

## Desenvolvimento

Para compilar a partir do código-fonte:

```powershell
.\gradlew.bat clean build
```

Requer **JDK 21**. O JAR gerado fica em `build/libs/`. Os testes unitários das categorias rodam com `.\gradlew.bat test`.

A documentação de como cada categoria identifica os itens está em [`docs/CATEGORIAS.md`](docs/CATEGORIAS.md).

Licença: All Rights Reserved.
