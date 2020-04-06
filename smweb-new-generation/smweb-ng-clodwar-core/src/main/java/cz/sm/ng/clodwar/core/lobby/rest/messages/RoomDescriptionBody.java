package cz.sm.ng.clodwar.core.lobby.rest.messages;

/**
 * Simple POJO class used to store room description
 * message data. This object will be serialized and
 * deserialized based on JSON format by automatically
 * implemented Spring Boot converters.
 *
 * @author Norbert Dopjera
 */
public class RoomDescriptionBody {
    private int id;
    private String name;
    private String password;
    private int capacity;
    private String description;

    public RoomDescriptionBody() {}

    public RoomDescriptionBody(int id, String name, String password, int capacity, String description) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.capacity = capacity;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
