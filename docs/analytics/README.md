# Módulo Analytics e Relatórios

Somente leitura: não altera dados nem arquivos de outros módulos.
Pacote: `com.ghflusao.analytics` · Páginas: `/analytics` e `/analytics/passageiros-por-voo`.

## Regra de receita
Bilhetes `EMITIDO` e `UTILIZADO` geram receita. `CANCELADO` não entra (equivale ao módulo Python,
que só somava vendas `FINALIZADA`). A regra está em um único lugar: `AnalyticsService.SITUACOES_RECEITA`.

## Mapeamento Python (loja de roupas) → Java (voos)
| Python (`AnalyticsController`) | Java | Observação |
|---|---|---|
| `obter_indicadores` (faturamento, quantidade, ticket médio) | `AnalyticsService.resumo()` | Venda FINALIZADA → bilhete válido; ticket = receita / válidos, 0 se não houver |
| `produtos_mais_vendidos(5)` | `voosMaisProcurados(5)` | Soma de itens → contagem de bilhetes válidos; desempate por código do voo |
| `faturamento_por_produto` | colunas "Receita" da tabela de voos | Mesmo agrupamento por voo |
| `faturamento_por_categoria` / `por_marca` | `receitaPorEmpresa()` | Única dimensão equivalente é a Empresa |
| `vendas_por_dia` | `receitaPorDia()` | Por dia de emissão |
| `vendas_por_periodo` / `faturamento_por_periodo` | `resumoPeriodo(inicio, fim)` | Dia final incluído (intervalo semiaberto) |
| `estoque_baixo` / `produtos_sem_estoque` | — | Sem equivalente: o modelo não controla capacidade/assentos |
| — | `passageirosPorVoo(id)` | Novo (relatório pedido) |

## Consultas JPQL (`AnalyticsConsultasJpa`)
Veja o arquivo: todas usam projeção direta em `record` (sem carregar entidades, sem N+1).
Parâmetro `:situacoes` = `SITUACOES_RECEITA`.

## Dados de demonstração (opcional)
`dados_demo_analytics.sql` — rodar manualmente (não é migration). Deixa o ranking e os gráficos com variação.

## Limitações
- Receita diária agrupa em Java os bilhetes válidos (evita funções de data específicas do banco); suficiente para o porte do projeto.
- Receita por empresa lista só empresas com bilhetes válidos.
- Voo não tem data de partida: os períodos usam a **data de emissão** do bilhete.
- Cancelamento não gera reembolso no modelo atual; bilhete cancelado é simplesmente excluído da receita.
- Se a outra frente alterar nomes de campos das entidades (`Bilhete`, `Voo`, `Escala`, `Passageiro`) ou do enum `SituacaoBilhete`, ajustar `AnalyticsConsultasJpa` e `AnalyticsService.SITUACOES_RECEITA`.
