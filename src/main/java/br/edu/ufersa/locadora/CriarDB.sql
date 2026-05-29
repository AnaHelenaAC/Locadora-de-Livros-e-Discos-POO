CREATE TABLE Discos (
ID BINARY(16) PRIMARY KEY,
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
ID BINARY(16) PRIMARY KEY,
titulo VARCHAR(100),
criadoPor VARCHAR(100),
genero VARCHAR(100),
valor DOUBLE NOT NULL,
dataDeLancamento DATE,
qtdItens INT,
isDisco BOOLEAN,
qtdPaginas INT
);

CREATE TABLE Clientes (
ID BINARY(16) PRIMARY KEY,
nome VARCHAR(100),
endereco VARCHAR(256),
email VARCHAR(320),
telefone VARCHAR(32),
cpf CHAR(11)
);

CREATE TABLE Usuarios (
ID BINARY(16) PRIMARY KEY,
email VARCHAR(320),
senha VARCHAR(100),
nome VARCHAR(100),
);
