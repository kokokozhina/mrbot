package gitlab.client.impl;

import org.gitlab.api.models.GitlabCommit;

import java.util.List;

import static java.lang.Math.max;

public class Analytics {

    public static String checkSquash(List<GitlabCommit> commits) {

        if (commits.size() > 6) {
            return "SQUASH NEEDED : " + commits.size() + " commits";
        }

        return "";
    }

    private static double checkLCS(String s, String t) { // longest common subsequence

        int n = s.length();
        int m = t.length();

        int[][] lcs = new int[2][m + 1];

        for (int i = 0; i <= m; i++) {
            lcs[0][i] = 0;
            lcs[1][i] = 0;
        }

        for (int i = 1; i <= n; i++) {
            lcs[1][0] = 0;
            for (int j = 1; j <= m; j++) {
                lcs[0][j] = lcs[1][j];
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    lcs[1][j] = lcs[0][j - 1] + 1;
                } else {
                    if (lcs[0][j] >= lcs[1][j - 1]) {
                        lcs[1][j] = lcs[0][j];
                    } else {
                        lcs[1][j] = lcs[1][j - 1];
                    }
                }
            }
        }

        return lcs[1][m] / (double) max(n, m);
    }

    public static String checkCommitDescriptionSimilarity(List<GitlabCommit> commits) {

        String result = "";
        for (int i = 0; i < commits.size(); i++) {
            for (int j = i + 1; j < commits.size(); j++) {

                String s = /*commits.get(i).getTitle() + " " + */ commits.get(i).getMessage();
                String t = /*commits.get(j).getTitle() + " " + */ commits.get(j).getMessage();

//                result += s + " | " + t + " = " + checkLCS(s, t) + "\n\n";
                if (checkLCS(s, t) > 0.8) {
                    return "INAPPROPRIATE MR due to similar commit descriptions";
                }
            }
        }

        return result;
    }

}
