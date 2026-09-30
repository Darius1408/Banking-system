create table banci(
	id serial primary key,
	nume varchar(100) not null unique,
	adresa text not null,
	numar_telefon varchar(10) not null unique check(length(numar_telefon) = 10),
	email text unique check(email like '%_@__%.__%'),
	cod_banca varchar(11) not null unique,
	cui varchar(20) not null unique,
	nr_inregistrare varchar(30) not null unique
);

create table clienti(
	id serial primary key,
	tip_client varchar(20) not null check (tip_client in ('fizica', 'juridica')),
	nume varchar(150) not null,
	adresa text not null,
	numar_telefon varchar(10) not null check(length(numar_telefon) = 10)
);

create table persoane_fizice(
	client_id int primary key references clienti(id) on delete cascade,
	cnp varchar(13) unique not null check(length(cnp) = 13)
);

create table persoane_juridice(
	client_id int primary key references clienti(id) on delete cascade,
	cui varchar(20) unique not null,
	nr_inregistrare varchar(30) unique not null,
	reprezentant_legal varchar(150) not null
);

create table conturi_bancare(
	id serial primary key,
	banca_id int not null references banci(id) on delete restrict,
	client_id int not null references clienti(id) on delete restrict,
	iban varchar(24) unique not null check(length(iban) = 24),
	sold numeric(15, 2) not null default 0.00 check(sold >= 0),
	stare boolean not null default true,
	pin varchar(10) not null
);

create table tranzactii(
	id serial primary key,
	iban_sursa varchar(24) references conturi_bancare(iban),
	iban_destinatie varchar(24) references conturi_bancare(iban),
	suma numeric(15, 2) not null check(suma > 0),
	tip_tranzactie varchar(20) not null check(tip_tranzactie in ('depunere', 'retragere', 'transfer intrabancar', 'transfer interbancar')),
	stare varchar(20) not null default 'finalizat' check(stare in('in procesare', 'finalizat', 'respins')),
	data_tranzactie timestamp not null default current_timestamp,
	descriere text
);