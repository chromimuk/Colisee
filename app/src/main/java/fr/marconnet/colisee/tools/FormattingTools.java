package fr.marconnet.colisee.tools;

import android.util.Log;

import java.util.List;

import fr.marconnet.acp.models.Seance;


public class FormattingTools {

    public static String formatActeurs(List<String> acteurs) {
        return FormattingTools.beautifulList(acteurs, "Avec ");
    }

    public static String formatRealisateurs(List<String> realisateurs) {

        // TODO: not great
        String pretext = "";
        if (!realisateurs.isEmpty() && realisateurs.get(0).length() > 0) {

            char[] voyelles = {'A', 'E', 'I', 'O', 'U', 'Y', 'H'};
            char firstLetter = realisateurs.get(0).charAt(0);

            if (new String(voyelles).indexOf(firstLetter) > -1) {
                pretext = "Film d'";
            } else {
                pretext = "Film de ";
            }
        }

        return FormattingTools.beautifulList(realisateurs, pretext);
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


    public static String formatSeancesCards(List<Seance> seances) {
        return (seances.isEmpty()) ? "Pas de séances prévues" :
                seances.get(0).toString();
    }

    public static String formatSeancesDetail(List<Seance> seances) {

        if (seances.isEmpty())
            return "Pas de séances prévues";

        int jourNb = -1;
        StringBuilder builder = new StringBuilder();

        for (Seance s : seances) {
            if (s.getJourNb() != jourNb) {
                if (jourNb > -1)
                    builder.append("\n");
                builder.append(s.getJourText()).append(" - ");
            }

            builder.append(s.toStringShort()).append("   ");
            jourNb = s.getJourNb();
        }

        return builder.toString();
    }


}
