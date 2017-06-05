import java.util.ListResourceBundle;

/**
 * Created by bespa on 02.06.2017.
 */
public class Locale_isl extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"DateTime","dd/MM/yyyy hh:mm:ss"},
                { "true", "Já"},
                { "false",   "enga"},
                { "Delete",  "Til að fjarlægja"},
                {"Edit",     "Til að breyta"},
                {"DoHust",     "þú keyrir!!!"},
                {"ShowThoughts",  "Sýna hugsanir"},
                {"Do",   "Til að gera"},
                {"hust",     "Flís"},
                {"AddInJson",    "Bæta í json"},
                {"Remove",   "Fjarlægja json"},
                {"AddPerson",    "Til að bæta mann"},
                {"ChooseColor",  "Veldu lit"},
                {"ChooseLanguage",   "Til að velja tungumál"},
                {"Name",     "Nafnið"},
                {"Age",  "Aldri"},
                {"Add",  "Bæta við"},
                {"Cancel",   "Nálægt"},
                {"TroublesWithTheLaw",   "í vandræðum með lögum"},
                {"Play",  "Spila"},
                {"Stop",   "stöðva"},
                {"Pause","Hlé"},
                {"Resume","Ný"},
                {"Ok","allt í lagi"},
                {"SetName","Nafnið"},
                {"MalishAndKarlson","Kid og Carlson"},
                {"PutHere","Bæta við viðkomandi að json:"},
                {"Разрыв соединения!Сервер отключён!","Slitnar!Þjónninn er niður!"},
                {"Write name of NormalHuman","Skrifaðu nafn persónu"},
                {"Данный человек редактируется!!!","Þessi manneskja breyta!!!"},
                {"NothingToRemove","Það er ekkert að fjarlægja!"},
                {"WrongNormalHuman","Ranga manneskju!"},
                {"NoNormalHumanLikeThis","Það er engin slík manneskja!"},
                {"Нет подключения!Сервер отключён!","Engin tenging! Þjónninn er niður!"},
                {"Time","Sköpun tíma"},
                {"Sort","Konar"},
                {"Filter","Sía"},
                {"Reset","Endurstilla"}
        };
    }
}
