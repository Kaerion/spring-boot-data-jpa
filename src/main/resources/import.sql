INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Gandalf', 'El Gris', 'gandalf@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Aragorn', 'Hijo de Arazorn', 'aragorn@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Gimly', 'Hijo de Gloyn', 'gimly@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Legolas', 'Del Bosque', 'legolas@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Frodo', 'Bolson', 'frodo@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Sam', 'Sagaz', 'sam@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Merry', 'Brandigamo', 'merry@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Pipin', 'Tuck', 'pipin@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Pepe', 'El Gris', 'email1@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Paco', 'Hijo de Arazorn', 'email2@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Jose', 'Hijo de Gloyn', 'email3@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Jhon', 'Del Bosque', 'email4@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Federico', 'Bolson', 'email5@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Diana', 'Sagaz', 'email6@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Antonio', 'Brandigamo', 'email7@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Pedro', 'Tuck', 'email8@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Pedro', 'Bolson', 'email15@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Manolo', 'Sagaz', 'email16@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Marcos', 'Brandigamo', 'email17@latierramedia.com', NOW(), '');
INSERT into clientes (nombre, apellido, email, create_at, foto) values ('Javi', 'Tuck', 'email18@latierramedia.com', NOW(), '');

INSERT into productos (nombre, precio, create_at) values ('LG Monitor LCD 24 pulgadas', 250, NOW());
INSERT into productos (nombre, precio, create_at) values ('Camara digital Sony DSC-W3401', 150, NOW());
INSERT into productos (nombre, precio, create_at) values ('Apple Watch 4', 400, NOW());
INSERT into productos (nombre, precio, create_at) values ('Sony Vaio notebook', 700, NOW());
INSERT into productos (nombre, precio, create_at) values ('Mica comoda 5 cajones', 50, NOW());
INSERT into productos (nombre, precio, create_at) values ('Bianchi Bicicleta Aro 26', 320, NOW());
INSERT into productos (nombre, precio, create_at) values ('Impresora Packard Bell G841G', 250, NOW());

INSERT INTO users (username, password, enabled) values ('admin', '$2a$10$PKXE6mav7Rf/2HNcHRpkpecH2gWGaCnulFjZQS/bOauMmDZ71F2Ga', 1);
INSERT INTO users (username, password, enabled) values ('user', '$2a$10$2FzjpDI2wiZK6C6ZJYZm7.uAVqnWZDXnPMXFp6kMmNEl6/iuOJLoK', 1);

INSERT INTO authorities (user_id, authority) values (1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) values (1, 'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) values (2, 'ROLE_USER');