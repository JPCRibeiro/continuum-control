-- ================================
-- AGENTES
-- ================================
INSERT INTO agentes (codigo, codinome, especialidade, situacao) VALUES
('k7m2p9x4', 'James Bond', 'Espionagem',           'EM_MISSAO'),
('3bq8vn1t', 'Loki',       'Viajante',             'EM_MISSAO'),
('w5zc0j6r', 'Ada',        'Criptoanalise',        'DISPONIVEL'),
('t1yh4ds8', 'Kyra',       'Arqueologia Temporal', 'DISPONIVEL'),
('9fnp2ea7', 'Dorian',     'Infiltracao',          'DISPONIVEL'),
('62uxlmq0', 'Vesper',     'Diplomacia',           'DISPONIVEL');

-- ================================
-- USUARIOS
-- ================================
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Operador',   'operador@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'OPERADOR'),
('Supervisor', 'supervisor@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'SUPERVISOR'),
('Admin',      'admin@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'ADMINISTRADOR');

INSERT INTO usuarios (nome, email, senha, perfil, agente_id) VALUES
('James Bond', 'bond@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'AGENTE',
(SELECT id FROM agentes WHERE codigo = 'k7m2p9x4')),
('Loki', 'loki@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'AGENTE',
(SELECT id FROM agentes WHERE codigo = '3bq8vn1t')),
('Ada', 'ada@continuum.control',
'$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'AGENTE',
(SELECT id FROM agentes WHERE codigo = 'w5zc0j6r'));

-- ================================
-- EVENTOS
-- ================================
INSERT INTO eventos (acontecimento, descricao, data, localizacao, importancia) VALUES
('Primeira transmissao de radio transatlantica',
'Marconi recebe o primeiro sinal de radio atravessando o Atlantico',
DATE '1901-12-12', 'Terra Nova, Canada', 'ALTA'),
('Publicacao da teoria da relatividade geral',
'Einstein apresenta as equacoes de campo a Academia Prussiana de Ciencias',
DATE '1915-11-25', 'Berlim, Alemanha', 'CRITICA'),
('Lancamento do Sputnik 1',
'Primeiro satelite artificial colocado em orbita terrestre',
DATE '1957-10-04', 'Baikonur, Cazaquistao', 'ALTA'),
('Queda do Muro de Berlim',
'Abertura das fronteiras entre Berlim Oriental e Ocidental',
DATE '1989-11-09', 'Berlim, Alemanha', 'CRITICA'),
('Assinatura da Magna Carta',
'Joao Sem Terra sela o documento imposto pelos baroes ingleses',
DATE '1215-06-15', 'Runnymede, Inglaterra', 'MODERADA');

-- ================================
-- ANOMALIAS
-- ================================
INSERT INTO anomalias (evento_id, divergencia, detectada_em, risco, situacao, origem, observacao) VALUES
((SELECT id FROM eventos WHERE acontecimento = 'Publicacao da teoria da relatividade geral'),
'Equacoes de campo publicadas com uma constante divergente',
TIMESTAMP '2026-10-02 09:14:00', 'CRITICO', 'CONFIRMADA',
'Interferencia nao autorizada', 'Divergencia confirmada por tres leituras independentes'),

((SELECT id FROM eventos WHERE acontecimento = 'Primeira transmissao de radio transatlantica'),
'Sinal recebido onze minutos antes do horario registrado',
TIMESTAMP '2026-10-11 17:40:00', 'ALTO', 'EM_CORRECAO',
'Origem nao determinada', 'Equipe de correcao em campo'),

((SELECT id FROM eventos WHERE acontecimento = 'Queda do Muro de Berlim'),
'Abertura das fronteiras registrada com doze horas de antecedencia',
TIMESTAMP '2026-10-20 08:05:00', 'ALTO', 'EM_CORRECAO',
'Consequencia de missao anterior', 'Relacionada ao incidente CC-0042'),

((SELECT id FROM eventos WHERE acontecimento = 'Lancamento do Sputnik 1'),
'Orbita inicial divergente em quatro graus',
TIMESTAMP '2026-09-18 11:20:00', 'MODERADO', 'ESTABILIZADA',
'Falha operacional', 'Corrigida em operacao anterior, mantida para consulta historica'),

((SELECT id FROM eventos WHERE acontecimento = 'Assinatura da Magna Carta'),
'Clausula 61 ausente em todas as copias conhecidas',
TIMESTAMP '2026-08-30 15:00:00', 'BAIXO', 'IRREVERSIVEL',
'Fenomeno temporal de origem desconhecida', 'Correcao considerada inviavel'),

((SELECT id FROM eventos WHERE acontecimento = 'Lancamento do Sputnik 1'),
'Sinal de telemetria interrompido doze horas antes do previsto',
TIMESTAMP '2026-10-25 14:10:00', 'MODERADO', 'CONFIRMADA',
'Origem nao determinada', 'Aguardando equipe de verificacao');

-- ================================
-- MISSOES
-- ================================
INSERT INTO missoes (codigo, objetivo, anomalia_id, destino_temporal, prioridade, situacao) VALUES
('a4td91kc', 'Restaurar o registro original da transmissao',
(SELECT id FROM anomalias WHERE divergencia = 'Sinal recebido onze minutos antes do horario registrado'),
TIMESTAMP '1901-12-12 12:30:00', 'ALTA', 'EM_EXECUCAO'),

('hx07bz5n', 'Realinhar o horario de abertura das fronteiras',
(SELECT id FROM anomalias WHERE divergencia = 'Abertura das fronteiras registrada com doze horas de antecedencia'),
TIMESTAMP '1989-11-09 18:00:00', 'NORMAL', 'EM_EXECUCAO'),

('p3vm68ws', 'Investigar a origem da constante divergente',
(SELECT id FROM anomalias WHERE divergencia = 'Equacoes de campo publicadas com uma constante divergente'),
TIMESTAMP '1915-11-20 10:00:00', 'EMERGENCIA', 'PLANEJADA'),

('r2kq85ft', 'Verificar a interrupcao da telemetria',
(SELECT id FROM anomalias WHERE divergencia = 'Sinal de telemetria interrompido doze horas antes do previsto'),
TIMESTAMP '1957-10-04 19:28:00', 'NORMAL', 'PLANEJADA');

-- ================================
-- DESIGNACOES
-- ================================
INSERT INTO missao_agentes (missao_id, agente_id) VALUES
((SELECT id FROM missoes WHERE codigo = 'a4td91kc'),
(SELECT id FROM agentes WHERE codigo = 'k7m2p9x4')),
((SELECT id FROM missoes WHERE codigo = 'hx07bz5n'),
(SELECT id FROM agentes WHERE codigo = '3bq8vn1t')),
((SELECT id FROM missoes WHERE codigo = 'r2kq85ft'),
(SELECT id FROM agentes WHERE codigo = 'w5zc0j6r')),
((SELECT id FROM missoes WHERE codigo = 'p3vm68ws'),
(SELECT id FROM agentes WHERE codigo = 't1yh4ds8'));