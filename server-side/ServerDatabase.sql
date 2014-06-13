PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE LOGGING(
SENSOR_CODE VARCHAR(20),
QUANTITY VARCHAR(45),
PIN_NO CHARACETR(5) NOT NULL,
IS_LOGGING BOOLEAN NOT NULL,
PRIMARY KEY(PIN_NO));
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_18',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_20',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_22',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_26',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_11',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_38',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_40',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_37',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_33',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_36',0);
INSERT INTO "LOGGING" VALUES(NULL,NULL,'P9_35',0);
COMMIT;
