package model;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
        // Création du joueur
        Player alice = new Player("Alice");

        // Donner des jetons à Alice
        Map<Money, Integer> jetons = Map.of(
                Money.DIAMOND, 3,
                Money.EMERALD, 2,
                Money.RUBY, 1
        );
        alice.addMoney(jetons);
        System.out.println("Jetons de départ d'Alice : " + alice.getMoney());

        // Création d'une carte Upgrade niveau 2 (2 points)
        Upgrade upgradeCard = new Upgrade(2, "U1", Money.SAPPHIRE);
        // Définir un coût pour cette carte
        upgradeCard.cost().put(Money.DIAMOND, 2);
        upgradeCard.cost().put(Money.EMERALD, 1);
        upgradeCard.cost().put(Money.GOLD, 8);

        System.out.println("Carte à acheter : " + upgradeCard);

        // Tenter l'achat
        boolean achatReussi = alice.buy(upgradeCard);
        System.out.println("Achat réussi ? " + achatReussi);

        if (achatReussi) {
            alice.addCard(upgradeCard);
            alice.update(); // Mise à jour des points
        }

        // Résultat final
        System.out.println("Jetons restants : " + alice.getMoney());
        System.out.println("Cartes possédées : " + alice.getPossession());
        System.out.println("Points : " + alice.getPts());
    }
}
