# Erlaine GHFlusão

Sistema web de gestão de voos, escalas, aeroportos, passageiros e bilhetes.
Java 21 · Spring Boot (MVC) · Thymeleaf · Bootstrap 5 · PostgreSQL · JPA/Hibernate · Flyway · Maven.

## Como rodar (GitHub Codespaces)
1. Abra o repositório em um Codespace (ele sobe o Java 21 e o PostgreSQL pelo `.devcontainer`).
2. No terminal: `mvn spring-boot:run`
3. Abra a porta 8080 na aba **Ports**.

O Flyway cria as tabelas (`V1`) e carrega os dados iniciais (`V2`) na primeira execução.
Para recomeçar do zero: apague o banco (`DROP SCHEMA public CASCADE; CREATE SCHEMA public;`) e rode de novo.

## Estrutura
`model` (entidades) · `repository` · `service` (regras de negócio) · `controller` · `templates` (views).

## Status
- Pronto: esquema, dados iniciais, entidades, repositórios, CRUD de Empresa (modelo para os demais).
- Próximos: Cidade, Aeroporto, Passageiro → Voo com Escalas → Bilhetes → Relatórios.
