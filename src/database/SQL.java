package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String LETTERSFORGAME = "SELECT\n  letter.*,\n  lettertype.waarde\nFROM letter\n\n  JOIN lettertype\n    ON letter.lettertype_letterset_code = lettertype.letterset_code AND letter.lettertype_karakter = lettertype.karakter\nWHERE spel_id = ?";
        public static final String TURNSFORGAME = "SELECT\n  spel.id           spel,\n  beurt.id          beurt,\n  beurt.aktie_type,\n  beurt.score,\n  beurt.account_naam,\n  placed.letter_id  gelegd_id,\n  plankje.letter_id plank_id,\n  placed.blancoletterkarakter,\n  placed.tegel_x,\n  placed.tegel_y\nFROM spel\n  LEFT JOIN beurt ON beurt.spel_id = spel.id\n  LEFT JOIN gelegdeletter placed ON placed.beurt_id = beurt.id AND placed.spel_id = spel.id\n  LEFT JOIN letterbakjeletter plankje ON beurt.id = plankje.beurt_id AND beurt.spel_id = plankje.spel_id\n\nWHERE spel.id = ?;";
        public static final String TILESFORBOARD = "SELECT * FROM wordfeud.tegel WHERE bord_naam =?";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String USERWITHCREDS = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r ON a.naam = r.account_naam\nWHERE naam = ?\n      AND wachtwoord = ?";
        public static final String USERWITHNAME = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r ON a.naam = accountrol.account_naam\nWHERE a.naam = ?";
        public static final String COMPFOROWNER = "SELECT id FROM competitie WHERE account_naam_eigenaar = ?";
    }

    public class ALL {
        public static final String USERS = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r on a.naam = r.account_naam";
        public static final String GAMES = "SELECT spel.*, MAX(beurt.id) AS last_turn\n" + "FROM spel\nLEFT JOIN beurt ON spel.id = beurt.spel_id\nGROUP BY spel.id";
        public static final String COMPETITIONS = "SELECT C.id, C.omschrijving, C.account_naam_eigenaar, AVG(R.gemidddelde_score) AS gemidddelde_score FROM competitie AS C LEFT JOIN rank_avg AS R ON C.id = R.competitie_id GROUP BY C.id;";
        public static final String PLAYERSCOMPS = "SELECT * FROM deelnemer";
        public static final String WINSLOSES = "SELECT w.account_naam, wins,lost FROM rank_nr_wins w JOIN rank_nr_lost l ON w.account_naam = l.account_naam";
        public static final String WORDS = "SELECT * FROM woordenboek WHERE account_naam != 'bookowner'";
    }

    public class INSERT {
        public static final String INSERTTURN = "INSERT INTO wordfeud.beurt(id, spel_id, account_naam, score, aktie_type) VALUES (?,?,?,?,?);";
        public static final String INSERTPLACEDTILES = "INSERT INTO wordfeud.gelegdeletter(letter_id, spel_id, beurt_id, tegel_x, tegel_y, tegel_bord_naam, blancoletterkarakter) VALUES";
        public static final String INSERTRACKTILES = "INSERT INTO wordfeud.letterbakjeletter(spel_id, letter_id, beurt_id) VALUES";
        public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
        public static final String SETROLE = "INSERT INTO `wordfeud`.`accountrol` (`account_naam`, `rol_type`) VALUES (?, ?);";
        public static final String INSERTCOMPETITION = "INSERT INTO competitie (omschrijving, account_naam_eigenaar) VALUES (?, ?);";
        public static final String INSERTPLAYER = "INSERT INTO deelnemer (account_naam, competitie_id) VALUES (?, ?);";
        public static final String INSERTMESSAGE = "INSERT INTO wordfeud.chatregel (chatregel.spel_id,chatregel.account_naam,chatregel.bericht) VALUES (?,?,?);";
        public static final String CREATEGAME = "INSERT INTO spel (competitie_id, toestand_type, account_naam_uitdager, reaktie_type, bord_naam, letterset_naam, account_naam_tegenstander) VALUES (?, 'request', ?, 'unknown', 'standard', ?, ?);";
    }

    public class DELETE {
        public static final String REMOVEROLE = "DELETE FROM `wordfeud`.`accountrol` WHERE `accountrol`.`account_naam` = ? AND `accountrol`.`rol_type` = ? ";
    }

    public class UPDATE {
        public static final String UPDATEPASSWORD = "UPDATE account SET wachtwoord = ? WHERE naam = ?";
        public static final String UPDATEWORDSTATUS = "UPDATE woordenboek SET status = ? WHERE woord = ? AND account_naam = ?";
        public static final String UPDATEGAMESTATE = "UPDATE wordfeud.spel SET toestand_type = ? WHERE id = ?";
    }

    public class COUNT{
        public static final String COUNTWORDS = "SELECT COUNT(woordenboek.woord ) FROM woordenboek WHERE woord =? AND woordenboek.letterset_code=?;";
    }
}
