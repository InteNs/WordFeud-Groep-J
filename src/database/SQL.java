package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String GAMESFORUSER = "SELECT * FROM spel WHERE account_naam_tegenstander = ? OR account_naam_uitdager = ?;";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String SELECTLETTERS = "SELECT * FROM wordfeud.lettertype WHERE letterset_code = ?";
        public static final String SELECTUSERINCOMP = "SELECT naam FROM account a JOIN deelnemer d ON a.naam = d.account_naam JOIN competitie c ON c.id = d.competitie_id WHERE c.id = ?";
        public static final String SELECTUSERROLES = "SELECT rol_type FROM accountrol WHERE account_naam = ?";

    }

    public class ALL {
        public static final String USERS = "SELECT * FROM account;";
        public static final String GAMES = "SELECT * FROM spel;";
        public static final String COMPETITIONS = "SELECT * FROM competitie";
        public static final String ROLES = "SELECT * FROM accountrol";
    }

    public class COUNT {
        public static final String USERSWITHCREDS = "SELECT count(naam) FROM account WHERE naam = ? AND wachtwoord = ?";
        public static final String USERCOUNT = "SELECT count(naam) FROM account WHERE naam = ?";
    }

    public class INSERT {
        public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
        public static final String  SETROLE = "INSERT INTO `wordfeud`.`accountrol` (`account_naam`, `rol_type`) VALUES (?, ?);";
        public static final String INSERTCOMPETITION = "INSERT INTO competitie (omschrijving, account_naam_eigenaar) VALUES (?, ?);";
        public static final String INSERTPLAYER = "INSERT INTO deelnemer (account_naam, competitie_id) VALUES (?, ?);";
    }
    
    public class DELETE {
        public static final String REMOVEROLE = "DELETE FROM `wordfeud`.`accountrol` WHERE `accountrol`.`account_naam` = ? AND `accountrol`.`rol_type` = ? ";
    }
    
    public class UPDATE {
        public static final String UPDATEPASSWORD = "UPDATE account SET wachtwoord = ? WHERE naam = ?";
                
    }
    
}
