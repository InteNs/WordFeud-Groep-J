package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String GAMESFORUSER = "SELECT * FROM spel WHERE account_naam_tegenstander = ? OR account_naam_uitdager = ?;";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String SELECTLETTERS = "SELECT * FROM wordfeud.lettertype WHERE letterset_code = ?";
    }

    public class ALL {
        public static final String USERS = "SELECT * FROM account;";
        public static final String GAMES = "SELECT * FROM spel;";
    }

    public class COUNT {
        public static final String USERSWITHCREDS =  "SELECT count(naam) FROM account WHERE naam = ? AND wachtwoord = ?";
    }
}
