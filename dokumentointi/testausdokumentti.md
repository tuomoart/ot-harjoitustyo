# Testausdokumentti

Ohjelmalogiikkaa on testattu Junitilla yksikkö- ja integraatiotasolla ja koko ohjelman toimintaa on testattu manuaalisesti.


## Yksikkö- ja integraatiotestaus

Sovelluslogiikan testaamisesta huolehtivat testiluokat domain.FractalTest sekä domain.ComplexNumberTest. Nämä luokat testaavat pakkauksen domain luokkia sekä yksikkö-, että integraatiotestein. Monet sovelluslogiikan osat on testattu jo niiden kirjoitusvaiheessa yksikkötestein, eikä näiden testien poistamiselle jälkikäteen nähty tarvetta. Valtaosa sovelluslogiikan toiminnoista on testattu myös integraatiotestein, minkä takia useita asioita testataan moneen kertaan. Tämän ei nähty kuitenkaan olevan huono asia. Laajan yksikkötestauksen takia jokaista toimintoa ei ole testattu erillisillä integraatiotason testeillä, sillä laajan integraatiotason testin epäonnistuessa yksikkötestit kertovat, mistä epäonnistuminen johtuu. DAO luokan dao.SQLiteHistoryDao testaamisesta huolehtii testiluokka dao.SQLiteDaoTest. Myös siinä merkittävimmät toiminnallisuudet on testattu sekä yksikkö- että integraatiotestein.

### Testauskattavuus

Käyttöliittymää lukuunottamatta sovelluksen testauksen rivikattavuus on 99% ja haarautumakattavuus on 100%

TODO: KUVA

Testaamatta jäi odottamatonta virhettä koskeva tilanne
