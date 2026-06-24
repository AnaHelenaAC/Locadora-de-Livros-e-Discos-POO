# Locadora de Livros e Discos — POO

A desktop rental management system for books and music discs as a university Object-Oriented Programming project (UFERSA).

---

## Table of Contents

- [Features](#features)
- [Architecture Overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Configuring the Database Connection](#configuring-the-database-connection)
- [Building and Running](#building-and-running)
- [Default Login](#default-login)
- [Project Structure](#project-structure)
- [Design Patterns Used](#design-patterns-used)
- [Known Issues / Notes](#known-issues--notes)

---

## Features

- **User authentication** with role-based access (Manager / Employee)
- **Book and disc catalogue** management (CRUD)
- **Client registration** (CRUD)
- **Rental workflow** — open a rental, add items, calculate daily rates and fines
- **Financial overview** screen
- **Employee / user management** (Manager only)
- CSV-style import of books and discs via file upload screens

---

## Architecture Overview

```
src/main/java/br/edu/ufersa/locadora/
├── Main.java                  ← Application entry point; configures DB connection
├── controllers/               ← JavaFX controllers (one per FXML view)
├── model/
│   ├── DAO/                   ← Data Access Objects + ConnectionFactory
│   ├── entities/              ← Domain entities (Livro, Disco, Cliente, Aluguel…)
│   ├── Service/               ← Business logic layer
│   ├── SessaoUsuario.java     ← Singleton holding the logged-in user & DB factory
│   └── EstoqueObserver.java   ← Observer interface for stock events
├── exceptions/                ← Custom checked exceptions
├── router/                    ← SceneRouter — centralised FXML navigation
└── util/                      ← ViewSwitcher helper
```

Views (FXML files) and CSS live in `src/main/resources/`.

The SQL script for creating the database is at:
```
src/main/resources/CriarDB.sql
```

---

## Prerequisites

| Requirement | Minimum version |
|---|---|
| Java JDK | 17 (tested; pom targets 14+) |
| Maven | 3.6+ |
| MySQL Server | 8.0+ |

> **JavaFX 21** is pulled in automatically by Maven — you do **not** need to install it separately.

---

## Database Setup

### 1. Start MySQL

Make sure your MySQL server is running locally on port **3306**.

### 2. Run the creation script

Open a MySQL client (command line, MySQL Workbench, DBeaver, etc.) and execute the file `src/main/resources/CriarDB.sql`:

```sql
-- From the MySQL CLI:
SOURCE /path/to/project/src/main/resources/CriarDB.sql;
```

Or paste the contents directly. The script will:

1. Create the database `locadora_de_discos_e_livros`
2. Create all tables (`tb_discos`, `tb_livros`, `tb_clientes`, `tb_usuarios`, `tb_alugueis`, `tb_itens_aluguel`)
3. Insert the default admin user (`login: admin`, `senha: senha123`)

### 3. Create the MySQL user (optional but recommended)

The application currently connects with the credentials hardcoded in `Main.java` (user `poo`, password `AH443162ah`). Create that user in MySQL, or change the credentials (see the next section).

```sql
CREATE USER 'poo'@'localhost' IDENTIFIED BY 'AH443162ah';
GRANT ALL PRIVILEGES ON locadora_de_discos_e_livros.* TO 'poo'@'localhost';
FLUSH PRIVILEGES;
```

---

## Configuring the Database Connection

The connection parameters live in **`Main.java`**:

```java
// src/main/java/br/edu/ufersa/locadora/Main.java
ConnectionFactory factory = new ConnectionFactoryMySQL(
        "jdbc:mysql://localhost:3306/locadora_de_discos_e_livros",
        "poo",           // ← MySQL username
        "AH443162ah"     // ← MySQL password
);
```

Change the URL, username, and password here to match your environment before running. There is also a `ConnectionFactoryPostgreSQL` class available if you prefer PostgreSQL — swap the factory and adjust the SQL script accordingly (the DDL uses MySQL-specific syntax like `AUTO_INCREMENT` and `BOOLEAN`).

---

## Building and Running

### Using Maven (recommended)

```bash
# Clone / extract the project
cd Locadora-de-Livros-e-Discos-POO

# Compile and run in one step
mvn javafx:run
```

### Using an IDE (IntelliJ IDEA, Eclipse, NetBeans)

1. Open the project as a **Maven project** (the IDE will import dependencies automatically).
2. Wait for Maven to download JavaFX 21 and the MySQL connector.
3. Run the `Main` class (`br.edu.ufersa.locadora.Main`).

> If the IDE complains about module-path, make sure it recognises the `module-info.java` at `src/main/java/module-info.java`.

---

## Default Login

After creating the database, use these credentials to log in:

| Field | Value |
|---|---|
| Login | `admin` |
| Password | `senha123` |

The default account has **Manager** privileges.

---

## Project Structure

```
Locadora-de-Livros-e-Discos-POO/
├── pom.xml                          Maven build file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/edu/ufersa/locadora/
│   │   │       ├── Main.java        Entry point
│   │   │       ├── controllers/     JavaFX controllers
│   │   │       ├── model/
│   │   │       │   ├── DAO/         Database access
│   │   │       │   ├── entities/    Domain model classes
│   │   │       │   └── Service/     Business services
│   │   │       ├── exceptions/      Custom exceptions
│   │   │       ├── router/          Scene navigation
│   │   │       └── util/
│   │   └── resources/
│   │       └── br/edu/ufersa/locadora/
│   │           ├── view/            FXML layouts
│   │           ├── css/             Stylesheets
│   │           ├── images/          UI icons
│   │           └── CriarDB.sql      ← Database creation script
│   └── test/
└── telas figma/                     UI design references (PNG)
```

---

## Design Patterns Used

| Pattern | Where |
|---|---|
| **DAO (Data Access Object)** | `model/DAO/` — one DAO per entity |
| **Factory Method** | `ConnectionFactory` / `ConnectionFactoryMySQL` / `ConnectionFactoryPostgreSQL` |
| **Singleton** | `SessaoUsuario` — single session object shared across the app |
| **Builder** | `Aluguel.Builder` |

---

## Known Issues / Notes

- Database credentials are **hardcoded** in `Main.java`. For production or team use, move them to a `.properties` file or environment variable.
- The SQL script uses MySQL dialect (`AUTO_INCREMENT`, `BOOLEAN`, `USE`). If switching to PostgreSQL, update the DDL to use `SERIAL`, `BOOLEAN`/`true`, and remove `USE`.
- The `MainTest.java` file is commented-out test code and is not executed — it can be ignored.
- The `target/` directory included in the zip contains compiled `.class` files; you can delete it and let Maven recompile cleanly with `mvn clean compile`.
