package networking.events.types;

public class NetworkEventTypeEnum {
    public final static String HANDSHAKE_OUTGOING = "HANDSHAKE_OUTGOING";
    public final static String HANDSHAKE_INCOMING = "HANDSHAKE_INCOMING";

    public final static String UPDATE_ENTITY_OUTGOING = "UPDATE_ENTITY_OUTGOING";
    public final static String UPDATE_ENTITY_INCOMING = "UPDATE_ENTITY_INCOMING";

    public final static String CREATE_ENTITY_INCOMING = "CREATE_ENTITY_INCOMING";
    public final static String CREATE_ENTITY_OUTGOING = "CREATE_ENTITY_OUTGOING";

    public final static String REMOVE_ENTITY_INCOMING = "REMOVE_ENTITY_INCOMING";
    public final static String REMOVE_ENTITY_OUTGOING = "REMOVE_ENTITY_OUTGOING";

    public final static String REPLACE_ENTITY_INCOMING = "REPLACE_ENTITY_INCOMING";
    public final static String REPLACE_ENTITY_OUTGOING = "REPLACE_ENTITY_OUTGOING";
}
