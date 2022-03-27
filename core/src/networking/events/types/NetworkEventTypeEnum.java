package networking.events.types;

public class NetworkEventTypeEnum {
  public static final String HANDSHAKE_OUTGOING = "HANDSHAKE_OUTGOING";
  public static final String HANDSHAKE_INCOMING = "HANDSHAKE_INCOMING";

  public static final String UPDATE_ENTITY_OUTGOING = "UPDATE_ENTITY_OUTGOING";
  public static final String UPDATE_ENTITY_INCOMING = "UPDATE_ENTITY_INCOMING";

  public static final String CREATE_ENTITY_INCOMING = "CREATE_ENTITY_INCOMING";
  public static final String CREATE_ENTITY_OUTGOING = "CREATE_ENTITY_OUTGOING";

  public static final String REMOVE_ENTITY_INCOMING = "REMOVE_ENTITY_INCOMING";
  public static final String REMOVE_ENTITY_OUTGOING = "REMOVE_ENTITY_OUTGOING";

  public static final String REPLACE_ENTITY_INCOMING = "REPLACE_ENTITY_INCOMING";
  public static final String REPLACE_ENTITY_OUTGOING = "REPLACE_ENTITY_OUTGOING";

  public static final String CHUNK_SWAP_INCOMING = "CHUNK_SWAP_INCOMING";
  public static final String CHUNK_SWAP_OUTGOING = "CHUNK_SWAP_OUTGOING";

  public static final String AUTH_INCOMING = "AUTH_INCOMING";

  public static final String PING_REQUEST_OUTGOING = "PING_REQUEST_OUTGOING";
  public static final String PING_REQUEST_INCOMING = "PING_REQUEST_INCOMING";

  public static final String PING_RESPONSE_OUTGOING = "PING_RESPONSE_OUTGOING";
  public static final String PING_RESPONSE_INCOMING = "PING_RESPONSE_INCOMING";
}
