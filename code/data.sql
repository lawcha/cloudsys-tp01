-- inserts sample data in the database

insert into admin ("email", "password")
values
    ('jeanAdmin@cuisine.ch', crypt('jeanPass', gen_salt('bf'))),
    ('michelAdmin@cuisine.ch', crypt('michelPass', gen_salt('bf')));

insert into category ("title", "description")
values
    ('Entrée', 'C est une entrée'),
    ('Dessert', 'C est un dessert, surcré de préférence'),
    ('Cuisine française', 'Typique de la gastronomie en France'),
    ('Cuisine italienne', 'La nourriture qui se parle avec les doigts'),
    ('Cuisine libanaise', 'Spécialités du Liban'),
    ('Cuisine asiatique', 'Saveurs de l Asie'),
    ('Pâtes', 'Les pâtes dans toutes leur splendeur'),
    ('Salade', 'Parce que la salade c est important'),
    ('Pommes-de-terre', 'Ou aussi appelées patates'),
    ('Viande de boeuf', 'Tout le reste n est que garniture'),
    ('Viande de poulet', 'Parce que ça ne fait pas que cui-cui et des oeufs'),
    ('Viande de cheval', 'Le cheval ça nous régale'),
    ('Poisson', 'Pour faire des bulles hors de l eau'),
    ('Sushis', 'Restez zen, c est aussi ça la cuisine');

insert into client ("email", "password", "lastname", "firstname", "street", "phone", "npa", "city")
values
    ('michel.gourmand@mails.com', crypt('12345', gen_salt('bf')), 'Gourmand', 'Michel', 'Rue du Choco 3', '0263259878', 1723, 'Marly'),
    ('jean-b@mes-mails.lcu', crypt('jb123', gen_salt('bf')), 'Bonneau', 'Jean', 'Chemin d ici 21', '0789875465', 1005, 'Lausanne'),
    ('arnaud@email.com', crypt('ar1', gen_salt('bf')), 'Antoine', 'Arnaud', 'Route 1', '0791231212', 1580, 'Oleyres'),
    ('bidule@mail.ch', crypt('bi2', gen_salt('bf')), 'Bidule', 'Bernard', 'Route 2', '0781231212', 1595, 'Faoug'),
    ('charles@charle.com', crypt('charles3', gen_salt('bf')), 'Charles', 'Charly', 'Route 3', '0771232323', 1983, 'Lanna'),
    ('daniel@mail.com', crypt('dada32', gen_salt('bf')), 'Smith', 'Daniel', 'Route 4', '0219991122', 3365, 'Seeberg'),
    ('h.s@email.de', crypt('hs11hs12hs13', gen_salt('bf')), 'Sturli', 'Hans', 'Strasse 5', '0334567777', 3376, 'Berken'),
    ('hans-p@mails.ch', crypt('peter99', gen_salt('bf')), 'Peter', 'Hans', 'Strasse 6', '0989854343', 3376, 'Graben'),
    ('zimmi@emails.com', crypt('zimzim-32', gen_salt('bf')), 'Zimmerman', 'Hug', 'Strasse 7', '0689998855', 9320, 'Arbon'),
    ('karls_berg@my-bier.ic', crypt('bier0.8', gen_salt('bf')), 'Berg', 'Karls', 'Strasse 8', '0233334455', 9434, 'Au SG');

insert into organizer ("email", "street", "lastname", "firstname", "phone", "city", "npa")
values
    ('bob@mail.ch', 'Route de l Asie', 'Lecuisinier', 'Bob', '0789998787', 'Lausanne', 1004),
    ('jeanMichel@blabla.com', 'Place du Château', 'Michel', 'Jean', '0983245688', 'Fribourg', 1700);

insert into workshop ("title", "date", "datetime_inscription", "datetime_publication", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "state", "title_category", "email_organizer", "npa", "city", "image")
values
    ('Des lasagnes', '2020-07-21', '2020-07-20 10:10:10', '2020-07-19 10:10:10', 'Dans la rue 4', '<foo>description de lasagne</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="5" unit="kg">Pates</ingredient></ingredients>', 10, 5, 'published', 'Cuisine italienne', 'bob@mail.ch', 1700, 'Fribourg', ''),
    ('De la salade', '2020-07-21', '2020-07-20 10:10:10', '2020-07-19 10:10:10', 'Dany 21', '<foo>description de salade</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="1" unit="kg">Salade</ingredient></ingredients>', 10, 5, 'published', 'Salade', 'bob@mail.ch', 1006, 'Lausanne', ''),
    ('Atelier sushis',  '2019-07-21', '2019-07-20 10:10:10', '2019-07-19 10:10:10', 'Dans la rue 4', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="5" unit="kg">Saumon</ingredient></ingredients>', 5, 3, 'closed', 'Sushis', 'bob@mail.ch', 1700, 'Fribourg', ''),
    ('Atelier sushis',  '2020-07-21', '2020-07-20 10:10:10', '2020-07-19 10:10:10', 'Rue 21', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="5" unit="kg">Saumon</ingredient></ingredients>', 1, 1, 'published', 'Sushis', 'bob@mail.ch', 1700, 'Fribourg', ''),
    ('Inutile',  '2020-08-21', '2020-08-20 10:10:10', '2020-08-19 10:10:10', 'Ch. des places 21', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="1" unit="kg">Terre</ingredient></ingredients>', 10, 1, 'published', 'Entrée', 'jeanMichel@blabla.com', 1595, 'Faoug', ''),
    ('Glace à la vanille',  '2019-08-21', '2019-08-20 10:10:10', '2019-08-19 10:10:10', 'Strasse 4', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="3" unit="kg">Glace vanille</ingredient></ingredients>', 1, 1, 'closed', 'Dessert', 'bob@mail.ch', 3365, 'Seeberg', ''),
    ('De la salade verte', '2020-07-01', '2020-05-10 10:10:10', '2020-04-10 10:10:10', 'Labas 21', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="1" unit="kg">Salade</ingredient></ingredients>', 10, 5, 'published', 'Salade', 'bob@mail.ch', 1005, 'Lausanne', ''),
('Cheval sur ardoise', '2020-06-10', '2020-05-10 10:10:10', '2020-04-10 10:10:10', 'Strasse 21', '<foo>myxhtml</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="3" unit="kg">Cheval</ingredient></ingredients>', 7, 3, 'published', 'Viande de cheval', 'bob@mail.ch', 9434, 'Au SG', '');

insert into workshop_draft ("title", "date", "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image")
values
    ('Tortellini', '2020-07-21', '2020-07-19 10:10:10', 'Rue 21', '<foo>description de tortellini</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="3" unit="kg">Pates</ingredient></ingredients>', 11, 4, 'Cuisine italienne', 'bob@mail.ch', 1700, 'Fribourg', ''),
    ('Du pain en entrée', '2020-07-21', '2020-07-17 10:10:10', 'Ici', '<foo>description</foo>', '<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="1" unit="kg">Pain</ingredient></ingredients>', 1, 0, 'Entrée', 'bob@mail.ch', 1003, 'Lausanne', '');

insert into public.tr_client_workshop (email_client, title_workshop, date_workshop) 
values 
    ('michel.gourmand@mails.com', 'Des lasagnes', '2020-07-21'), 
    ('michel.gourmand@mails.com', 'De la salade', '2020-07-21'), 
    ('jean-b@mes-mails.lcu', 'De la salade', '2020-07-21'),
    ('h.s@email.de', 'Atelier sushis',  '2019-07-21'),
    ('bidule@mail.ch', 'Atelier sushis',  '2019-07-21'),
    ('zimmi@emails.com', 'Atelier sushis',  '2019-07-21'),
    ('zimmi@emails.com', 'Atelier sushis',  '2020-07-21'),
    ('karls_berg@my-bier.ic', 'Glace à la vanille', '2019-08-21'),
    ('michel.gourmand@mails.com', 'De la salade verte', '2020-07-01'),
    ('jean-b@mes-mails.lcu', 'De la salade verte', '2020-07-01'),
    ('hans-p@mails.ch', 'De la salade verte', '2020-07-01'),
    ('charles@charle.com', 'De la salade verte', '2020-07-01'),
    ('h.s@email.de', 'De la salade verte', '2020-07-01'),
    ('daniel@mail.com', 'Cheval sur ardoise', '2020-06-10'),
    ('jean-b@mes-mails.lcu', 'Cheval sur ardoise', '2020-06-10');
