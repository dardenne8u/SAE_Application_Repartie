CREATE TABLE Restaurant
(
    idRest   varchar,
    nom      varchar,
    tel      varchar,
    adresse  varchar,
    codePost varchar,
    ville    varchar,
    PRIMARY KEY (idRest)
);

CREATE TABLE coordonnees
(
    idRest    varchar,
    latitude  double,
    longitude double,
    PRIMARY KEY (idRest)
);