package fr.marconnet.colisee.tools;

import java.util.List;

import fr.marconnet.acp.models.Showing;


public class FormattingTools {

    public static String formatActeurs(List<String> acteurs) {
        return FormattingTools.beautifulList(acteurs, "Avec ");
    }

    public static String formatDirectors(List<String> directors) {

        // TODO: not great
        String pretext = "";
        if (!directors.isEmpty() && directors.get(0).length() > 0) {

            char[] vowels = {'A', 'E', 'I', 'O', 'U', 'Y', 'H'};
            char firstLetter = directors.get(0).charAt(0);

            if (new String(vowels).indexOf(firstLetter) > -1) {
                pretext = "Film d'";
            } else {
                pretext = "Film de ";
            }
        }

        return FormattingTools.beautifulList(directors, pretext);
    }


    private static String beautifulList(List<String> list, String preText) {

        if (list.isEmpty())
            return "";

        int listSize = list.size();
        StringBuilder builder = new StringBuilder(preText);
        for (int i = 0; i < listSize - 1; i++) {
            builder.append(list.get(i));
            if (i == listSize - 2)
                builder.append(" et ");
            else
                builder.append(", ");
        }
        builder.append(list.get(listSize - 1));

        return builder.toString();
    }


    public static String formatShowingCards(List<Showing> showing) {
        return (showing.isEmpty()) ? "Pas de séances prévues" :
                showing.get(0).toString();
    }

    public static String formatShowingDetails(List<Showing> showing) {

        if (showing.isEmpty())
            return "Pas de séances prévues";

        int jourNb = -1;
        StringBuilder builder = new StringBuilder();

        for (Showing s : showing) {
            if (s.getDayNb() != jourNb) {
                if (jourNb > -1)
                    builder.append("\n");
                builder.append(s.getJourText()).append(" - ");
            }

            builder.append(s.toStringShort()).append("   ");
            jourNb = s.getDayNb();
        }

        return builder.toString();
    }


}
