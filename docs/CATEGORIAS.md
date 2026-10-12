# Categorias do Tom's Storage Category Filters

Este documento explica como cada categoria identifica um item, qual regra vale quando um item pertence a mais de uma
categoria, e de onde vêm os dados usados pelo detector do Apotheosis.

Todas as classificações são feitas em `classify/ItemClassifier.java` a partir de **tags de item**, **classes de item**,
**componentes de dados** e, quando instalado, **componentes do Apotheosis**. Nenhuma regra usa o nome do item, a
descrição visível ou o namespace do item. A classificação não é armazenada em cache: ela é recalculada quando a lista do
terminal é reconstruída, então mudanças nos componentes de um item (novos afixos, gemas, encaixes) são sempre refletidas.

## Regras por categoria

| Botão | Critério | Dados usados |
|---|---|---|
| **Tudo** | Todos os itens | (nenhum filtro de categoria) |
| **Armas** | Espadas, machados, arcos, bestas, tridentes, maças e outros itens que causam dano de ataque | `#minecraft:swords`, `SwordItem`, `#minecraft:axes`, `AxeItem`, `ProjectileWeaponItem`, `TridentItem`, `MaceItem`, modificador `ATTACK_DAMAGE` > 0 (apenas se não for ferramenta nem bloco) |
| **Armaduras** | Capacetes, peitorais, calças, botas e escudos | `ArmorItem`, `Items.SHIELD`, `#minecraft:head_armor`, `#minecraft:chest_armor`, `#minecraft:leg_armor`, `#minecraft:foot_armor` |
| **Ferramentas** | Picaretas, pás, enxadas, machados, tesouras, varas de pescar, isqueiros e similares | `DiggerItem`, `HoeItem`, `AxeItem`, `#minecraft:pickaxes`, `#minecraft:shovels`, `#minecraft:hoes`, componente `TOOL`, `Items.SHEARS`, `Items.FISHING_ROD`, `Items.FLINT_AND_STEEL` |
| **Comida** | Alimentos comestíveis ou bebíveis (mel, maçã dourada, carne...) | componente `FOOD` (`DataComponents.FOOD`) |
| **Blocos** | Itens que colocam um bloco no mundo | `BlockItem` |
| **Materiais** | Materiais de fabricação e recursos diversos | tags `c:ingots`, `c:nuggets`, `c:gems`, `c:dusts`, `c:raw_materials`, `c:plates`, `c:rods` (exceto blocos), **ou** qualquer item que não pertence a Armas, Armaduras, Ferramentas, Comida nem Blocos |
| **Minérios** | Blocos de minério e minérios brutos | tags `c:ores` e `c:raw_materials` |
| **Madeiras** | Troncos e tábuas | tags `minecraft:logs` e `minecraft:planks` |
| **Acessórios** | Itens que o Curios aceita em um slot de acessório (colares, anéis, cintos... de qualquer mod) | `CuriosApi.getCurio(stack).isPresent()`. Só aparece com o Curios instalado. |
| **Apotheosis** | Equipamentos com afixos ou encaixes de gemas | componentes `apotheosis:affixes`, `apotheosis:sockets`, `apotheosis:socketed_gems` (ver abaixo) |
| **Encantamento** (sub-filtro do Apotheosis) | Blocos que dão Quanta, Arcana ou Eterna à mesa de encantamento | registro de dados `enchanting_stats` do Apothic Enchanting. Só aparece com o Apothic Enchanting instalado. |

Observações sobre o que é **excluído** de cada regra:

- Espadas também possuem o componente `TOOL` no Minecraft. Por isso elas são excluídas explicitamente da regra de
  Ferramentas: uma espada aparece só em Armas.
- Picaretas possuem modificador de dano de ataque, mas são ferramentas. Por isso a regra genérica de "arma por dano de
  ataque" não as inclui (a regra exige que o item não seja ferramenta).
- Itens com a classe `BlockItem` nunca são armas nem ferramentas pela regra genérica.
- Armaduras não são armas, mesmo que tenham modificador de dano.

## Itens em mais de uma categoria

A classificação é **multi-pertencimento**: um item aparece em todas as categorias cuja regra ele satisfaz. A exibição
é uma interseção com a pesquisa de texto, então o comportamento é o mesmo em qualquer combinação de filtros.

Exemplos:

| Item | Categorias |
|---|---|
| Machado de diamante | Armas, Ferramentas |
| Espada de diamante | Armas |
| Picareta de ferro | Ferramentas |
| Escudo | Armaduras |
| Arco | Armas |
| Maçã dourada | Comida |
| Pedra | Blocos |
| Lingote de ferro | Materiais |
| Bloco de ferro (armazenamento) | Blocos (não Materiais, pois é bloco) |
| Espada de diamante com afixos do Apotheosis | Armas, Apotheosis |
| Poção | Materiais (não tem componente `FOOD` nem é bloco; cai no fallback) |

Regra de consistência: **Materiais é um fallback**. Um item só entra em Materiais por tag de material ou quando não
pertence a nenhuma das outras categorias de conteúdo. Por isso itens diversos (como poções) aparecem lá.

## Interação com a pesquisa de texto

Quando um botão de categoria está selecionado, a lista exibida é:

```
itens armazenados
  AND correspondência com a pesquisa de texto  (regras originais do Tom's Storage, inclusive @, #, $ e |)
  AND pertence à categoria selecionada
```

O filtro de categoria é aplicado logo depois da pesquisa de texto e antes da ordenação, dentro de
`AbstractStorageTerminalScreen.updateSearch()`. O texto da pesquisa não é alterado ao trocar de categoria.

## Apotheosis

### Onde os dados ficam (Apotheosis 8.9.0, branch `1.21`)

Definidos em `dev.shadowsoffire.apotheosis.Apoth.Components`, com o namespace `apotheosis`:

| Componente | Tipo | Significado |
|---|---|---|
| `apotheosis:affixes` | `ItemAffixes` (mapa afixo -> nível) | Afixos aplicados ao item. `AffixHelper.applyAffix` grava aqui. |
| `apotheosis:sockets` | `Integer` (0 a 16) | Quantidade de encaixes. `SocketHelper.setSockets` grava aqui. |
| `apotheosis:socketed_gems` | `ItemContainerContents` | Um slot por encaixe, com a gema instalada. `SocketHelper.setGems` grava aqui. |
| `apotheosis:rarity`, `apotheosis:gem`, `apotheosis:purity`... | outros | Não são usados para a categoria. |

Os nomes foram confirmados no código-fonte de `Apoth.java`, `AffixHelper.java`, `SocketHelper.java` e `ItemAffixes.java`
da branch `1.21` do Apotheosis.

### Regra do detector

Um item entra em **Apotheosis** se pelo menos uma das condições for verdadeira:

1. `apotheosis:affixes` existe e não está vazio (`ItemAffixes.isEmpty() == false`) -> **afixos**.
2. `apotheosis:sockets` > 0 -> **encaixes**.
3. `apotheosis:socketed_gems` contém pelo menos uma gema -> **gemas instaladas**.

Itens sem esses componentes (por exemplo, uma espada de diamante vanilla) não entram na categoria. Se o Apotheosis não
estiver instalado, o botão **não aparece** e as demais categorias funcionam normalmente.

As três flags são guardadas separadamente em `ApotheosisData(affixes, sockets, gems)`. Elas viraram três sub-filtros
exibidos em uma segunda coluna, ao lado do botão Apotheosis:

| Sub-filtro | Critério |
|---|---|
| **Afixos** (`APOTHEOSIS_AFFIXES`) | `apotheosis:affixes` não vazio |
| **Encaixes** (`APOTHEOSIS_SOCKETS`) | `apotheosis:sockets` > 0 (encaixe vazio ou com gema) |
| **Gemas encaixadas** (`APOTHEOSIS_SOCKETED_GEMS`) | pelo menos uma gema em `apotheosis:socketed_gems` (equipamento com gema instalada) |
| **Gemas** (`APOTHEOSIS_GEMS`) | o próprio item é uma gema do Apotheosis (componente `apotheosis:gem`, gravado por `GemItem.setGem`) |

Os sub-filtros usam a mesma regra de interseção com a pesquisa e com a categoria principal. Como um item pode ter
afixos e encaixes ao mesmo tempo, ele aparece em mais de um sub-filtro.

**Encantamento** mostra os blocos listados nos dados `enchanting_stats` do Apothic Enchanting com algum valor de Eterna,
Quanta ou Arcana maior que zero (por exemplo, estantes de livro e crânios). Ele não separa os três valores em
sub-filtros próprios; isso pode ser adicionado depois.

Note que **Gemas** mostra gemas soltas (os itens de gema). Um equipamento com gema encaixada aparece em **Encaixes**
(e, se tiver gema instalada, o dado `apotheosis:socketed_gems` existe, mas não há sub-filtro separado para ele).

### Decisões sobre o detector

- Os componentes são lidos diretamente com `stack.get(...)`. Não usamos `AffixHelper.getAffixes` nem
  `SocketHelper.getGems`, porque eles usam cache do Apotheosis (com hash dos componentes) e, para encaixes, disparam o
  `GetItemSocketsEvent`. Lendo o componente cru, não existe risco de dado desatualizado.
- A presença de afixo é verificada pelo dado armazenado, não pela validade do afixo no registro. Um afixo que não existe
  mais no registro ainda conta como "tem afixo", o que é o comportamento mais útil para encontrar equipamentos afixados.
- O detector não depende de nomes, descrições, tooltips nem do namespace do item. Uma espada de diamante gerada pelo
  Apotheosis é reconhecida pelos componentes, mesmo tendo o id `minecraft:diamond_sword`.
- Os componentes só são lidos quando `ModList` reporta o `apotheosis` instalado. As classes do Apotheosis ficam em
  `ApotheosisComponents`, que só é carregada nesse caso.

## Limitações conhecidas

- O filtro depende de três pontos internos de `AbstractStorageTerminalScreen` do Tom's Storage 2.4.2:
  `updateSearch()`, o campo `refreshItemList` e a lista `itemListClientSorted`. Não existe um ponto de extensão público
  para filtrar a lista de itens, por isso o addon usa um mixin pequeno. Se o Tom's Storage mudar esses pontos, o mixin
  falha ao carregar. Por isso a dependência declara `[2.4.2,)`, e a validação foi feita contra essa versão.
- A tag `#minecraft:*_armor` e as tags `c:*` dependem de os dados do mod existirem. Se alguma não existir, a regra
  correspondente simplesmente não encontra itens; nenhuma exceção é lançada.
- Equipamentos de outros mods que não usam as classes vanilla (`ArmorItem`, `SwordItem`, `DiggerItem`...) e não possuem
  as tags correspondentes podem ser classificados de forma diferente. O fallback genérico cobre armas com dano de ataque;
  armaduras modded sem `ArmorItem` precisam estar nas tags de armadura.
- Itens com o componente `TOOL` que não são espadas são classificados como ferramentas, e não como armas, mesmo que
  causem dano. Isso acontece com alguns itens de mods que usam esse componente.
