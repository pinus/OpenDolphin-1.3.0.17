package open.dolphin.orca;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import open.dolphin.infomodel.InfoModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.service.PvtService;
import org.apache.log4j.Logger;

/**
 * ORCA から CLAIM を受け取り，pvt モデルを作成して PvtService に送る server.
 * ポート 5002 で ORCA とソケット通信する
 * transferred from version 2.2
 * @author pns
 */
@Singleton
@Startup
@RunAs("user")
public class PvtServer {

    private static final int DEFAULT_PORT = 5002;
    private static final int EOT = 0x04;
    private static final int ACK = 0x06;
    private static final int NAK = 0x15;
    private static final String UTF8 = "UTF8";
    private static final String SJIS = "SHIFT_JIS";
    private static final String EUC = "EUC_JIS";

    private ServerSocket listenSocket;
    private Thread serverThread;
    private final String encoding = UTF8;

    private final Logger logger = Logger.getLogger(PvtServer.class);

    @EJB
    private PvtService pvt;

    public PvtServer() {
    }

    @PostConstruct
    public void startService() {
        try {
            logger.info("PvtServer: start");

            InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT);
            listenSocket = new ServerSocket();
            listenSocket.bind(address);
            logger.info("PvtServer: socket address = " + address);

            serverThread = new ServerThread();
            serverThread.start();
        } catch (IOException ex) {
            logger.error("PvtServer exeption : " + ex.getMessage());
        }
    }

    @PreDestroy
    public void stopService() {
        logger.info("PvtServer: stop");

        if (serverThread != null) {
            serverThread = null;
        }

        if (listenSocket != null) {
            try {
                listenSocket.close();
                listenSocket = null;
            } catch (IOException e) {
                logger.info(e.getMessage(), e.getCause());
            }
        }
    }

    /**
     * Socket 接続待ちするスレッド
     * 接続があったら Connection スレッドを呼び出して，すぐ次の接続待ちをする
     */
    private final class ServerThread extends Thread {

        @Override
        public void run() {

            Thread thisThread = Thread.currentThread();

            while (thisThread == serverThread) {
                try {
                    logger.info("PvtServer: waiting for connection");
                    Socket client = listenSocket.accept();
                    logger.info("PvtServer: connected from " + client.getInetAddress().getHostAddress());
                    Connection con = new Connection(client);
                    con.start();

                } catch (IOException e) {
                    logger.info(e.getMessage(), e.getCause());
                    logger.info("PvtServer Exception while listening for connections:" + e);
                }
            }
        }
    }

    /**
     * socket (ORCA) から CLAIM データを受け取って，addPvt するスレッド
     * addPvt が終わったら，スレッドも終了する
     */
    private final class Connection extends Thread {

        private Socket client;

        public Connection(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            BufferedInputStream reader;
            BufferedOutputStream writer;

            try {

                reader = new BufferedInputStream(new DataInputStream(client.getInputStream()));
                writer = new BufferedOutputStream(new DataOutputStream(client.getOutputStream()));

                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                BufferedOutputStream buf = new BufferedOutputStream(bo);
                String received;

                byte[] buffer = new byte[16384];
                int readLen;

                while (true) {

                    readLen = reader.read(buffer);

                    if (readLen == -1) {
                        break;
                    }

                    if (buffer[readLen-1] == EOT) {
                        buf.write(buffer, 0, readLen-1);
                        buf.flush();
                        received = bo.toString(encoding);
                        //int len = received.length();
                        bo.close();
                        buf.close();

                        //System.out.println("PvtServer: received=" + received);
                        addPvt(received);

                        // return ACK
                        writer.write(ACK);
                        writer.flush();

                    } else {
                        buf.write(buffer, 0, readLen);
                    }
                }

                reader.close();
                writer.close();
                client.close();
                client = null;

            } catch (IOException e) {
                logger.info("PvtServer: catch " + e.getMessage());

            } finally {
                if (client != null) {
                    try {
                        client.close();
                        client = null;
                    } catch (IOException e2) {
                        logger.info("PvtServer: finally " + e2.getMessage());
                    }
                }
            }
        }

        private void addPvt(String pvtXml) {
            BufferedReader r = new BufferedReader(new StringReader(pvtXml));
            PvtBuilder builder = new PvtBuilder();
            builder.parse(r);
            PatientVisitModel model = builder.getProduct();

            pvt.addPvt(model, InfoModel.DEFAULT_FACILITY_OID);
            logger.info("PvtServer: addPvt [" + model.getPatient().getPatientId() + "]");
        }
    }
}
