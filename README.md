# ✈️ GH Viagens

**Sistema Web de Gestão de Voos e Operações Aéreas**

![Java](https://img.shields.io/badge/Java-21-762334)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-176B4D)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791)
![Status](https://img.shields.io/badge/Status-MVP_Concluído-176B4D)

## 📖 Sobre o projeto

O GH Viagens é um sistema web desenvolvido para centralizar e simplificar a gestão de operações aéreas, permitindo o gerenciamento de empresas, cidades, aeroportos, passageiros, voos, escalas e bilhetes.

O sistema também conta com um módulo de Analytics, responsável por transformar os dados operacionais em indicadores e relatórios gerenciais, auxiliando na análise das operações e na tomada de decisões.

Desenvolvido como projeto acadêmico do curso de Bacharelado em Sistemas de Informação do Centro Universitário La Salle do Rio de Janeiro (UNILASALLE-RJ).

## 🎯 Objetivos

- Centralizar informações relacionadas às operações aéreas.
- Automatizar o gerenciamento de voos e bilhetes.
- Garantir a integridade dos dados por meio de regras de negócio.
- Disponibilizar indicadores e relatórios gerenciais.
- Aplicar conceitos de orientação a objetos, engenharia de software e desenvolvimento web em camadas.

## ⚙️ Funcionalidades

### Cadastros

- Cadastro e gerenciamento de empresas aéreas.
- Cadastro de cidades e unidades federativas.
- Cadastro de aeroportos.
- Cadastro e gerenciamento de passageiros.

### Voos e Escalas

- Cadastro de voos vinculados às empresas.
- Definição de itinerários e trechos.
- Gerenciamento da origem e do destino.
- Registro da duração e do valor de cada trecho.
- Cálculo automático do valor total do voo.
- Controle da situação dos voos.

### Bilhetes

- Emissão de bilhetes.
- Associação entre passageiro e voo.
- Registro do valor no momento da emissão.
- Consulta e cancelamento de bilhetes.
- Controle de utilização.
- Prevenção de bilhetes ativos duplicados.

### Analytics e Relatórios

- Receita total.
- Quantidade de bilhetes válidos e cancelados.
- Ticket médio.
- Receita por dia de emissão.
- Receita por empresa.
- Ranking dos voos mais procurados.
- Filtros por período.
- Relatório de passageiros por voo.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem de programação |
| Spring Boot 3.5 | Desenvolvimento da aplicação |
| Spring MVC | Arquitetura web |
| Spring Data JPA | Persistência de dados |
| Hibernate | Mapeamento objeto-relacional |
| PostgreSQL | Banco de dados relacional |
| Thymeleaf | Renderização das páginas |
| Bootstrap 5 | Interface responsiva |
| HTML, CSS e JavaScript | Desenvolvimento da interface |
| Chart.js | Visualização de dados |
| Flyway | Versionamento do banco de dados |
| Maven | Gerenciamento de dependências |
| JUnit | Testes automatizados |
| Git e GitHub | Controle de versão |
| GitHub Codespaces | Ambiente de desenvolvimento |

## 🏗️ Arquitetura

O sistema utiliza uma arquitetura em camadas, separando as responsabilidades da aplicação para facilitar sua manutenção e evolução.

**Interface → Controller → Service → Repository → Banco de Dados**

- **Model:** representação das entidades do sistema.
- **Controller:** recebimento das solicitações do usuário.
- **Service:** processamento das regras de negócio.
- **Repository:** comunicação com o banco de dados.
- **Templates:** apresentação das informações ao usuário.
- **Analytics:** processamento de indicadores e relatórios.

## 🗄️ Modelo de dados

O banco de dados é composto por sete entidades principais:

| Entidade | Responsabilidade |
|---|---|
| Empresa | Representa as companhias aéreas |
| Cidade | Identifica as localidades |
| Aeroporto | Representa os aeroportos |
| Voo | Armazena informações dos voos |
| Escala | Representa os trechos de um voo |
| Passageiro | Armazena os dados dos passageiros |
| Bilhete | Registra a emissão de passagens |

## 📋 Regras de negócio

- Cada voo possui um código único.
- A origem e o destino de um trecho devem ser diferentes.
- Os trechos devem respeitar a continuidade do itinerário.
- O preço total do voo corresponde à soma dos valores de seus trechos.
- A emissão de bilhetes exige um voo programado com pelo menos um trecho.
- O valor do bilhete é preservado após sua emissão.
- Um passageiro não pode possuir bilhetes ativos duplicados para o mesmo voo.
- Voos com bilhetes vinculados não podem ser excluídos.
- O cancelamento de um voo exige que não existam bilhetes emitidos ou utilizados vinculados a ele.
- Bilhetes cancelados não são considerados no cálculo da receita.

### Situações de voo

- Programado
- Em andamento
- Concluído
- Cancelado

### Situações de bilhete

- Emitido
- Utilizado
- Cancelado

## 🎨 Identidade visual

O GH Viagens possui uma interface administrativa inspirada nas cores do Fluminense, combinando elegância, organização e facilidade de navegação.

| Cor | Código hexadecimal |
|---|---|
| Grená | #762334 |
| Verde | #176B4D |
| Branco | #FFFFFF |
| Bege | #E8D8C1 |

## 🚀 Como executar

### GitHub Codespaces

1. Acesse o repositório no GitHub.
2. Clique em **Code**.
3. Selecione a opção **Codespaces**.
4. Crie ou abra um ambiente de desenvolvimento.
5. Aguarde a configuração do Java e do PostgreSQL.

No terminal, execute:

    mvn spring-boot:run

Abra a porta **8080** na aba **Ports**.

O Flyway será responsável pela criação das tabelas e pelo carregamento dos dados iniciais na primeira execução.

### Rotas principais

| Rota | Funcionalidade |
|---|---|
| /empresas | Gestão de empresas |
| /cidades | Gestão de cidades |
| /aeroportos | Gestão de aeroportos |
| /passageiros | Gestão de passageiros |
| /voos | Gestão de voos e escalas |
| /bilhetes | Gestão de bilhetes |
| /analytics | Dashboard gerencial |
| /analytics/passageiros-por-voo | Relatório de passageiros |

## 🧪 Testes

Para executar os testes automatizados:

    mvn clean test

Durante o desenvolvimento, foram realizados testes unitários do módulo Analytics e testes operacionais manuais, incluindo:

- Cadastro, edição e exclusão de registros.
- Criação de voos e inclusão de trechos.
- Emissão e cancelamento de bilhetes.
- Bloqueio de operações inválidas.
- Atualização dos indicadores gerenciais.

## 📚 Documentação complementar

- docs/analytics/README.md — Documentação do módulo Analytics.
- docs/interface/README.md — Documentação da interface visual.

## 📌 Status do projeto

**Versão 1.0 — MVP acadêmico implementado**

O projeto possui seus principais módulos operacionais implementados e integrados, incluindo banco de dados, regras de negócio, interface administrativa e relatórios gerenciais.

Trata-se de um protótipo acadêmico, não destinado à operação comercial de companhias aéreas.

## 👨‍💻 Desenvolvimento

Projeto acadêmico desenvolvido no curso de Sistemas de Informação — UNILASALLE-RJ.

**2026 — GH Viagens**

*Tecnologia conectando destinos.*
