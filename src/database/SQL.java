package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String GAMESFORUSER = "SELECT * FROM spel WHERE account_naam_tegenstander = ? OR account_naam_uitdager = ?;";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String LETTERSFORLANG = "SELECT * FROM lettertype WHERE letterset_code = ?;";
        public static final String TURNSFORGAME = "SELECT b.*, g.woorddeel, g.`x-waarden`, g.`y-waarden`\n" + "FROM beurt b LEFT JOIN gelegd g ON g.beurt_id = b.id WHERE b.spel_id = ?;";
    }

    public class ALL {
        public static final String USERS = "SELECT * FROM account;";
        public static final String GAMES = "SELECT * FROM spel;";
    }

    public class COUNT {
        public static final String USERSWITHCREDS =  "SELECT count(naam) FROM account WHERE naam = ? AND wachtwoord = ?";
        public static final String USERCOUNT 	  =  "SELECT count(naam) FROM account WHERE naam = ?";
    }
    
    public class INSERT {
    	public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
    }
}
