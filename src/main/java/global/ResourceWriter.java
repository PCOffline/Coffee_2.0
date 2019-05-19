package global;

public class ResourceWriter extends FileWrite {

    private static final String CLASS_COUNT = "src/main/resources/class_count.txt";
    private static final String CLASS_PATH = "src/main/resources/classes.txt";
    private static final String AT_PATH = "src/main/resources/assault_teams.txt";
    private static final String INF = "Infantry";
    private static final String REC = "Recon";
    private static final String PAR = "Paratrooper";
    private static final String TAN = "Tanker";
    private static final String PIL = "Pilot";
    private static final String GEN = "General";
    private static final String[] classes = {INF, REC, PAR, TAN, PIL, GEN};

    private ResourceWriter() {
    }

    static String getClasses() {
        return readFile(CLASS_COUNT);
    }

    static String getClasses(long id) {
        long line = getLine(CLASS_PATH, id + ":\n", true, true);
        return readFile(CLASS_PATH, line, getLine(CLASS_PATH, "\n", true, true, line));
    }

    static void addToClass(int type) {
        String s = goLine(CLASS_COUNT, (long) type + 1L);
        String substring = s.split(" ")[1];
        writeFile(CLASS_COUNT, s.replace(substring, Integer.parseInt(substring) + 1 + ""), (long) type + 1L);
    }

    static void addToClass(int type, long id) {
        if (keywordExist(CLASS_PATH, id + ":\n", true, true)) {
            long line = getLine(CLASS_PATH, "\n", true, true, getLine(CLASS_PATH, id + ":\n", true, true));
            writeFile(CLASS_PATH, GEN + " " + classes[type] + "\n", line);
        } else {
            writeFile(CLASS_PATH, id + ":\n");
            writeFile(CLASS_PATH, GEN + " " + classes[type] + "\n");
        }
    }


    static void addToClass(int type, long id, String rank, String weapon) {
        if (keywordExist(CLASS_PATH, id + ":\n", true, true)) {
            long line = getLine(CLASS_PATH, "\n", true, true, getLine(CLASS_PATH, id + ":\n", true, true));
            writeFile(CLASS_PATH, classes[type] + " " + rank + " " + weapon + "\n", line);
        } else {
            writeFile(CLASS_PATH, id + ":\n");
            writeFile(CLASS_PATH, classes[type] + " " + rank + " " + weapon + "\n");
        }
    }

    static String getAT() {
        String infantry = readFile(AT_PATH, getLine(AT_PATH, INF, true, false));
        infantry = infantry.substring(infantry.indexOf(':'));
        String recon = readFile(AT_PATH, getLine(AT_PATH, REC, true, false));
        recon = recon.substring(recon.indexOf(':'));
        String paratrooper = readFile(AT_PATH, getLine(AT_PATH, PAR, true, false));
        paratrooper = paratrooper.substring(paratrooper.indexOf(':'));
        String tanker = readFile(AT_PATH, getLine(AT_PATH, TAN, true, false));
        tanker = tanker.substring(tanker.indexOf(':'));
        String pilot = readFile(AT_PATH, getLine(AT_PATH, PIL, true, false));
        pilot = pilot.substring(pilot.indexOf(':'));
        return "Infantry:" + infantry + "\nRecon:" + recon + "\nParatrooper:" + paratrooper + "\nTanker:" + tanker + "\nPilot:" + pilot;
    }

    static void addToAT(int type) {
        switch (type) {
            case 0:
                String infantry = goLine(AT_PATH, getLine(AT_PATH, INF, true, false));
                writeFile(AT_PATH, INF + ": " + Integer.parseInt(infantry.substring(infantry.indexOf(' '))) + 1, getLine(AT_PATH, INF, true, false));
                break;
            case 1:
                String recon = goLine(AT_PATH, getLine(AT_PATH, REC, true, false));
                writeFile(AT_PATH, REC + ": " + Integer.parseInt(recon.substring(recon.indexOf(' '))) + 1, getLine(AT_PATH, REC, true, false));
                break;
            case 2:
                String paratrooper = goLine(AT_PATH, getLine(AT_PATH, PAR, true, false));
                writeFile(AT_PATH, PAR + ": " + Integer.parseInt(paratrooper.substring(paratrooper.indexOf(' '))) + 1, getLine(AT_PATH, PAR, true, false));
                break;
            case 3:
                String tanker = goLine(AT_PATH, getLine(AT_PATH, TAN, true, false));
                writeFile(AT_PATH, TAN + ": " + Integer.parseInt(tanker.substring(tanker.indexOf(' '))) + 1, getLine(AT_PATH, TAN, true, false));
                break;
            case 4:
                String pilot = goLine(AT_PATH, getLine(AT_PATH, PIL, true, false));
                writeFile(AT_PATH, PIL + ": " + Integer.parseInt(pilot.substring(pilot.indexOf(' '))) + 1, getLine(AT_PATH, PIL, true, false));
                break;
            default:
                break;
        }
    }
}
