CREATE TABLE IF NOT EXISTS locadora_de_discos_e_livros;
USE locadora_de_discos_e_livros;

CREATE TABLE Discos (
ID CHAR(32) PRIMARY KEY,
titulo VARCHAR(100),
criadoPor VARCHAR(100),
genero VARCHAR(100),
valor DOUBLE NOT NULL,
dataDeLancamento DATE,
qtdItens INT,
isDisco BOOLEAN,
duracao INT
);

CREATE TABLE Livros (
ID CHAR(32) PRIMARY KEY,
titulo VARCHAR(100),
criadoPor VARCHAR(100),
genero VARCHAR(100),
valor DOUBLE NOT NULL,
dataDeLancamento DATE,
qtdItens INT,
isDisco BOOLEAN,
qtdPaginas INT
);

CREATE TABLE tb_clientes (
cpf CHAR(14) PRIMARY KEY,
nome VARCHAR(100),
endereco VARCHAR(256)
);

CREATE TABLE Usuarios (
ID INT AUTO_INCREMENT PRIMARY KEY,
login VARCHAR(320),
senha VARCHAR(100),
nome VARCHAR(100)
);

CREATE TABLE tb_alugueis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_cpf CHAR(14) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim_prevista DATE NOT NULL,
    valor_base DOUBLE NOT NULL,
    valor_multa DOUBLE NOT NULL DEFAULT 0,

    FOREIGN KEY (cliente_cpf) REFERENCES tb_clientes(cpf) ON DELETE RESTRICT
);

CREATE TABLE tb_itens_aluguel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluguel_id INT NOT NULL,
    disco_id CHAR(32) NULL,
    livro_id CHAR(32) NULL,
    preco_diaria DOUBLE NOT NULL,
    dias_alugados INT NOT NULL,
    data_fim DATE NULL,

    FOREIGN KEY (aluguel_id) REFERENCES tb_alugueis(id) ON DELETE CASCADE,
    FOREIGN KEY (disco_id) REFERENCES Discos(ID) ON DELETE RESTRICT,
    FOREIGN KEY (livro_id) REFERENCES Livros(ID) ON DELETE RESTRICT,

    CONSTRAINT chk_item_acervo CHECK ((disco_id IS NOT NULL AND livro_id IS NULL) OR (disco_id IS NULL AND livro_id IS NOT NULL))
);
