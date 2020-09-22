-- impl. of a spring user which have specific privileges

create user "spring-user" with encrypted password 'spring-user-pass';
grant connect on database "passion-cuisine" to "spring-user";
grant select on
    public.admin,
    public.category,
    public.client,
    public.location,
    public.organizer,
    public.tr_client_workshop,
    public.workshop,
    public.workshop_draft,
    public.restricted_workshop
    to "spring-user";

grant insert, update, delete on
    public.client,
    public.tr_client_workshop,
    public.workshop,
    public.workshop_draft
    to "spring-user";

grant trigger on
    public.admin,
    public.client,
    public.tr_client_workshop,
    public.workshop,
    public.workshop_draft
    to "spring-user";

grant execute on function
    crypt(text, text),
    gen_salt(text),
    gen_salt(text,integer),
    emailsuniques(),
    workshopkeysuniques()
    to "spring-user";

grant execute on function
    drafttopublished(varchar,varchar,varchar,varchar,varchar,varchar,smallint,smallint,varchar,varchar,smallint,varchar,varchar),
    duplicate(varchar,date, varchar, date),
    duplicatedraft(varchar,date, varchar, date),
    editworkshop(varchar,varchar,varchar,varchar,varchar,varchar,varchar,varchar,smallint,smallint,varchar,varchar,smallint,varchar,varchar),
    editworkshopdraft(varchar,varchar,varchar,varchar,varchar,varchar,varchar,varchar,smallint,smallint,varchar,varchar,smallint,varchar,varchar),
    manageworkshopstate(),
    publishedtodraft(varchar,varchar,varchar,varchar,varchar,varchar,smallint,smallint,varchar,varchar,smallint,varchar,varchar)
    to "spring-user";

grant usage on type workshop_state to "spring-user";
