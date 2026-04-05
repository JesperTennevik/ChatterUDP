# Chatter
Chatter is a Lightweight LAN chat application built using Java, Swing and UDP-Multicast.

# Features
- Custom display name.
- List of active users.
- Automatic synchronization of active users.
- Supports multiple instances on the same machine.

# Structure
### Chatter (Entry Point)
Handles the Swing UI, List of active users, sending messages and listens to receive events.

### Receiver
Listens for packets on the connected multicast address. Parses the messages and sends back the correct event.   

### Sender
Sends messages to the connected multicast address.

### ReceiverEventListener
Callback interface that allows the receiver to notify the main program of events.

```Java
public interface ReceiverEventListener {
    void OnUserJoined(String user);
    void OnUserLeft(String user);
    void OnRequestSync();
    void OnMessageReceived(String message);
}
```

# Issues
No known issues.