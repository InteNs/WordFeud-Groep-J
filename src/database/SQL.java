package database;

public class SQL {
    public class SELECT {
        public static final String SELECTGAMESFORUSER = "select * from wordfeud.spel where account_naam_tegenstander = ? or account_naam_uitdager = ?;";
        public static final String SELECTMESSAGEFORGAME = "select * from chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String FINDUSER = "SELECT count(naam) AS 'Gebruikersnamen' FROM account WHERE naam=? AND wachtwoord=?";
    }
}
