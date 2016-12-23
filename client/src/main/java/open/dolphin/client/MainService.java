package open.dolphin.client;

/**
 * MainService.
 *
 * @author kazm
 */
public interface MainService {

    public String getName();

    public void setName(String name);

    public MainWindow getContext();

    public void setContext(MainWindow context);

    public void start();

    public void stop();

}
