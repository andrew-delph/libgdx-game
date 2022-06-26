package networking.translation;

public class DataTranslationEnum {
  public static final String UUID = "UUID";
  public static final String UUID_LIST = "UUID_LIST";
  public static final String CHUNK_RANGE = "CHUNK_RANGE";
  public static final String COORDINATES = "COORDINATES";
  public static final String HEALTH = "HEALTH";
  public static final String EQUIPPED = "EQUIPPED";
  public static final String REPLACE_BLOCK = "REPLACE_BLOCK";
  public static final String CREATE_ENTITY = "CREATE_ENTITY";
  public static final String UPDATE_ENTITY = "UPDATE_ENTITY";
  public static final String REMOVE_ENTITY = "REMOVE_ENTITY";
  public static final String HANDSHAKE = "HANDSHAKE";
  public static final String CREATE_AI = "CREATE_AI";
  public static final String CHUNK_SWAP = "CHUNK_SWAP";
  public static final String AUTH = "AUTH";
  public static final String REQUEST_PING = "REQUEST_PING";
  public static final String RESPONSE_PING = "RESPONSE_PING";
  public static final String CREATE_TURRET = "CREATE_TURRET";
  public static final String INDEX = "INDEX";

  public static final String EMPTY_ITEM = "EMPTY_ITEM";
  public static final String ORB_ITEM = "ORB_ITEM";
  public static final String[] ITEM_TYPES = {EMPTY_ITEM, ORB_ITEM}; // THIS IS GROSS
}
