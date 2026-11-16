-- Registros iniciais (dados fictícios; CNPJ e CPFs de exemplo)

INSERT INTO empresa (id_empresa, nome, cnpj) VALUES
    (1, 'Erlaine GHFlusão', '11222333000181');

INSERT INTO cidade (id_cidade, nome, uf) VALUES
    (1, 'Rio de Janeiro', 'RJ'),
    (2, 'Belo Horizonte', 'MG'),
    (3, 'Salvador', 'BA'),
    (4, 'São Paulo', 'SP'),
    (5, 'Brasília', 'DF');

INSERT INTO aeroporto (id_aeroporto, id_cidade, nome, sigla) VALUES
    (1, 1, 'Aeroporto Internacional do Galeão', 'GIG'),
    (2, 2, 'Aeroporto Internacional de Confins', 'CNF'),
    (3, 3, 'Aeroporto Internacional de Salvador', 'SSA'),
    (4, 4, 'Aeroporto de Congonhas', 'CGH'),
    (5, 5, 'Aeroporto Internacional de Brasília', 'BSB');

-- Voo.valor = soma dos valores das escalas
INSERT INTO voo (id_voo, id_empresa, codigo_voo, situacao, valor) VALUES
    (1, 1, 'GH101', 'PROGRAMADO', 550.00),
    (2, 1, 'GH202', 'PROGRAMADO', 660.00),
    (3, 1, 'GH303', 'CONCLUIDO',  280.00);

INSERT INTO escala (id_escala, id_voo, id_aeroporto_origem, id_aeroporto_destino, sequencial, tempo_escala, valor) VALUES
    (1, 1, 1, 2, 1,  60, 250.00),  -- GH101: GIG -> CNF
    (2, 1, 2, 3, 2, 110, 300.00),  -- GH101: CNF -> SSA
    (3, 2, 4, 5, 1, 105, 320.00),  -- GH202: CGH -> BSB
    (4, 2, 5, 1, 2, 120, 340.00),  -- GH202: BSB -> GIG
    (5, 3, 1, 4, 1,  55, 280.00);  -- GH303: GIG -> CGH

INSERT INTO passageiro (id_passageiro, nome, cpf) VALUES
    (1, 'Ana Souza',    '11144477735'),
    (2, 'Bruno Lima',   '52998224725'),
    (3, 'Carla Mendes', '39053344705');

INSERT INTO bilhete (id_bilhete, id_voo, id_passageiro, data_emissao, valor_total, situacao) VALUES
    (1, 1, 1, '2026-11-10 09:30:00', 550.00, 'EMITIDO'),
    (2, 1, 2, '2026-11-10 10:15:00', 550.00, 'CANCELADO'),
    (3, 2, 3, '2026-11-11 16:40:00', 660.00, 'EMITIDO'),
    (4, 3, 1, '2026-10-20 14:00:00', 280.00, 'UTILIZADO');

-- Reajusta as sequências, já que os ids foram informados manualmente
SELECT setval(pg_get_serial_sequence('empresa',    'id_empresa'),    (SELECT MAX(id_empresa)    FROM empresa));
SELECT setval(pg_get_serial_sequence('cidade',     'id_cidade'),     (SELECT MAX(id_cidade)     FROM cidade));
SELECT setval(pg_get_serial_sequence('aeroporto',  'id_aeroporto'),  (SELECT MAX(id_aeroporto)  FROM aeroporto));
SELECT setval(pg_get_serial_sequence('voo',        'id_voo'),        (SELECT MAX(id_voo)        FROM voo));
SELECT setval(pg_get_serial_sequence('escala',     'id_escala'),     (SELECT MAX(id_escala)     FROM escala));
SELECT setval(pg_get_serial_sequence('passageiro', 'id_passageiro'), (SELECT MAX(id_passageiro) FROM passageiro));
SELECT setval(pg_get_serial_sequence('bilhete',    'id_bilhete'),    (SELECT MAX(id_bilhete)    FROM bilhete));
