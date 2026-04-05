package Net;

public interface ReceiverEventListener {
    void OnUserJoined(String user);
    void OnUserLeft(String user);
    void OnRequestSync();
    void OnMessageReceived(String message);
}
