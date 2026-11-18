# Interface GH Viagens

Painel administrativo em Thymeleaf + Bootstrap 5, sem dependências novas além do CSS/JS próprios.
Nome exibido: **GH Viagens** (nomes técnicos internos — pacotes, banco, repositório — não foram alterados).

## Identidade visual
| Cor | Hex | Uso |
|---|---|---|
| Grená | `#762334` | Predominante: menu lateral, botões principais, títulos, gráficos |
| Verde | `#176B4D` | Complementar: situações positivas (concluído, utilizado, emitido), gráfico de voos |
| Branco | `#FFFFFF` | Cartões, tabelas, barra superior |
| Bege | `#E8D8C1` | Detalhes: bordas, cabeçalho de tabela, sublinhado dos títulos, ícone da marca |

Variáveis em `static/css/gh.css` (`--gh-grena`, `--gh-verde`, `--gh-bege`...). O Bootstrap foi reapontado para a paleta
(`--bs-primary` etc.), então `btn-primary`, `text-bg-primary`, links e foco de campos já saem na cor certa.

## Como funciona (baixo risco de integração)
Os fragments `head(titulo)` e `navbar` de `fragments/layout.html` **mantêm nome e assinatura**. Qualquer página que:
```html
<head th:replace="~{fragments/layout :: head('Titulo')}"></head>
<body>
<nav th:replace="~{fragments/layout :: navbar}"></nav>
<main> ... </main>
```
recebe automaticamente menu lateral, barra superior e estilo. `<main>` filho direto do `<body>` ganha a margem do menu,
mesmo com `class="container"` (templates antigos). Tabelas, botões, alertas e formulários do Bootstrap já herdam o novo visual.

## Componentes (`templates/fragments/componentes.html`)
Nenhum conhece entidades: recebem só textos, números ou URLs.

| Componente | Uso |
|---|---|
| `cabecalho(titulo, subtitulo, acaoUrl, acaoRotulo)` | Título da página + botão de ação (`null` omite) |
| `alertas` | Mostra os atributos `mensagem` (sucesso) e `erro` do modelo, quando existirem |
| `situacao(valor)` | Selo colorido. Reconhece PROGRAMADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO, EMITIDO, UTILIZADO; outro valor aparece cinza |
| `acoes(editarUrl, excluirUrl, visualizarUrl, confirmacao)` | Botões Ver/Editar/Excluir de uma linha; cada URL é opcional (`null` omite) |
| `linhaVazia(colunas, mensagem)` | Linha "sem registros" |
| `formAcoes(cancelarUrl)` | Botões Salvar / Cancelar |
| `kpi(rotulo, valor, nota, icone, tom)` | Cartão de indicador; `tom`: `'grena'`, `'verde'` ou `'bege'` |

Ícones: sprite SVG embutido no `navbar` (`i-grid`, `i-building`, `i-pin`, `i-plane`, `i-users`, `i-route`, `i-layers`,
`i-ticket`, `i-chart`, `i-wallet`, `i-plus`, `i-edit`, `i-trash`, `i-eye`, `i-ok`, `i-alert`, `i-xcircle`, `i-inbox`, `i-receipt`).

### Esqueleto para os módulos Voos, Escalas e Bilhetes
Lista:
```html
<main>
  <div th:replace="~{fragments/componentes :: cabecalho('Voos', 'Subtítulo', @{/URL-DO-NOVO}, 'Novo voo')}"></div>
  <div th:replace="~{fragments/componentes :: alertas}"></div>
  <div class="gh-card gh-table-card"><div class="gh-table-wrap">
    <table class="table table-striped table-hover align-middle">
      <thead><tr><th>...</th><th class="text-end">Ações</th></tr></thead>
      <tbody>
        <tr th:each="x : ${SUA_LISTA}">
          <td th:text="${x.campo}"></td>
          <td><span th:replace="~{fragments/componentes :: situacao(${x.situacao})}"></span></td>
          <td class="text-end"><div th:replace="~{fragments/componentes :: acoes(@{/.../editar}, @{/.../excluir}, @{/.../ver}, 'Excluir?')}"></div></td>
        </tr>
        <th:block th:if="${#lists.isEmpty(SUA_LISTA)}">
          <tr th:replace="~{fragments/componentes :: linhaVazia(3, 'Nenhum registro encontrado.')}"></tr>
        </th:block>
      </tbody>
    </table>
  </div></div>
</main>
```
Formulário: `<div class="gh-card gh-form-card"><form ...> campos com <div class="mb-3"><label class="form-label">...`
e `<div th:replace="~{fragments/componentes :: formAcoes(@{/URL-DA-LISTA})}"></div>` no fim.
Campos inválidos: `form-control is-invalid` + `<div class="invalid-feedback">`.

## Alterações em `fragments/layout.html` (arquivo compartilhado)
1. `head(titulo)`: título passa de `· GHFlusão` para `· GH Viagens`; **acrescentados** favicon embutido,
   `<link rel="stylesheet" th:href="@{/css/gh.css}">` e `<script th:src="@{/js/gh.js}" defer>`. O link do Bootstrap foi mantido.
2. `navbar`: a raiz deixou de ser `<nav class="navbar ...">` e passou a ser `<div>` com sprite de ícones, menu lateral
   (`<aside class="gh-sidebar">`), fundo para telas pequenas e barra superior. Entraram os itens **Dashboard** (`/analytics`)
   e **Passageiros por voo** (`/analytics/passageiros-por-voo`) além de Empresas, Cidades, Aeroportos e Passageiros.
3. Voos, Escalas e Bilhetes aparecem **desativados** ("Em breve", `href="#"`), sem assumir endpoints. Para ativar cada um,
   troque `href="#"` por `th:href="@{/endpoint-real}"` e remova a classe `gh-nav-disabled` e o `<span class="gh-soon">`
   (há um comentário com o exemplo no arquivo).
O destaque do item ativo é feito em `gh.js` pela URL atual (o caminho mais específico vence).

## Arquivos
Criados: `static/css/gh.css`, `static/js/gh.js`, `templates/fragments/componentes.html`, `docs/interface/README.md`.
Modificados: `templates/fragments/layout.html`; cadastros `empresa|cidade|aeroporto|passageiro` (`lista.html` e `form.html`);
`templates/analytics/resumo.html` e `passageiros-voo.html`. **Nenhum** Java, repository, migration ou `application.properties`.

## Preservado
`th:object`, `th:field`, `th:each`, `th:value`, `th:selected`, `th:errors`, nomes dos campos (`name="..."`), URLs de
listar/novo/editar/excluir, `th:inline="javascript"` e os atributos do modelo do Analytics. No Analytics, os botões de aba
(Resumo gerencial / Passageiros por voo) foram substituídos pelo menu lateral, que aponta para as mesmas rotas.
Textos como "Novo(a) cidade" viraram "Nova cidade"/"Novo aeroporto" (só o texto visível).

## Observações e limitações
- O nome da empresa cadastrada pela migration `V2` continua "Erlaine GHFlusão" (é **dado do banco**, não interface).
  Para exibir "GH Viagens" sem mexer na migration: `UPDATE empresa SET nome = 'GH Viagens' WHERE cnpj = '11222333000181';`
- `/` continua redirecionando para `/empresas` (`HomeController`, Java). O dashboard está em `/analytics`; para abri-lo na raiz,
  troque o redirect por `redirect:/analytics` (decisão da frente que cuida do Java).
- Bootstrap e Chart.js seguem por CDN (jsDelivr), como antes; é preciso internet na apresentação.
- A ação "Visualizar" existe no componente `acoes`, mas os quatro cadastros não têm página de detalhe e não a usam.
