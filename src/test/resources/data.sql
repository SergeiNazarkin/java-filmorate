merge into MPA (MPA_ID, MPA_name)
    values (1, 'G');
merge into MPA (MPA_ID, MPA_name)
    values (2, 'PG');
merge into MPA (MPA_ID, MPA_name)
    values (3, 'PG-13');
merge into MPA (MPA_ID, MPA_name)
    values (4, 'R');
merge into MPA (MPA_ID, MPA_name)
    values (5, 'NC-17');

merge into GENRES (GENRE_ID, GENRE_NAME)
    values (1, 'Комедия');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (2, 'Драма');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (3, 'Мультфильм');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (4, 'Триллер');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (5, 'Документальный');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (6, 'Боевик');

insert into USERS (LOGIN, EMAIL, USER_NAME, BIRTHDAY)
values ('log', 'log@mail.ru', 'name', '1985-07-06');
insert into FILMS (FILM_NAME, MPA_ID, DESCRIPTION, RELEASE_DATE, DURATION)
values ('name', '1', 'description', '1985-07-06', '120');