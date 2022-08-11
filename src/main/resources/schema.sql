create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER               not null,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER               not null,
    MPA_NAME CHARACTER VARYING(50) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(50)  not null,
    MPA_ID       INTEGER                not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create unique index IF NOT EXISTS FILM_NAME
    on FILMS (FILM_NAME);

create table if not exists FILMS_GENRES
(
    FILM_ID  INTEGER REFERENCES films (film_id) ON DELETE CASCADE not null,
    GENRE_ID INTEGER REFERENCES GENRES (genre_id)  ON DELETE CASCADE not null,
    constraint FILMS_GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILMS_GENRES_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

create unique index if not exists FILMS_GENRES_FILM_ID_GENRE_ID_UINDEX
    on FILMS_GENRES (FILM_ID, GENRE_ID);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    LOGIN     CHARACTER VARYING(50) not null,
    EMAIL     CHARACTER VARYING(50) not null,
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE,
    constraint USER_ID
        primary key (USER_ID)
);

create unique index IF NOT EXISTS USERS_EMAIL_UNQ
    on USERS (EMAIL);

create unique index IF NOT EXISTS USERS_LOGIN_UNQ
    on USERS (LOGIN);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create unique index IF NOT EXISTS LIKES_FILM_ID_USER_ID_UINDEX
    on LIKES (FILM_ID, USER_ID);

create table IF NOT EXISTS USERS_FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint USERS_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint USERS_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create unique index if not exists USERS_FRIENDS_USER_ID_FRIEND_ID_UINDEX
    on USERS_FRIENDS (USER_ID, FRIEND_ID);
