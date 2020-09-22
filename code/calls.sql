-- exemple de CALL qui ne fonctionne plus (order params + missed params) :

CALL public.editworkshop(
	cast('Tortellini' as varchar), 
	cast('2020-07-21' as date),
	cast('Tortellini végétarienne' as varchar), 
	cast('2020-07-21 10:10:10' as timestamp), 
	cast('2020-07-19 10:10:10' as timestamp), 
	cast('Rue 42' as varchar),
	cast('<foo>description de tortellini</foo>' as xml), 
	cast('<ingredients xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="ingredients_validator.xsd"><ingredient count="3" unit="kg">Pates</ingredient></ingredients>' as xml), 
	cast(11 as smallint), 
	cast(4 as smallint), 
	cast('Cuisine italienne' as varchar),
	cast('bob@mail.ch' as varchar),
	cast(1700 as smallint),
	cast('Fribourg' as varchar)
)
