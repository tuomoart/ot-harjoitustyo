# Arkkitehtuuri

## Rakenne

Ohjelman rakenne käy ilmi allaolevasta pakkaus- ja luokkakaaviosta:

![](https://raw.githubusercontent.com/tuomoart/ot-harjoitustyo/master/dokumentointi/kuvat/luokkakaavio.png)


## Käyttöliittymä

Käyttöliittymä koostuu yhdestä päänäkymästä, minkä tukena se tarvittaessa käyttää virheviesti-ikkunoita sekä tiedoston tallennukseen käyttöjärjestelmän tarjoamaa tiedostoselainta.

Käyttöliittymä on rakennettu ohjelmallisesti luokassa ui.Gui. Käyttöliittymä on eristetty sovelluslogiikasta ja käyttöliittymä käyttää logiikan toimintoja kutsumalla luokan domain.Fractal -metodeja. Koska sovelluslogiikka tarvitsee toimintaansa suuren määrän parametreja, joista osa ei välttämättä aina muutu, suurin osa parametrien välittämisestä käyttöliittymän ja sovelluslogiikan välillä tapahtuu siten, että käyttöliittymä kutsuu domain.Fractal -luokan setterimetodeja. Kun sovelluslogiikka on luonut uuden kuvan, se antaa sen käyttöliittymälle boolean-taulukkona, josta käyttöliittymä renderöi uuden kuvan näkyviin.


## Sovelluslogiikka

Sovelluslogiikan rakenteen näkee ylempänä olevasta luokka- ja pakkauskaaviosta.

Sovelluslogiikan kaikesta toiminnallisuudesta vastaa luokka domain.Fractal. Se käyttää apunaan luokan domain.ComplexNumber -olioita, joka tarjoaa mahdollisuuden tallentaa ja käsitellä kompleksilukuja. Se tarjoaa kompleksiluvun komponenttien getterien lisäksi metodit

- ComplexNumber square()
- ComplexNumber add(ComplexNumber cn)
- double magnitude()

Luokka domain.Fractal tarjoaa käyttöliittymälle mahdollisuuden asettaa sekä noutaa sen parametrejä, muodostaa senhetkisillä parametreilla uusi fraktaalikuvio sekä tallettaa parametrejä muutoshistoriaan ja peruuttaa muutoksia. Se tarjoaa myös mahdollisuuden asettaa parametrit takaisin oletusarvoihinsa. Fraktaalikuvioiden piirtäminen ja asetusten talletuksiin liittyvä toiminnallisuus olisi voitu jakaa kahteen luokkaan, mutta niin ei haluttu tehdä, sillä se olisi luonut ohjelmaan tarpeetonta monimutkaisuutta, sillä nämä kaksi toiminnallisuutta ovat niin vahvasti nivoutuneet yhteen. Kuitenkin, jos jompi kumpi toiminnallisuus olisi ollut nykyistä laajempi, olisi niiden erottaminen voinut olla aiheellista.

domain.Fractal käyttää parametrien muutoshistoriaa sille konstruktorin parametrinä injektoidun HistoryDao-olion avulla. Se saa konstruktorin parametrinä myös Parameters-olion, jonka avulla se pystyy käyttämään tiedostossa olevia konfiguraatiotietoja oletusarvojen hallintaan.


## Tietojen pysyväistallennus

Muutoshistoriaa käytetään dao.HistoryDao-rajapinnan avulla. Tässä ohjelmassa käytetty dao.HistoryDao-rajapinnan toteuttava luokka on dao.SQLiteHistoryDao. Se tarjoaa muutoshistorian käyttäen SQLite-tietokantaa. Luokka osaa tallettaa merkkijonotyyppistä tietoa ja noutaa sitä poistaen ja palauttaen viimeisimmän rivin. Se osaa myös tyhjentää tietokannan. domain.Fractal antaa metodikutsussaan String-tyyppisen parametrin, jossa asetuksen on formatoitu muotoon `iteraatiot,reaaliosa,imaginääriosa`. Tietoa noudettaessa dao.SQLiteHistoryDao palauttaa tällaisen merkkijonon, ja Fractal hoitaa parametrien purkamisen takaisin käyttökelpoiseen muotoon. SQLite-tietokannassa on merkkijonon lisäksi mukana id-numero, joka mahdollistaa viimeisimmän rivin noutamisen.

### Konfiguraatiot

Ohjelman oletusasetukset on talletettu konfiguraatiotiedostoon resources.config.properties. Tätä käytetään Javan Properties-olion avulla. Konfiguraatiotiedosto ei ole tarkoitettu käyttäjän muokattavaksi, joten se on sijoitettu sisäänrakennetuksi resurssiksi. Näin ohjelman käyttöönotto saadaan käyttäjälle mutkattomaksi. Ohjelmaan voisi tehdä toiminnallisuuden, jolla oletusasetuksia on mahdollistsa muuttaa.


## Päätoiminnallisuudet

Ohjelman päätoiminnallisuuksia ovat erilaisten parametrien muutosten aiheuttamat uudelleenpiirrot. Kaikkien parametrien osalta toimintalogiikka on melko samanlaista. Niiden parametrien, joiden muutoksia on mahdollista peruuttaa, toimintaan liittyy myös uuden muutoksen tallettaminen muutoshistoriaan. Muutoksen tallettamispäätös on jätetty käyttöliittymän tehtäväksi, sillä sovelluslogiikan on hankala tunnistaa, mistä muutoksista pitäisi tallettaa uusi muutos. Esimerkiksi liukusäädintä siirrettäessä jokaista siirron aikaista välipiirtoa ei haluttane tallettaa muutoshistoriaan, vaan sen sijaan mielekäs on ainoastaan siirron päätearvo.

Sekvenssikaaviossa on kuvattuna, mitä tapahtuu kun käyttäjä liikuttaa imaginääriluvun reaaliosaa muuttavaa liukusäädintä. Muiden parametrien muutokset toimivat vastaavasti, mutta osassa ei ole lopun muutoshistorian päivitystä:

![](https://raw.githubusercontent.com/tuomoart/ot-harjoitustyo/master/dokumentointi/kuvat/sekvenssikaavio.png)


## Ohjelmaan jääneet heikkoudet

### Käyttöliittymä

Käyttöliittymän rakentava koodi sijaitsee pääasiassa luokan Gui metodissa start. Koska metodi on todella suuri, olisi sitä aihetta pilkkoa.


### Domain

luokka Fractal ei noudata optimaalisesti Single-responsibility -periaatetta, sillä se sisältää sekä fraktaalin laskemiseen että muutoshistoriaan liittyviä toimintoja. Nämä toiminnot kuitenkin molemmat käyttävät samoja parametreja, joten niiden erottelu ei tämän ohjelman laajuus huomioon ottaen ollut mielekästä.
