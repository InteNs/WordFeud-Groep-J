Database class
===================
In deze readme worden de functies in de  database class verklaart en uitgelegd.
De database class is **static** en kan en moet dus **niet** geïnitialiseerd worden.
In alle voorbeelden zal de tabel 'account' worden gebruikt.

----------


Select() methode
-------------
er zijn 3 verschillende select methodes
 1. Select methode om een enkele kolom uit te lezen en deze terug te geven als String.
 2. Select methode om een een complete tabel uit de database te lezen en deze terug te geven als ResultSet.
 3. Select methode om om een meerdere records van een kolom terug te geven als ArrayList.
 4. Query methode om een willekeurige query uit te voeren en geeft het resultaat terug als een ResultSet

1 . Syntax
 ```
 Database.select(String);
 ```
 Voorbeeld
```
     String s;
	 s = Database.select("SELECT naam FROM account WHERE wachtwoord='les'");
	 System.out.println(s);
```
 output
 ```
 0lesley0
 ```
2 . Syntax
 ```
 Database.select(String, ResultSet);
 ```
  Voorbeeld
 ```
ResultSet rs = null;
rs = Database.select("SELECT * FROM account", rs);
while (rs.next()) {
		System.out.println(rs.getString("naam") + ":" + rs.getString("wachtwoord"));
}
rs.close();
 ```
 >**Let op**
 >Vergeet niet de Database.close(); methode aan te roepen na het gebruik van deze functie!

 output
 ```
 0lesley0:les
allrights:123
jager684:ger
marijntje42:mar
rikmeijer:rik
test-admin:123
test-moderator:123
test-observer:123
test-player:123'
 ```
 3.Syntax
 ```
 Database.select(String, ArrayList<String>);
 ```
 Voorbeeld
 ```  
ArrayList<String> list = new ArrayList<String>();
list = Database.select("SELECT * FROM account", list);
for (String s : list) {
	System.out.println(s);
}
 ```
 Output
  ```
  0lesley0
allrights
jager684
marijntje42
rikmeijer
test-admin
test-moderator
test-observer
test-player
  ```
close() methode
-------------
Deze methode sluit alle connecties met de database in de database class.
Deze hoef je alleen maar aan te roepen na de  **Database.select(String, ResultSet);** methode.

query() methode
-------------
Voor het uitvoeren van queries op de database (update en delete statements etc.)

Voorbeeld
```
Database.query("UPDATE account SET wachtwoord='les1' WHERE naam='0lesley0'");
```

