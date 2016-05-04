package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String GAMESFORUSER = "SELECT * FROM spel WHERE account_naam_tegenstander = ? OR account_naam_uitdager = ?;";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String SELECTLETTERS = "SELECT * FROM wordfeud.lettertype WHERE letterset_code = ?";
        public static final String SELECTCOMPS = "SELECT omschrijving FROM competitie c JOIN deelnemer d ON d.competitie_id = c.id JOIN account a on a.naam = d.account_naam WHERE a.naam = ?";
    }

    public class ALL {
        public static final String USERS 		= "SELECT * FROM account;";
        public static final String GAMES 		= "SELECT * FROM spel;";
        public static final String COMPETITIONS = "Select * FROM competitie";
    }

    public class COUNT {
        public static final String USERSWITHCREDS =  "SELECT count(naam) FROM account WHERE naam = ? AND wachtwoord = ?";
        public static final String USERCOUNT 	  =  "SELECT count(naam) FROM account WHERE naam = ?";
    }
    
    public class INSERT {
    	public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
    }
}
