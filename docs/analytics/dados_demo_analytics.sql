-- DADOS DE DEMONSTRAÇÃO PARA O MÓDULO ANALYTICS (opcional)
--
-- NÃO é uma migration do Flyway: execute manualmente, uma única vez, para deixar
-- os gráficos e rankings mais interessantes na apresentação. Pressupõe o banco
-- com V1 e V2 aplicadas (voos GH101, GH202, GH303 e passageiros iniciais).
-- Dados fictícios; valor_total = valor do voo no momento da emissão.
--
-- Uso (no terminal do Codespace, com o PostgreSQL do devcontainer):
--   PGPASSWORD=ghflusao psql -h localhost -U ghflusao -d ghflusao -f docs/analytics/dados_demo_analytics.sql

INSERT INTO passageiro (nome, cpf) VALUES
    ('Daniel Rocha',    '12345678909'),
    ('Elisa Martins',   '23456789173'),
    ('Fábio Nogueira',  '34567891228'),
    ('Giovana Prado',   '45678912364'),
    ('Henrique Dias',   '56789123482'),
    ('Isabela Torres',  '67891234582');

INSERT INTO bilhete (id_voo, id_passageiro, data_emissao, valor_total, situacao)
SELECT v.id_voo, p.id_passageiro, b.data_emissao::timestamp, v.valor, b.situacao
FROM (VALUES
    -- GH202: o voo mais procurado
    ('GH202', '12345678909', '2026-11-09 08:20:00', 'EMITIDO'),
    ('GH202', '23456789173', '2026-11-09 11:05:00', 'EMITIDO'),
    ('GH202', '34567891228', '2026-11-10 14:30:00', 'EMITIDO'),
    ('GH202', '45678912364', '2026-11-12 09:45:00', 'EMITIDO'),
    ('GH202', '56789123482', '2026-11-13 17:10:00', 'CANCELADO'),
    -- GH101
    ('GH101', '67891234582', '2026-11-11 10:00:00', 'EMITIDO'),
    ('GH101', '12345678909', '2026-11-12 15:20:00', 'EMITIDO'),
    ('GH101', '45678912364', '2026-11-13 12:00:00', 'CANCELADO'),
    -- GH303 (voo já concluído)
    ('GH303', '23456789173', '2026-10-18 09:00:00', 'UTILIZADO'),
    ('GH303', '34567891228', '2026-10-19 16:40:00', 'UTILIZADO')
) AS b (codigo_voo, cpf, data_emissao, situacao)
JOIN voo v ON v.codigo_voo = b.codigo_voo
JOIN passageiro p ON p.cpf = b.cpf;
