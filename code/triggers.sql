-- Functions and stuffs for triggers

-- email admin & client unique together
create function emailsUniques()
returns trigger
language plpgsql
as $$
begin
	-- check with admin
	if exists (select 1 from admin where "email" = new."email") then
		raise exception 'invalid email'; -- less verbose, more security

	-- check with client
	elsif exists (select 1 from client where "email" = new."email") then
		raise exception 'invalid email'; -- less verbose, more security
	end if;
	return new;
end;
$$;

create trigger emailsUniquesAdmin before insert on admin
	for each row
	-- when (old."email" is distinct from new."email") -- if "before update" required, create new trigger
	execute procedure emailsUniques();

create trigger emailsUniquesClient before insert on client
	for each row 
	-- when (old."email" is distinct from new."email") -- if "before update" required, create new trigger
	execute procedure emailsUniques();


-- title_wk + date_wk & title_wk_draft + date_wk_draft are unique together
create function workshopKeysUniques()
returns trigger
language plpgsql
as $$
begin
	-- check for insert
	if TG_OP = 'INSERT' then
		-- check with workshop
		if exists (select 1 from workshop where "title" = new."title" and "date" = new."date") then
			raise exception '% already exists in workshop', new."title";

		-- check with workshop_draft
		elsif exists (select 1 from workshop_draft where "title" = new."title" and "date" = new."date") then
			raise exception '% already exists in workshop_draft', new."title";
		end if;
	-- check for update
	elsif TG_OP = 'UPDATE' then
		-- check with workshop
		if (old."title" is distinct from new."title" and old."date" is distinct from new."date") then
	                if exists (select 1 from workshop where "title" = new."title" and "date" = new."date") then
                        	raise exception '% already exists in workshop', new."title";

                	-- check with workshop_draft
                	elsif exists (select 1 from workshop_draft where "title" = new."title" and "date" = new."date") then
        	                raise exception '% already exists in workshop_draft', new."title";
	                end if;
		end if;
	end if;
	return new;
end;
$$;

create trigger workshopKeysUniquesWK before insert or update on workshop
	for each row 
	execute procedure workshopKeysUniques();

create trigger workshopKeysUniquesWKDraft before insert or update on workshop_draft
	for each row 
	execute procedure workshopKeysUniques();


