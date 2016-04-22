# WordFeud
Informatie over alles wat met de applicatie te maken heeft.

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
2. Maak een nieuwe **branch**:
```bash
git fb name-of-branch
```
3. Schrijf je java **code**.
4. **Stage** je veranderingen:
```bash
git ap
```
5. **Commit** de staged files:
```bash
git c 'verandering in engels tegenwoordige tijd'
```
6. **Push** de commit naar github:
```bash
git push
```
7. Ga naar github, naar de branch die je hebt aangemaakt, en maak een **Pull Request** aan.