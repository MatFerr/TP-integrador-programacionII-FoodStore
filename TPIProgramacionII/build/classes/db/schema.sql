/*CREATE DATABASE IF NOT EXISTS Food_Store*/

USE Food_Store;

CREATE TABLE categorias (
id bigint auto_increment primary key not null,
nombre varchar(50) not null,
descripcion varchar(50) not null,
eliminado boolean default false not null,
created_at timestamp default current_timestamp not null
);

CREATE TABLE productos(
id bigint auto_increment primary key not null,
nombre varchar(50) not null,
precio double not null,
descripcion varchar(50) not null,
stock int not null,
imagen varchar(50) not null,
disponible boolean default true,
eliminado boolean default false not null,
created_at timestamp default current_timestamp not null,
categoria_id bigint not null,
constraint categoria_fk foreign key (categoria_id) references categorias(id));

