package blackjackclient;

import java.io.IOException;
import java.io.PrintWriter;
// import static java.lang.System.exit;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlackjackClient {

  static final String MACHINE = "localhost";
  static final int PORT = 2121;
  

  public static void main(String[] args) {
    //if (args.length != 1) exit(1);
    final String name; // = args[0];
    int inHand;
    ClientState state = ClientState.IN_GAME;
    
    try (
        Socket s = new Socket(MACHINE, PORT); 
        Scanner sc = new Scanner(s.getInputStream());
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        Scanner in = new Scanner(System.in);
      )
    {
      System.out.print("Add meg a neved: ");
      name = in.nextLine();

      send(pw, name);

      inHand = Integer.valueOf(receive(sc));
      System.out.println("Kezdo lapok erteke: " + inHand);
      System.out.flush();
      
      String cmd;
      int newCard;
      
      do {
        if (state == ClientState.IN_GAME) {
          System.out.print("[hit/stick]: ");
          System.out.flush();
          cmd = in.nextLine();
          send(pw, cmd);
          if (cmd.equals("stick")) {
            state = ClientState.STICK;
          } else { // cmd.equals("hit");
            newCard = Integer.valueOf(receive(sc));
            System.out.print("Kapott lap erteke: " + newCard + "   ");
            inHand += newCard;
            if (inHand > 21) {
              state = ClientState.BUST;
            }
          }
        }
        String msg = receive(sc);
        if (msg.equals(ClientState.BUST.getValue())) {
          System.out.println(msg);
        } else {
          try {  
            Integer.parseInt(msg);
          } catch (NumberFormatException ex) {
            state = ClientState.FINISHED;
          }
          if (state != ClientState.FINISHED) {
            System.out.println("Kezben: " + msg + " (helyben szamolt: " + inHand + ")");
          } else {
            System.out.println("\nA nyertes neve: " + msg);
          }
        }
      } while (state != ClientState.FINISHED);
    } catch (IOException ex) {
      Logger.getLogger(BlackjackClient.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  static void send(PrintWriter pw, String msg) {
    pw.println(msg);
    pw.flush();
  }
  
  @SuppressWarnings("empty-statement")
  static String receive(Scanner sc) {
    while (!sc.hasNextLine());
    return sc.nextLine();
  }
}
