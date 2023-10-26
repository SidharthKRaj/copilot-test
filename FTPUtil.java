import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUtil {
    private String host;
    private String user;
    private String password;
    private FTPClient ftp;

    public FTPUtil(String host, String user, String password) {
        if (host == null) {
            throw new IllegalArgumentException("Missing required input: host");
        }
        if (user == null) {
            throw new IllegalArgumentException("Missing required input: user");
        }
        if (password == null) {
            throw new IllegalArgumentException("Missing required input: password");
        }
        this.host = host;
        this.user = user;
        this.password = password;
        this.ftp = null;
    }

    public void connect() throws IOException {
        if (ftp != null) {
            return;
        }
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                ftp = new FTPClient();
                ftp.connect(host);
                ftp.login(user, password);
                ftp.enterLocalPassiveMode();
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                break;
            } catch (IOException e) {
                retryCount++;
                System.err.println("Error connecting to FTP server: " + e.getMessage() + ". Retrying...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
        if (ftp == null) {
            throw new IOException("Could not connect to FTP server after " + retryCount + " retries");
        }
    }

    public void disconnect() throws IOException {
        if (ftp != null) {
            ftp.logout();
            ftp.disconnect();
            ftp = null;
        }
    }

    public void uploadFile(String localFile, String remoteFile) throws IOException {
        if (ftp == null) {
            throw new IOException("No FTP connection exists");
        }
        if (localFile == null) {
            throw new IllegalArgumentException("Missing required input: localFile");
        }
        if (remoteFile == null) {
            throw new IllegalArgumentException("Missing required input: remoteFile");
        }
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                ftp.put(localFile, remoteFile);
                break;
            } catch (IOException e) {
                retryCount++;
                System.err.println("Error uploading file: " + e.getMessage() + ". Retrying...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
        if (retryCount == 3) {
            throw new IOException("Could not upload file after " + retryCount + " retries");
        }
    }

    public void downloadFile(String remoteFile, String localFile) throws IOException {
        if (ftp == null) {
            throw new IOException("No FTP connection exists");
        }
        if (remoteFile == null) {
            throw new IllegalArgumentException("Missing required input: remoteFile");
        }
        if (localFile == null) {
            throw new IllegalArgumentException("Missing required input: localFile");
        }
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                ftp.get(remoteFile, localFile);
                break;
            } catch (IOException e) {
                retryCount++;
                System.err.println("Error downloading file: " + e.getMessage() + ". Retrying...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
        if (retryCount == 3) {
            throw new IOException("Could not download file after " + retryCount + " retries");
        }
    }
}