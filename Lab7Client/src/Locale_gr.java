import java.util.ListResourceBundle;

/**
 * Created by bespa on 02.06.2017.
 */
public class Locale_gr extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"DateTime","dd/MM/yyyy hh:mm:ss"},
                { "true", "ναι"},
                { "false",   "όχι"},
                { "Delete",  "Διαγραφή"},
                {"Edit",     "Αλλαγή"},
                {"DoHust",     "μπορείτε να εκτελέσετε!!!"},
                {"ShowThoughts",  "Εμφάνιση σκέψεις"},
                {"Do",   "Να"},
                {"hust",     "Μάρκα"},
                {"AddInJson",    "Προσθήκη σε json"},
                {"Remove",   "Διαγραφή σε json"},
                {"AddPerson",    "Προσθήκη ατόμου"},
                {"ChooseColor",  "Επιλέξτε το χρώμα"},
                {"ChooseLanguage",   "Να επιλέξετε τη γλώσσα της"},
                {"Name",     "Όνομα"},
                {"Age",  "Ηλικία"},
                {"Add",  "Προσθέσετε"},
                {"Cancel",   "να Κλείσει"},
                {"TroublesWithTheLaw",   "Προβλήματα με το νόμο"},
                {"Play",  "Παίξτε"},
                {"Stop",   "Σταμάτα"},
                {"Pause","Παύση"},
                {"Resume","Βιογραφικό"},
                {"Ok","εντάξει"},
                {"SetName","Όνομα"},
                {"MalishAndKarlson","Παιδί και Carlson"},
                {"PutHere","Προσθέστε το άτομο σε JSON:"},
                {"Разрыв соединения!Сервер отключён!","Για να τερματίσετε τη σύνδεση!"},
                {"Write name of NormalHuman","Γράψτε το όνομα του προσώπου"},
                {"Данный человек редактируется!!!","Αυτό το άτομο έχει!!!"},
                {"NothingToRemove","Τίποτα για διαγραφή!"},
                {"WrongNormalHuman","Λάθος άνθρωπος!"},
                {"NoNormalHumanLikeThis","Δεν υπάρχει τέτοιο άτομο!"},
                {"Нет подключения!Сервер отключён!","Δεν υπάρχει σύνδεση! Ο διακομιστής είναι!"},
                {"Time","Ώρα δημιουργίας"},
                {"Sort","Είδος"},
                {"Filter","Φίλτρο"},
                {"Reset","Επαναφορά"}
        };
    }
}