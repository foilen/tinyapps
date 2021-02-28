/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package slbh.lang;

public class French implements Language {
    @Override
    public String buttonAddFloorAfter() {
        return "Ajouter étage après";
    }

    @Override
    public String buttonAddFloorBefore() {
        return "Ajouter étage avant";
    }

    @Override
    public String buttonCancel() {
        return "Annuler";
    }

    @Override
    public String buttonProperties() {
        return "Propriétés";
    }

    @Override
    public String buttonRemoveFloor() {
        return "Enlever l'étage";
    }

    @Override
    public String buttonToClipboard() {
        return "Copier au presse-papier";
    }

    @Override
    public String labelBackgroundFloor() {
        return "Étage en fond: ";
    }

    @Override
    public String labelCurrentFloor() {
        return "Étage courant: ";
    }

    @Override
    public String labelObjects() {
        return "Objet";
    }

    @Override
    public String lang() {
        return "Français";
    }

    @Override
    public String menuFile() {
        return "Fichier";
    }

    @Override
    public String menuLanguage() {
        return "Langue";
    }

    @Override
    public String menuNew() {
        return "Nouveau";
    }

    @Override
    public String menuOpen() {
        return "Ouvrir";
    }

    @Override
    public String menuSave() {
        return "Enregistrer";
    }

    @Override
    public String objectFloor() {
        return "Plancher";
    }

    @Override
    public String objectStairsE() {
        return "Escalier Est";
    }

    @Override
    public String objectStairsN() {
        return "Escalier Nord";
    }

    @Override
    public String objectStairsS() {
        return "Escalier Sud";
    }

    @Override
    public String objectStairsW() {
        return "Escalier Ouest";
    }

    @Override
    public String objectStart() {
        return "Position de départ";
    }

    @Override
    public String objectWall() {
        return "Mur";
    }

    @Override
    public String propertiesDeltaXY() {
        return "Largeur (m)";
    }

    @Override
    public String propertiesDeltaZ() {
        return "Hauteur (m)";
    }

    @Override
    public String propertiesRepeat() {
        return "Répéter la figure (tour de babel)";
    }
}
