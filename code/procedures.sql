-- Liste des procedures requises :

create or replace function duplicateDraft (k1 varchar, k2 date, v1 varchar, v2 date)
returns int
language plpgsql
as $$
begin
        -- 1) check workshop exist
        if not exists (select 1 from workshop_draft where "title" = k1 and "date" = k2) then
                -- close function
                raise exception '% already exists in workshop_draft', k1;
		return 1;
        end if;
         -- 2) insert new workshop_draft with select duplicated data
        insert into workshop_draft ("title", "date", "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image")
        select
               v1,v2, "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image"
        from workshop_draft
        where
               "title" = k1 and "date" = k2;
        -- 3) commit
        return 0;
end;
$$;


create or replace function duplicate (k1 varchar, k2 date, v1 varchar, v2 date)
returns int
language plpgsql
as $$
begin
	-- 1) check workshop exist
	if not exists (select 1 from workshop where "title" = k1 and "date" = k2) then
        	-- close function
                raise exception '% already exists in workshop', k1;
		return 1;
	end if;
	 -- 2) insert new workshop_draft with select duplicated data
        insert into workshop_draft ("title", "date", "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image")
        select
               v1,v2, "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image"
        from workshop
        where
               "title" = k1 and "date" = k2;
	-- 3) commit
	return 0;
end;
$$;


create or replace function manageWorkshopState ()
returns int
language plpgsql
as $$
declare
	w workshop%rowtype;
begin
	-- 1) change from published to closed if possible
	update workshop
	set state = 'closed'
	where 
		"date" < current_timestamp and "min_participants" <= (select count(*) from tr_client_workshop where "date_workshop" = "date" and "title_workshop" = "title");
	-- 2) change from published to draft if possible
	-- loop on workshop and check not enough participants + date in the past
	for w in select * from workshop where "date" < current_timestamp and "min_participants" > (select count(*) from tr_client_workshop where "date_workshop" = "date" and "title_workshop" = "title") loop
		-- delete from workshop
		delete from workshop
		where
			"title" = w."title" and "date" = w."date";
		-- insert into workshop_draft
		insert into workshop_draft ("title", "date", "datetime_inscription", "street", "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "title_category", "email_organizer", "npa", "city", "image")
	        values (
        	        w."title",
			w."date",
			w."datetime_inscription",
			w."street",
			w."description_xhtml",
			w."ingredients_xml",
			w."max_participants",
			w."min_participants",
			w."title_category",
			w."email_organizer",
			w."npa",
			w."city",
			w."image"
                	);
	end loop;
	-- 3) commit
	return 0;
end;
$$;


create function publishedtodraft(v1 character varying, v2 character varying, v3 character varying, v4 character varying,
                                 v5 character varying, v6 character varying, v7 smallint, v8 smallint, v9 character varying, v10 character varying,
                                 v11 smallint, v12 character varying, v13 character varying) returns integer
    language plpgsql
as
$$
begin
    -- 1) check if no inscription
    if exists(select 1 from tr_client_workshop where "title_workshop" = v1 and "date_workshop" = cast(v2 as date)) then
        -- close function
        raise exception '% has inscritpions', v1;
        return 1;
    end if;
    -- 2) check lock_time and set it
    if (select "lock_time" from workshop where "title" = v1 and "date" = cast(v2 as date)) > current_timestamp then
        -- raise notice 'someone is working currently on this workshop';
        -- close function
        raise exception 'someone is working on %',v1;
        return 1;
    end if;
    -- 3) check state workshop not closed
    if (select "state" from workshop where "title" = v1 and "date" = cast(v2 as date)) = 'closed' then
        -- close function
        raise exception '% is in state closed',v1;
        return 1;
    end if;
    -- ELSE
    -- 4) delete workshop
    delete
    from workshop
    where "title" = v1
      and "date" = cast(v2 as date);
    -- 5) create workshop_draft
    insert into workshop_draft ("title", "date", "datetime_inscription", "street", "description_xhtml",
                                "ingredients_xml", "max_participants", "min_participants", "title_category",
                                "email_organizer", "npa", "city", "image")
    values (v1, cast(v2 as date), cast(v3 as timestamp), v4, cast(v5 as xml), cast(v6 as xml), v7, v8, v9, v10, v11, v12, cast(v13 as bytea));
    -- 6) commit
    return 0;
end;
$$;


create function drafttopublished(v1 character varying, v2 character varying, v3 character varying, v4 character varying,
                                 v5 character varying, v6 character varying, v7 smallint, v8 smallint, v9 character varying, v10 character varying,
                                 v11 smallint, v12 character varying, v13 character varying) returns integer
    language plpgsql
as
$$
begin
    -- 1) check lock_time and set it
    if (select "lock_time" from workshop where "title" = v1 and "date" = cast(v2 as date)) > current_timestamp then
        -- raise notice 'someone is working currently on this workshop';
        -- close function
        raise exception 'someone is working on %',v1;
        return 1;
    end if;
    -- ELSE
    -- 2) delete workshop_draft
    delete
    from workshop_draft
    where "title" = v1
      and "date" = cast(v2 as date);
    -- 3) create workshop state published (check not exist ??)
    -- don't forget datetime_publication
    -- don't forget workshop_state
    insert into workshop ("title", "date", "datetime_inscription", "datetime_publication", "street",
                          "description_xhtml", "ingredients_xml", "max_participants", "min_participants", "state",
                          "title_category", "email_organizer", "npa", "city", "image")
    values (v1, cast(v2 as date), cast(v3 as timestamp), current_timestamp, v4, cast(v5 as xml), cast(v6 as xml), v7, v8, 'published', v9, v10, v11, v12, cast(v13 as bytea));
    -- 4) commit
    return 0;
end;
$$;


create or replace function editWorkshop(k1 character varying, k2 character varying, v1 character varying,
                                  v2 character varying, v3 character varying, v4 character varying,
                                  v5 character varying, v6 character varying, v7 smallint, v8 smallint,
                                  v9 character varying, v10 character varying, v11 smallint, v12 character varying,
                                  v13 character varying) returns integer
language plpgsql
as $$
begin
	-- EDITION from published to draft
	
	-- 1) check lock_time and if in the past set it
        if (select "lock_time" from workshop where "title" = k1 and "date" = cast(k2 as date)) > current_timestamp then
                -- raise notice 'someone is working currently on this workshop';
                -- close function
                raise exception 'someone is working on %',v1;
		return 1;
        end if;
	-- 2) check state is not closed
	if (select "state" from workshop where "title" = k1 and "date" = cast(k2 as date)) = 'closed' then
		-- raise notice 'can not modify a closed workshop';
		-- close function
		raise exception '% is in state closed',v1;
		return 1;
	end if;
        -- ELSE
        -- 3) edit workshop
        update workshop_draft
            set "title"                = v1,
				"date"                 = cast(v2 as date),
				"datetime_inscription" = cast(v3 as timestamp),
				"street"               = v4,
				"description_xhtml"    = cast(v5 as xml),
				"ingredients_xml"      = cast(v6 as xml),
				"max_participants"     = v7,
				"min_participants"     = v8,
				"title_category"       = v9,
				"email_organizer"      = v10,
				"npa"                  = v11,
				"city"                 = v12,
				"image"                = cast(v13 as bytea)
        where "title" = k1 
		  and "date" = cast(k2 as date);
	-- 4) commit
        return 0;
end;
$$;


create function editworkshopdraft(k1 character varying, k2 character varying, v1 character varying,
                                  v2 character varying, v3 character varying, v4 character varying,
                                  v5 character varying, v6 character varying, v7 smallint, v8 smallint,
                                  v9 character varying, v10 character varying, v11 smallint, v12 character varying,
                                  v13 character varying) returns integer
    language plpgsql
as
$$
begin
    -- EDITION from draft to draft

    -- 1) check lock_time and if in the past set it
    if (select "lock_time" from workshop where "title" = k1 and "date" = cast(k2 as date)) > current_timestamp then
        -- raise notice 'someone is working currently on this workshop';
        -- close function
        raise exception 'someone is working on %',k1;
        return 1;
    end if;
    -- ELSE
    -- 2) edit workshop
    update workshop_draft
    set "title"                = v1,
        "date"                 = cast(v2 as date),
        "datetime_inscription" = cast(v3 as timestamp),
        "street"               = v4,
        "description_xhtml"    = cast(v5 as xml),
        "ingredients_xml"      = cast(v6 as xml),
        "max_participants"     = v7,
        "min_participants"     = v8,
        "title_category"       = v9,
        "email_organizer"      = v10,
        "npa"                  = v11,
        "city"                 = v12,
        "image"                = cast(v13 as bytea)
    where "title" = k1
      and "date" = cast(k2 as date);
    -- 3) commit
    return 0;
end;
$$;
