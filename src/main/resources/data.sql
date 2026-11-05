INSERT INTO agentes (codigo, codinome, especialidade, situacao) VALUES
    ('agent001', 'James Bond', 'Espionagem', 'DISPONIVEL'),
    ('agent002', 'Loki', 'Viajante', 'DISPONIVEL')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
    ('Operador', 'operador@continuum.control',
     '$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'OPERADOR'),
    ('Supervisor', 'supervisor@continuum.control',
     '$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'SUPERVISOR'),
    ('Admin', 'admin@continuum.control',
     '$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.', 'ADMINISTRADOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO usuarios (nome, email, senha, perfil, agente_id) VALUES
    ('James Bond', 'bond@continuum.control', '$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.',
     'OPERADOR', (SELECT id FROM agentes WHERE codigo = 'agent001')),
    ('Loki', 'loki@continuum.control', '$2a$10$0t26/4kQCEAbaMv7ZI0d/.Ze8s1Il3OTuxQzA9E6rmvlGpZ7WaNS.',
     'OPERADOR', (SELECT id FROM agentes WHERE codigo = 'agent002'))
ON CONFLICT (email) DO NOTHING;