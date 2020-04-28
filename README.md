# Fraktaaligeneraattori
Sovelluksen avulla on mahdollista luoda ja tarkastella Julia-fraktaaleja. Fraktaalin ominaisuuksia voi muokata sivupalkin liukuvalitsimilla ja piirtoalaa voi siirtää hiirellä vetämällä. Piirtoalueen voi tallentaa "Save"-painikkeella. Imaginääriluvun komponenttien ja iteraatioiden määrän valintoja voi peruuttaa "Undo"-painikkeella. "Reset"-painike palauttaa kaikki parametrit niiden alkuperäisiin asetuksiin.


## Dokumentaatio

[Vaatimusmäärittely](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Arkkitehtuuri](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Käyttöohje](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/kayttoohje.md)

[Työaikakirjanpito](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/Tyoaikakirjanpito.md)


## Releaset

[Viikko 5](https://github.com/tuomoart/ot-harjoitustyo/releases/tag/viikko5)
[Viikko 6](https://github.com/tuomoart/ot-harjoitustyo/releases/tag/viikko6)


## Komentorivitoiminnot

Sovelluksen voi suorittaa komennolla
`mvn compile exec:java -Dexec.mainClass=ui.UiMain`


### Testaus

Testaus suoritetaan komennolla
`mvn test`

Testikattavuusraportti luodaan komennolla
`mvn jacoco:report`

### Jar-paketti

Jar-paketti luodaan komennolla
`mvn package`

### Checkstyle

Checkstyle-tarkistukset voidaan suorittaa komennolla
`mvn jxr:jxr checkstyle:checkstyle`

### JavaDoc

JavaDocin voi luoda komennolla
`mvn javadoc:javadoc`
