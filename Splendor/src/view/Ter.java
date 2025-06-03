package view;
import java.util.*;

public final class Ter {

    // Scanner partagé et statique
    private static final Scanner scanner = new Scanner(System.in);

    // Retour à la ligne
    public static void ln() {
        System.out.println();
    }

    // Affiche un String
    public static void ln(String message) {
        System.out.println(message);
    }

    // Affiche un StringBuilder
    public static void ln(StringBuilder sb) {
        System.out.println(sb.toString());
    }
    
    // Affiche un StringBuilder ou un String + option pour faire un retour à la ligne ou pas
    public static void ln(Object message,boolean withNewLine) {
    	Objects.requireNonNull(message);
    	if (message instanceof StringBuilder) System.out.print(((StringBuilder) message).toString());
        else if (message instanceof String) System.out.print((String) message);
        if(withNewLine)System.out.println();
    }

    /// Permet de faire un scanner pour avoir un int en retour
    /// @Param message, affiche un string avant de faire le scanner
    /// pas de retour à la ligne
    /// @return int saisie dans system.out
    public static int sc(String message) {
    	Objects.requireNonNull(message);
    	System.out.print(message);
    	
        while (!scanner.hasNextInt()) {
            System.out.print("[Erreur] Entrez un entier valide : ");
            scanner.next(); // Ignore entrée invalide
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
  /// Permet de faire un scanner pour avoir un int en retour
    /// @Param message, affiche un string avant de faire le scanner
    /// pas de retour à la ligne
    /// @return string saisie dans system.out
    public static String scS(String message) {
    	Objects.requireNonNull(message);
    	System.out.print(message);
    	
    	String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("[Erreur] Entrez un texte non vide : ");
            input = scanner.nextLine().trim();
        }
        return input;
    }
    
    // Supprime tout
    public static void clear() {
    	System.out.flush();
    }
    
    public static void space() {
    	ln();
    	ln("--- --- ---  --- --- ---  --- --- ---");
    	ln();
    }

}
