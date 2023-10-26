import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.example.util.FTPUtil;
import com.example.util.DBUtil;

public class ERADownloadandUpdate {
    private static final String FTP_HOST = "ftp.example.com";
    private static final int FTP_PORT = 21;
    private static final String FTP_USER = "username";
    private static final String FTP_PASS = "password";
    private static final String FTP_REMOTE_FILE = "/path/to/era/file.era";
    private static final String DB_DSN = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String DB_USER = "dbuser";
    private static final String DB_PASS = "dbpass";

    public static void main(String[] args) {
        FTPUtil ftpUtil = new FTPUtil(FTP_HOST, FTP_PORT, FTP_USER, FTP_PASS);
        DBUtil dbUtil = new DBUtil(DB_DSN, DB_USER, DB_PASS);
        try {
            ftpUtil.connect();
            File localFile = File.createTempFile("era", ".era");
            ftpUtil.downloadFile(FTP_REMOTE_FILE, localFile.getAbsolutePath());
            String eraString = readFileToString(localFile);
            dbUtil.connect();
            dbUtil.executeUpdate("INSERT INTO era_data (era_string) VALUES (?)", eraString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpUtil.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dbUtil.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String readFileToString(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        try (InputStream inputStream = new java.io.FileInputStream(file)) {
            inputStream.read(bytes);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}