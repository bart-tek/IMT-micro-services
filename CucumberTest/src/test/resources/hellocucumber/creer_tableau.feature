Feature: Creer un tableau

  Scenario Outline: Creer un tableau
    Given une cellule de tableur
    When Je saisi "<saisie>"
    Then La cellule affiche "<answer>"

  Examples:
   |saisie   | answer |
   |= 1+3   | 4 	    |
   |= 45    | 45			|
   |= 38+38 | 76			|