# WordFeud

Informatie over alles wat met de applicatie te maken heeft.

## Database connection info

Server ip 	: 77.172.146.212
Server port : 3306
Username 	: wordfeud
Password 	: wordfeud01
Database 	: wordfeud

## Java FX

[documentatie](https://docs.oracle.com/javase/8/javase-clienttechnologies.htm)
Voor opdrachten: zie drive.

## Scene Builder

[download link](http://gluonhq.com/open-source/scene-builder/)

## Git

[tutorial ssh](https://help.github.com/articles/generating-an-ssh-key/)

[alias config voor commands](http://pastebin.com/CaAkZmDJ)
Alias plakken in `/Users/<naam>/.gitconfig`.

### GitFlow: (met alias)

Doe tussen de stappen veel controle of alles goed gaat:

```bash
git l               //lijst van commits
git s               //lijst van bestanden
```

Voor problemen en vragen naar Cedric of Mark.

1. Ga naar **master** branch, en haal de nieuwste versie op:

```bash
git co master
git pull
```

1. Maak een nieuwe **branch**:

```bash
git fb name-of-branch
```

1. Schrijf je java **code**.
2. **Stage** je veranderingen:

```bash
git ap
```

1. **Commit** de staged files:

```bash
git c 'verandering in engels tegenwoordige tijd'
```

1. **Push** de commit naar github:

```bash
git push
```

1. Ga naar github, naar de branch die je hebt aangemaakt, en maak een **Pull Request** aan.

