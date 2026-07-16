CREATE TABLE usuario (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         fuso_horario VARCHAR(50) NOT NULL DEFAULT 'America/Sao_Paulo',
                         horario_reset TIME NOT NULL DEFAULT '00:00',
                         horario_notificacao TIME,
                         fcm_token VARCHAR(255)
);

CREATE TABLE habito (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        titulo VARCHAR(100) NOT NULL,
                        descricao VARCHAR(255),
                        ativo BOOLEAN NOT NULL DEFAULT TRUE,
                        usuario_id UUID NOT NULL,
                        CONSTRAINT fk_habito_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE registro_habito (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 habito_id UUID NOT NULL,
                                 data DATE NOT NULL,
                                 concluido BOOLEAN NOT NULL DEFAULT FALSE,
                                 CONSTRAINT fk_registro_habito FOREIGN KEY (habito_id) REFERENCES habito(id),
                                 CONSTRAINT uk_habito_data UNIQUE (habito_id, data)
);
