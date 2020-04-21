# Fraktaaligeneraattori
Sovelluksen avulla on mahdollista luoda ja tarkastella Julia-fraktaaleja. Fraktaalin ominaisuuksia voi muokata sivupalkin liukuvalitsimilla ja piirtoalaa voi siirtää hiirellä vetämällä. Piirtoalueen voi tallentaa "Save"-painikkeella. Imaginääriluvun komponenttien ja iteraatioiden määrän valintoja voi peruuttaa "Undo"-painikkeella, mutta tämä painike ei vielä toimi kovinkaan luotettavasti. "Reset"-painike palauttaa kaikki parametrit niiden alkuperäisiin asetuksiin.

HUOM! JavaFX-versio on tällä hetkellä 11.0.2, sillä ohjeen mukainen 12.0.2 ei toiminut oikein yliopiston etätyöpyödällä tai melkillä.


## Dokumentaatio

[Vaatimusmäärittely](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Arkkitehtuuri](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Työaikakirjanpito](https://github.com/tuomoart/ot-harjoitustyo/blob/master/dokumentointi/Tyoaikakirjanpito.md)


## Komentorivitoiminnot
### Testaus

Sovelluksen voi suorittaa komennolla
`mvn compile exec:java -Dexec.mainClass=fractal.Main`

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
