package global;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileWrite {

    private static final Logger logger = LoggerFactory.getLogger(FileWrite.class);

    public FileWrite() {
    }

    /**
     * Read a file and return its contents
     *
     * @param path Path of file
     * @return Contents of file
     */

    @NotNull
    public static String readFile(String path) {
        StringBuilder raw;
        String line;
        String text = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {


            raw = new StringBuilder();
            line = reader.readLine();

            while (line != null) {
                raw.append(line);
                raw.append("\n");
                line = reader.readLine();
            }

            text = raw.toString();

        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return text != null ? text : "";
    }

    /**
     * Read a file and return a string with all the content of the file until the limit
     *
     * @param path  Path of the file to read
     * @param limit The last line to read
     * @return String with all the content of the file until the limit
     */

    @NotNull
    public static String readFile(String path, long limit) {
        StringBuilder raw;
        String line;
        String text = null;
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            raw = new StringBuilder();
            line = reader.readLine();

            while (line != null && count <= limit) {
                count++;
                raw.append(line);
                raw.append("\n");
                line = reader.readLine();
            }

            text = raw.toString();

        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return text != null ? text : "";
    }

    /**
     * Read a file and return a string with all the content of the file from the limit
     *
     * @param path  Path of the file to read
     * @param limit The first line to read
     * @return String with all the content of the file from the limit
     */

    @NotNull
    public static String readFile(long limit, String path) {
        StringBuilder raw;
        String line;
        String text = null;
        int count = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {


            raw = new StringBuilder();
            line = reader.readLine();

            while (line != null && count < limit) {
                line = reader.readLine();
                ++count;
            }

            while (line != null) {
                raw.append(line);
                raw.append("\n");
                line = reader.readLine();
            }

            text = raw.toString();

        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return text != null ? text : "";
    }

    @NotNull
    public static String readFile(String path, long start, long end) {
        StringBuilder res = new StringBuilder();
        for (long i = start; i <= end; i++) {
            res.append(goLine(path, i)).append("\n");
        }
        return res.toString();
    }

    /**
     * Write a value into the last line of a file
     *
     * @param path  Path of the file to write to
     * @param value The value to write in the file
     */

    public static void writeFile(String path, String value) {

        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            outputStream.write((goLine(path, getLastLine(path)).equals("") ? readFile(path) + value : "\n" + readFile(path) + value).getBytes());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Write a value into the selected line
     *
     * @param path  Path of the file to write to
     * @param value The value to write in the file
     * @param line  line to write the value into the selected line
     */

    public static void writeFile(String path, String value, long line) {

        String s = line > 1 ? readFile(path, line - 1) : "";
        String s1 = readFile(line + 1, path);
        byte[] b = (s + (line > 1 ? "\n" : "") + value + (!s1.equals("") ? "\n" + s1 : "")).getBytes();
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            outputStream.write(b);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Delete line of a file
     *
     * @param path Path of the file where the line is deleted
     * @param line The line number to be deleted
     */

    public static void deleteLine(String path, long line) {

        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            outputStream.write((readFile(path, line - 1) + "\n" + readFile(line + 1, path)).getBytes());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Checks if a keyword exists in a file
     *
     * @param path          Path of file
     * @param keyword       The keyword to search
     * @param ignoreCase    Should the search ignore case or not
     * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
     * @return True if keyword is found
     */

    public static boolean keywordExist(String path, String keyword, boolean ignoreCase, boolean keywordIsLine) {
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();

            while (line != null) {
                if (keywordIsLine)
                    if (line.equals(keyword) || ignoreCase && line.equalsIgnoreCase(keyword))
                        return true;
                    else if (line.contains(keyword) || line.toLowerCase().contains(keyword.toLowerCase()))
                        return true;
                line = reader.readLine();
            }

            return false;
        } catch (java.io.IOException e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a keyword exists in a file
     *
     * @param path          Path of file
     * @param keyword       The keyword to search
     * @param ignoreCase    Should the search ignore case or not
     * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
     * @param start         Line to start the search from
     * @return True if keyword is found
     */

    public static boolean keywordExist(String path, String keyword, boolean ignoreCase, boolean keywordIsLine, long start) {
        String line;
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();

            while (line != null) {

                while (count < start) {
                    line = reader.readLine();
                    count++;
                }

                if (keywordIsLine)
                    if (line.equals(keyword) || (ignoreCase && line.equalsIgnoreCase(keyword)))
                        return true;
                    else if (line.contains(keyword) || line.toLowerCase().contains(keyword.toLowerCase()))
                        return true;
                line = reader.readLine();
            }

            return false;
        } catch (java.io.IOException e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    /**
     * Find the line of the first occurrence of a keyword as a whole line or as a part of a line in the file
     *
     * @param path          Path of the file to find the line of the keyword in
     * @param keyword       The keyword to find the line of
     * @param ignoreCase    Should the search ignore case or not
     * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
     * @return The line of the first occurrence of the keyword as a whole line or as a part of a line in the file
     */

    public static long getLine(String path, String keyword, boolean ignoreCase, boolean keywordIsLine) {
        String line;
        int count = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            line = reader.readLine();
            if (keywordIsLine) {
                while (line != null && (line.equals(keyword) || (ignoreCase && line.equalsIgnoreCase(keyword)))) {
                    line = reader.readLine();
                    count++;
                }
            } else {
                while (line != null) {
                    if (line.contains(keyword) || (ignoreCase && line.toLowerCase().contains(keyword)))
                        return count;
                    line = reader.readLine();
                    count++;
                }
            }

            return count;

        } catch (IOException e) {
            logger.info(e.getMessage());
            return -1;
        }
    }

    /**
     * Find the line of the first occurrence of a keyword as a whole line or as a part of a line in the file from the line inputted
     *
     * @param path          Path of the file to find the line of the keyword in
     * @param keyword       The keyword to find the line of
     * @param ignoreCase    Should the search ignore case or not
     * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
     * @param start         Line to start from
     * @return The line of the first occurrence of the keyword as a whole line or as a part of a line in the file
     */

    public static long getLine(String path, String keyword, boolean ignoreCase, boolean keywordIsLine, long start) {
        String line;
        int count = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            line = reader.readLine();
            while (count < start) {
                line = reader.readLine();
                count++;
            }

            if (keywordIsLine) {
                while (line != null && (line.equals(keyword) || (ignoreCase && line.equalsIgnoreCase(keyword)))) {
                    line = reader.readLine();
                    count++;
                }
            } else {
                while (line != null && (line.contains(keyword) || (ignoreCase && line.toLowerCase().contains(keyword.toLowerCase())))) {
                    line = reader.readLine();
                    count++;
                }
            }

            return count;

        } catch (IOException e) {
            logger.info(e.getMessage());
            return -1;
        }
    }

    public static long getLastLine(String path) {
        return readFile(path).split("\n").length - 1;
    }

    /**
     * Returns the content of a specific line
     *
     * @param path Path of the file that has the line
     * @param line Number of the line to get the content of
     * @return The content of a specific line
     */

    public static String goLine(String path, long line) {
        return readFile(line, path).split("\n")[0];
    }

}
