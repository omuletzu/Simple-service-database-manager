CREATE TABLE Persoana(							-- 01
	id INT,
    nume VARCHAR(50),
    email VARCHAR(50),
    adresa VARCHAR(50));
    
CREATE TABLE Deviz(
	id_d INT,
    data_introducere DATE,
    aparat VARCHAR(50),
    simptome VARCHAR(50), 
    defect VARCHAR(50),
    data_constatare DATE DEFAULT NULL,
    data_finalizare DATE DEFAULT NULL,
    durata INT,
    manopera_ora INT,
    total DECIMAL(8, 2) DEFAULT (durata * manopera_ora),						-- total e initial doar (durata * manopera_ora)
    id_client INT,
    id_depanator INT);
    
CREATE TABLE Piesa(
	id_p INT,
    descriere VARCHAR(50),
    fabricant VARCHAR(50),
    cantitate_stoc INT,
    pret_c INT);
    
CREATE TABLE Piesa_Deviz(
	id_d INT,
    id_p INT,
    cantitate INT,
    pret_r INT);
    
ALTER TABLE Persoana
ADD CONSTRAINT Persoana_id_pk PRIMARY KEY (id);

ALTER TABLE Deviz 
ADD CONSTRAINT Deviz_id_d_pk PRIMARY KEY (id_d),
ADD CONSTRAINT Deviz_id_client FOREIGN KEY (id_client) REFERENCES Persoana (id),
ADD CONSTRAINT Deviz_id_depanator FOREIGN KEY (id_depanator) REFERENCES Persoana (id);
    
ALTER TABLE Piesa
ADD CONSTRAINT Piesa_id_p_pk PRIMARY KEY (id_p);
    
ALTER TABLE Piesa_Deviz
ADD CONSTRAINT Piesa_Deviz_id_d FOREIGN KEY (id_d) REFERENCES Deviz (id_d),
ADD CONSTRAINT Piesa_Deviz_id_p FOREIGN KEY (id_p) REFERENCES Piesa (id_p),
ADD COLUMN sursa VARCHAR(50);

DELIMITER //
CREATE TRIGGER Deviz_total_trig
AFTER INSERT ON Piesa_Deviz
FOR EACH ROW
BEGIN
	UPDATE Deviz
    SET total = total + NEW.pret_r * NEW.cantitate
	WHERE id_d = NEW.id_d;
END //
DELIMITER ;

INSERT INTO Persoana (id, nume, email, adresa) VALUES								-- populare cu date
    (1, 'John Doe', 'john.doe@example.com', '123 Main St'),
    (2, 'Jane Smith', 'jane.smith@example.com', '456 Oak St'),
    (3, 'Depanator1', 'depanator1@example.com', '789 Pine St'),
    (4, 'Depanator2', 'depanator2@example.com', '345 Susi St');

INSERT INTO Deviz (id_d, data_introducere, aparat, simptome, defect, data_constatare, data_finalizare, durata, manopera_ora, id_client, id_depanator) VALUES
    (101, '2023-09-01', 'Aparat1', 'Symptoms1', 'Defect1', STR_TO_DATE('2023-09-01', '%Y-%m-%d'), STR_TO_DATE('2023-09-10', '%Y-%m-%d'), 9, 50, 1, 3),
    (102, '2023-09-02', 'Aparat2', 'Symptoms2', 'Defect2', STR_TO_DATE('2023-09-02', '%Y-%m-%d'), NULL, 8, 40, 2, 3),
    (103, '2023-09-03', 'Aparat3', 'Symptoms3', 'Defect3', STR_TO_DATE('2023-09-03', '%Y-%m-%d'), STR_TO_DATE('2023-09-03', '%Y-%m-%d'), 10, 60, 1, 3),
    (104, '2023-08-15', 'Aparat4', 'Symptoms4', 'Defect4', STR_TO_DATE('2023-08-20', '%Y-%m-%d'), NULL, 5, 30, 1, 3),
    (111, '2023-09-15', 'Aparat5', 'Symptoms5', 'Defect5', STR_TO_DATE('2023-09-20', '%Y-%m-%d'), NULL, 6, 31, 2, 4);

INSERT INTO Piesa (id_p, descriere, fabricant, cantitate_stoc, pret_c) VALUES
    (201, 'Piesa1', 'Fabricant1', 10, 20),
    (202, 'Piesa2', 'Fabricant2', 4, 30),
    (203, 'Piesa3', 'Fabricant3', 8, 25),
    (204, 'Piesa4', 'Fabricant4', 2, 22),
    (205, 'Piesa5', 'Fabricant5', 1, 31),
    (206, 'surub', 'Fabricant5', 1, 31);

INSERT INTO Piesa_Deviz (id_d, id_p, cantitate, pret_r) VALUES
    (101, 201, 2, 15),
    (101, 202, 1, 35),
    (102, 203, 3, 30),
    (103, 201, 1, 20),
    (103, 202, 2, 32),
    (102, 206, 1, 25),
    (111, 206, 1, 20);

ALTER TABLE Persoana													-- 02	A
ADD CONSTRAINT Persoana_email_ck CHECK (email LIKE '%@%');

ALTER TABLE Deviz														-- B
ADD CONSTRAINT Deviz_consec_data_ck CHECK (data_introducere <= data_constatare AND data_constatare <= data_finalizare);

SELECT * FROM Deviz 													-- 03	A
WHERE data_constatare IS NOT NULL AND data_finalizare > STR_TO_DATE('01-09-2023', '%d-%m-%Y')
ORDER BY data_introducere;

SELECT * FROM Piesa 													-- B
WHERE cantitate_stoc < 5
ORDER BY cantitate_stoc ASC, descriere DESC;

SELECT id_d, descriere, fabricant, pret_c, pret_r FROM Piesa 					-- 04	A
JOIN Piesa_Deviz ON Piesa.id_p = Piesa_Deviz.id_p AND pret_c > pret_r;

SELECT DISTINCT a.id_p, b.id_p FROM Piesa_Deviz a								-- B
JOIN Piesa_Deviz b ON a.id_d = b.id_d AND a.id_p < b.id_p
WHERE a.cantitate = b.cantitate;

SELECT * FROM Deviz															-- 05	A
WHERE id_d IN (SELECT id_d FROM Piesa_Deviz 
				WHERE id_p = (SELECT id_p FROM Piesa WHERE descriere = 'surub'));
                
SELECT descriere, fabricant FROM Piesa										-- B
WHERE id_p = (SELECT id_p FROM Piesa_Deviz
				WHERE pret_r >= ALL(SELECT pret_r FROM Piesa_Deviz));
                
SELECT nume, id, (SELECT COUNT(*) FROM deviz														-- 06	A
					WHERE id_depanator = id AND MONTH(data_finalizare) = 9) AS "Count" FROM persoana
JOIN deviz ON id = id_depanator
GROUP BY nume, id;

SELECT b.descriere, b.fabricant, SUM(a.cantitate) FROM Piesa_Deviz a				-- B
JOIN Piesa b ON a.id_p = b.id_p 
JOIN Deviz c ON a.id_d = c.id_d 
WHERE YEAR(c.data_finalizare) = 2023 AND MONTH(c.data_finalizare) = 9
GROUP BY b.descriere, b.fabricant;

INSERT INTO Persoana (id)													-- 07	A
VALUES (11);

INSERT INTO Persoana (id)
VALUES (17);

INSERT INTO Deviz (id_d, data_introducere, aparat, simptome, id_client, id_depanator)				
VALUES (123, STR_TO_DATE('30-09-2023', '%d-%m-%Y'), 'TV Samsung', 'imagine desincronizata', 11, 17);

DELETE FROM Piesa														-- B
WHERE id_p NOT IN (SELECT id_p FROM Piesa_Deviz);

SELECT * FROM Piesa;

UPDATE Deviz															-- C
SET total = total - total / 20.00
WHERE id_d = 111;

SELECT * FROM Deviz;

DELIMITER //
CREATE PROCEDURE ex8()
BEGIN
	CREATE TABLE IF NOT EXISTS Exceptii(
	id_d INT,
	id_p INT,
	cantitate INT,
	pret_r INT,
	natura VARCHAR(50),
	FOREIGN KEY (id_d) REFERENCES Deviz(id_d),
	FOREIGN KEY (id_p) REFERENCES Piesa(id_p));
        
	INSERT INTO Exceptii (id_d, id_p, cantitate, pret_r, natura)
	SELECT a.id_d, a.id_p, a.cantitate, a.pret_r, 'pret' FROM Piesa_Deviz a
	JOIN Piesa b ON a.id_p = b.id_p AND a.pret_r > b.pret_c;
                
	INSERT INTO Exceptii (id_d, id_p, cantitate, pret_r, natura)
	SELECT a.id_d, a.id_p, a.cantitate, a.pret_r, 'data' FROM Piesa_Deviz a
	JOIN Deviz b ON a.id_d = b.id_d
	WHERE b.data_constatare = b.data_finalizare;
END //
DELIMITER ;

CALL ex8();
SELECT * FROM Exceptii;  

DELIMITER //
CREATE TRIGGER ex9_a
AFTER INSERT ON Piesa_Deviz
FOR EACH ROW
BEGIN
	UPDATE Piesa
    SET cantitate_stoc = cantitate_stoc - NEW.cantitate
    WHERE id_p = NEW.id_p;
END //
DELIMITER ;

INSERT INTO Piesa_Deviz (id_d, id_p, cantitate, pret_r) VALUES 
	(101, 201, 2, 20),
    (102, 203, 3, 30);
SELECT * FROM Piesa;

CREATE VIEW PieseDeviz123 AS												-- B
SELECT data_introducere, aparat, simptome, defect, data_constatare,
data_finalizare, durata, manopera_ora, total, id_client,
a.nume as client, id_depanator, b.nume as depanator,
descriere, p.id_p, fabricant, cantitate_stoc, pret_c, cantitate, pret_r
FROM Persoana a, Persoana b, Deviz d, Piesa_Deviz c, Piesa p
WHERE d.id_d = 123 AND
a.id = d.id_client AND b.id = d.id_depanator AND
c.id_d = d.id_d AND p.id_p = c.id_p;

/*CREATE OR REPLACE TRIGGER InsteadOfPieseDeviz123				-- merge doar in apex
INSTEAD OF INSERT ON PieseDeviz123
FOR EACH ROW
BEGIN
    INSERT INTO Persoana (id, nume)
    VALUES (:NEW.id_client, :NEW.client);

    INSERT INTO Persoana (id, nume)
    VALUES (:NEW.id_depanator, :NEW.depanator);

    --INSERT INTO Piesa_Deviz (id_d, id_p, cantitate, pret_r)
    --VALUES (123, :NEW.id_p, :NEW.cantitate, :NEW.pret_r);

    INSERT INTO Piesa (id_p, descriere, fabricant, cantitate_stoc, pret_c)
    VALUES (:NEW.id_p, :NEW.descriere, :NEW.fabricant, :NEW.cantitate_stoc, :NEW.pret_c); -- Assuming initial values for cantitate_stoc and pret_c
END;
/
*/