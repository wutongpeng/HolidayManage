CREATE TABLE PSJJR(
F_YEAR VARCHAR(4) DEFAULT ' ' NOT NULL,
F_MONTH VARCHAR(2) DEFAULT ' ' NOT NULL,
F_VDATE VARCHAR(8) DEFAULT ' ' NOT NULL,
F_JJR VARCHAR(1) DEFAULT ' ' NOT NULL 
); 
CREATE UNIQUE INDEX PSJJR ON PSJJR (F_VDATE);