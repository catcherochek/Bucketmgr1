package com.catchersoft.bucketmgr.tools;

/**
 * Created by Клим on 09.09.2017.
 */

public class DBConstants {
    ///////////Данные о базе
    public static final String DATABASE_DBNAME = "maindb";
    public static final int DATABASE_VERSION = 18;
    //////выборки
    ///////////////////создание таблиц
    public static final String QUERY_TABLE_GOODS_CREATE = "CREATE TABLE \"goods\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"name\"  TEXT,\n" +
            "\"artname\"  TEXT,\n" +
            "\"img\"  TEXT\n" +
            ")";

    public static final String QUERY_TABLE_INV_CREATE = "CREATE TABLE \"inv\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"goods_id\"  INTEGER,\n" +
            "\"count\"  INTEGER,\n" +
            "\"sklad\"  INTEGER,\n" +
            "\"date\"  INTEGER DEFAULT (strftime('%Y-%m-%d %H:%M')),\n" +
            "CONSTRAINT \" goods_id\" FOREIGN KEY (\"goods_id\") REFERENCES \"goods\" (\"id\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ")";
    public static final String QUERY_TABLE_DEBITORS_CREATE = "CREATE TABLE debitors (\n" +
            "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                        NOT NULL\n" +
            "                        UNIQUE,\n" +
            "    name        TEXT    NOT NULL,\n" +
            "    description TEXT,\n" +
            "    image       TEXT\n" +
            ")";



    public static final String QUERY_TABLE_SKLAD_CREATE = "CREATE TABLE \"sklad\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"name\"  TEXT\n" +
            ");";
    public static final String QUERY_TABLE_SUPPLIERS_CREATE = "CREATE TABLE \"suppliers\" (\n" +
            "\"id\"  INTEGER NOT NULL,\n" +
            "\"name\"  TEXT,\n" +
            "PRIMARY KEY (\"id\")\n" +
            ")";

    public static final java.lang.String QUERY_TABLE_DEBITORS_TRANSACTIONS_CREATE = "CREATE TABLE debitors_transactions (\n" +
            "    id         INTEGER NOT NULL\n" +
            "                       PRIMARY KEY ASC AUTOINCREMENT\n" +
            "                       DEFAULT 1,\n" +
            "    debitor_id INTEGER REFERENCES debitors (id) ON DELETE CASCADE\n" +
            "                                                ON UPDATE CASCADE,\n" +
            "    goods_id   INTEGER REFERENCES goods (id),\n" +
            "    date       INTEGER DEFAULT (strftime('%Y-%m-%d %H:%M') ) \n" +
            "                       NOT NULL,\n" +
            "    status     INTEGER DEFAULT 0,\n" +
            "    count      INTEGER DEFAULT (0) \n" +
            "                       NOT NULL\n" +
            ")";
    public static final String QUERY_TABLE_TRANSACTIONS_CREATE = "CREATE TABLE \"transaction\" (\n" +
            "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                        NOT NULL\n" +
            "                        DEFAULT 1,\n" +
            "    goods_id    INTEGER NOT NULL,\n" +
            "    supplier_id INTEGER NOT NULL,\n" +
            "    count       REAL,\n" +
            "    date        INTEGER DEFAULT (strftime('%Y-%m-%d %H:%M') ),\n" +
            "    photo       TEXT,\n" +
            "    CONSTRAINT goods_ID FOREIGN KEY (\n" +
            "        goods_id\n" +
            "    )\n" +
            "    REFERENCES goods (id) ON DELETE CASCADE\n" +
            "                          ON UPDATE CASCADE,\n" +
            "    CONSTRAINT suppliers_ID FOREIGN KEY (\n" +
            "        supplier_id\n" +
            "    )\n" +
            "    REFERENCES suppliers (id) ON DELETE CASCADE\n" +
            "                              ON UPDATE CASCADE\n" +
            ")";
    //////Выборки - представления !!! Должны идти только после создания таблицы
    public static final String QUERY_VIEW_SELECT_TRANSACTIONS_CREATE = "CREATE VIEW select_transactions AS\n" +
            "    SELECT trans.id AS id,\n" +
            "           suppliers.name AS supplier,\n" +
            "           goods.name AS goods,\n" +
            "           trans.count AS count,\n" +
            "           trans.date AS date,\n" +
            "           trans.photo AS photo\n" +
            "      FROM [transaction] AS trans\n" +
            "           LEFT JOIN\n" +
            "           suppliers ON trans.supplier_id = suppliers.id\n" +
            "           LEFT JOIN\n" +
            "           goods ON trans.goods_id = goods.id";
    public static final String QUERY_VIEW_SELECT_TOTAL_REPORT_CREATE = "CREATE VIEW \"select_total_report\" AS \n" +
            "SELECT\n" +
            "Sum(trans.count) AS summ,\n" +
            "suppliers.name AS supplier,\n" +
            "goods.name AS goods\n" +
            "FROM\n" +
            "\"transaction\" AS trans\n" +
            "LEFT JOIN suppliers ON trans.supplier_id = suppliers.id\n" +
            "LEFT JOIN goods ON trans.goods_id = goods.id\n" +
            "GROUP BY\n" +
            "suppliers.id,goods.id";
    public static final String QUERY_VIEW_SELECT_DEBITORS_JOURNAL_CREATE = "CREATE VIEW select_debitors_journal AS\n" +
            "    SELECT d.id,\n" +
            "           debitors.name AS name,\n" +
            "           description AS descr,\n" +
            "           goods.name goods,\n" +
            "           goods.artname art,\n" +
            "           d.count AS count,\n" +
            "           d.date AS date,\n" +
            "           d.status AS status\n" +
            "      FROM debitors_transactions AS d\n" +
            "           LEFT JOIN\n" +
            "           debitors ON debitor_id = debitors.id\n" +
            "           LEFT JOIN\n" +
            "           goods ON goods.id = goods_id\n";

    public static final String QUERY_VIEW_SELECT_DEBITORS_REPORT_CREATE = "CREATE VIEW select_debitors_report AS\n" +
            "    SELECT *\n" +
            "      FROM (\n" +
            "               SELECT debitors.name,\n" +
            "                      debitors.description,\n" +
            "                      goods.name,\n" +
            "                      sum(d.count) AS summ\n" +
            "                 FROM debitors_transactions AS d\n" +
            "                      LEFT JOIN\n" +
            "                      debitors ON debitor_id = debitors.id\n" +
            "                      LEFT JOIN\n" +
            "                      goods ON goods.id = goods_id\n" +
            "                GROUP BY debitors.id,\n" +
            "                         goods.id\n" +
            "           )\n" +
            "           AS d\n" +
            "     WHERE d.summ != 0\n";
    /////выборки данные заполнение
    ////////////////////таблица transactions
    public static final String[] QUERIES_FILL_TRANSACTIONS = {"INSERT INTO \"transaction\" (id, goods_id, supplier_id, count, date) VALUES (1, 1, 2, 120, 3092017)",
            "INSERT INTO \"transaction\" (id, goods_id, supplier_id, count, date) VALUES (2, 1, 2, -20, '2017-09-03 11:15')",
            "INSERT INTO \"transaction\" (id, goods_id, supplier_id, count, date) VALUES (3, 2, 1, 20, '2017-09-03 11:37')",
            "INSERT INTO \"transaction\" (id, goods_id, supplier_id, count, date) VALUES (4, 1, 1, 100, '2017-09-03 11:37')"};

    public static final String[] QUERIES_FILL_SUPPLIERS = {"INSERT INTO suppliers (name) VALUES ('Тритон')",
            "INSERT INTO suppliers (name) VALUES ('Фирма1')",
            "INSERT INTO suppliers (name) VALUES ('Фирма2')",
            "INSERT INTO suppliers (name) VALUES ('Фирма3')",
            "INSERT INTO suppliers (name) VALUES ('Фирма4')",
            "INSERT INTO suppliers (name) VALUES ('Фирма5')"};
    public static final String[] QUERIES_FILL_GOODS = {"INSERT INTO goods (name, artname, img) VALUES ('Ведро 5л', '5л', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 10л', '10л', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 5л(Т)', '5л-трит', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 5л(К)', '5л-корея', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 5л(А)', '5л-айсб', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 10л(Т)', '10л-трит', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 10л(А)', '10л-айсб', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ящик  большой куриный', 'ящ.б.к.', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ящик маленький куриный', 'ящ.м.к.', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ящик колбасный', 'ящ.колб.', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ящик банановый', 'ящ.бан.', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ящик большой(3кг брутто)', 'ящ.(3кг)', NULL)",
            "INSERT INTO goods (name, artname, img) VALUES ('Ведро 3л', '3л', NULL);"};

    // id для динамического создания элементов формы

    public static int LAYOUT_ID_COSTUMERS = 1000000;
    public static int LAYOUT_ID_GOODS_LAST = 2000000;
    public static int LAYOUT_ID_GOODS = 2000000;
    public static int LAYOUT_ID_GOODS_OFFSET_FOR_NAME = 100000;
    public static int LAYOUT_ID_GOODS_OFFSET_FOR_ARTICUL = 200000;
    public static int LAYOUT_ID_DEBITORS=3000000;
}
