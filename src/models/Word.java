package models;

import enumerations.*;

public class Word {
    private String word;
    private String owner;
    private String letterset;
    private WordStatus status;

    public Word(String word, String owner, String letterset, WordStatus status) {
        this.word = word;
        this.owner = owner;
        this.letterset = letterset;
        this.status = status;
    }

    public String getWord() {
        return word;
    }

    public String toString() {
        return word;
    }

    public String getOwner() {
        return owner;
    }

    public String getLetterset() {
        return letterset;
    }

    public WordStatus getStatus() {
        return status;
    }

    public void setStatus(WordStatus status) {
        this.status = status;
    }
}
