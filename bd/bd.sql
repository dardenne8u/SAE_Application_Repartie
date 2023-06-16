CREATE TABLE Restaurant
(
    idRest   varchar(100),
    nom      varchar(100),
    tel      varchar(100),
    adresse  varchar(100),
    codePost varchar(100),
    ville    varchar(100),
    PRIMARY KEY (idRest)
);

CREATE TABLE coordonnees
(
    idRest    varchar(100),
    latitude  double,
    longitude double,
    PRIMARY KEY (idRest)
);