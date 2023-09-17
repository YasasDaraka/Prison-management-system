CREATE DATABASE Prison;
USE Prison;


CREATE TABLE Admin(
                      UserName VARCHAR(15) NOT NULL,
                      Password VARCHAR(15) NOT NULL
);

INSERT INTO Admin (UserName, Password)
VALUES ('admin', '1234');

CREATE TABLE Building(
                         BuildNO VARCHAR(10) NOT NULL,
                         Cells INT(10),
                         Type VARCHAR(20) NOT NULL,
                         Level VARCHAR(20) NOT NULL,
                         CONSTRAINT PRIMARY KEY (BuildNO)

);

CREATE TABLE Cases(
                      CaseID VARCHAR(20) NOT NULL,
                      InmateID VARCHAR(15) NOT NULL,
                      Crime VARCHAR(20) NOT NULL,
                      Sentence VARCHAR(20) NOT NULL,
                      TimeStart VARCHAR(20) NOT NULL,
                      TimeEnd VARCHAR(20) NOT NULL,
                      CONSTRAINT PRIMARY KEY (CaseID)
);

CREATE TABLE Prisoner(
                         InmateID VARCHAR(15) NOT NULL,
                         InmateName VARCHAR(30) NOT NULL,
                         Gender VARCHAR(15) NOT NULL,
                         DOB  VARCHAR(20),
                         Marital VARCHAR(10),
                         Security VARCHAR(10),
                         BuildNO VARCHAR(10),
                         CONSTRAINT PRIMARY KEY (InmateID),
                         CONSTRAINT FOREIGN KEY (BuildNO) REFERENCES Building(BuildNO)
                             ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Emergency(
                          EmId VARCHAR(10) NOT NULL,
                          InmateID VARCHAR(15) NOT NULL,
                          Name VARCHAR(30),
                          Relation VARCHAR(15),
                          Contact INT(20),
                          CONSTRAINT PRIMARY KEY (EmId),
                          CONSTRAINT FOREIGN KEY (InmateID) REFERENCES Prisoner(InmateID)
                              ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE Jailer(
                       JailerID VARCHAR(15) NOT NULL,
                       JailerName VARCHAR(30) NOT NULL,
                       Gender VARCHAR(10) NOT NULL,
                       DOB VARCHAR(20),
                       Address VARCHAR(30),
                       NicNo VARCHAR(15) NOT NULL,
                       JailerRank VARCHAR(10) NOT NULL,
                       CONSTRAINT PRIMARY KEY (JailerID)
);

CREATE TABLE Schedule(
                         JailerID VARCHAR(15) NOT NULL,
                         BuildNO VARCHAR(10) NOT NULL,
                         Shift VARCHAR(10) NOT NULL,
                         Weapons VARCHAR(15),
                         CONSTRAINT FOREIGN KEY (JailerID) REFERENCES Jailer(JailerID)
                             ON UPDATE CASCADE ON DELETE CASCADE,
                         CONSTRAINT FOREIGN KEY (BuildNO) REFERENCES Building(BuildNO)
                             ON UPDATE CASCADE ON DELETE CASCADE

);

CREATE TABLE Visitor(
                        NicNo VARCHAR(15) NOT NULL,
                        InmateID VARCHAR(15) NOT NULL,
                        Name VARCHAR(30) NOT NULL,
                        Address VARCHAR(30) NOT NULL,
                        Gender VARCHAR(10) NOT NULL,
                        CONSTRAINT PRIMARY KEY (NicNo),
                        CONSTRAINT FOREIGN KEY (InmateID) REFERENCES Prisoner(InmateID)

);

CREATE TABLE Staff(
                      EmpID VARCHAR(10) NOT NULL,
                      Name VARCHAR(30) NOT NULL,
                      Gender VARCHAR(8) NOT NULL,
                      DOB DATE,
                      Address VARCHAR(30),
                      Nic VARCHAR(15) NOT NULL,
                      Pos VARCHAR(15) NOT NULL,
                      BuildNO VARCHAR(10),
                      CONSTRAINT PRIMARY KEY (EmpID),
                      CONSTRAINT FOREIGN KEY (BuildNO) REFERENCES Building(BuildNO)
                          ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE VisitorDetails(
                               InmateId VARCHAR(10),
                               NicNo VARCHAR(12),
                               Date DATE,
                               time VARCHAR(15),
                               CONSTRAINT FOREIGN KEY (InmateId) REFERENCES Prisoner(InmateId)
                                   ON UPDATE CASCADE ON DELETE CASCADE,
                               CONSTRAINT FOREIGN KEY (NicNo) REFERENCES Visitor(NicNo)
                                   ON UPDATE CASCADE ON DELETE CASCADE
);

