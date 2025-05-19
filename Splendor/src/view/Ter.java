package view;
import java.util.*;

public final class Ter {

    // Scanner partagé et statique
    private static final Scanner scanner = new Scanner(System.in);

  

    // 1. Rien : juste retour à la ligne
    public static void ln() {
        System.out.println();
    }

    // 2. Affiche un message (String)
    public static void ln(String message) {
        System.out.println(message);
    }

    // 3. Affiche un message (StringBuilder)
    public static void ln(StringBuilder sb) {
        System.out.println(sb.toString());
    }

    // 4. Méthode générique avec options
    public static Object ln(Object message, Integer expectType, Boolean withNewLine) {
        if (message instanceof StringBuilder) {
            System.out.print(((StringBuilder) message).toString());
        } else if (message instanceof String) {
            System.out.print((String) message);
        }

        if (Boolean.TRUE.equals(withNewLine)) {
            System.out.println();
        }

        if (expectType == null) return null;

        if (expectType == 1) {
            while (!scanner.hasNextInt()) {
                System.out.print("[Erreur] Entrez un entier valide : ");
                scanner.next(); // Ignore entrée invalide
            }
            int value = scanner.nextInt();
            scanner.nextLine(); // 🧹 Consomme le \n restant après nextInt()
            return value;
        } else if (expectType == 2) {
            String input = scanner.nextLine().trim();
            while (input.isEmpty()) {
                System.out.print("[Erreur] Entrez un texte non vide : ");
                input = scanner.nextLine().trim();
            }
            return input;
        }

        return null;
    }


    // 5. Supprime tout
    public static void clear() {
    	System.out.flush();
    }
    
    public static void space() {
    	ln();
    	ln("--- --- ---  --- --- ---  --- --- ---");
    	ln();
    }

}
