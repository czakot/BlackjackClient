package blackjackclient;

public enum ClientState {
  IN_GAME("in game"),
  STICK("stick"),
  BUST("bust"),
  FINISHED("finished");
  
  private final String value;
  
  private ClientState(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
}
