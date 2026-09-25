import java.util.*;

public class PositionalIndex {

    private final Map<String, Map<Integer, List<Integer>>> index;

    public PositionalIndex() {
        index = new HashMap<>();
    }

    public void addDocument(int docId, String[] tokens) {

        for (int position = 0; position < tokens.length; position++) {

            String term = tokens[position];

            if (term.isBlank()) {
                continue;
            }

            index
                .computeIfAbsent(term, k -> new HashMap<>())
                .computeIfAbsent(docId, k -> new ArrayList<>())
                .add(position + 1);
        }
    }

    public void printIndex() {

        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {

            String term = entry.getKey();

            System.out.println(term);

            for (Map.Entry<Integer, List<Integer>> docEntry
                    : entry.getValue().entrySet()) {

                System.out.println(
                    "    DOC" + docEntry.getKey()
                    + " → "
                    + docEntry.getValue()
                );
            }
        }
    }


    public void search(String term) {

    Map<Integer, List<Integer>> result = index.get(term);

    if (result == null) {
        System.out.println("No documents found for: " + term);
        return;
    }

    System.out.println("Documents containing \"" + term + "\":");

    for (Map.Entry<Integer, List<Integer>> entry : result.entrySet()) {

        System.out.println(
            "DOC" + entry.getKey()
            + " → positions " + entry.getValue()
        );
    }
}

public void andSearch(String term1, String term2) {

    Map<Integer, List<Integer>> result1 = index.get(term1);
    Map<Integer, List<Integer>> result2 = index.get(term2);

    if (result1 == null || result2 == null) {
        System.out.println("No documents found.");
        return;
    }

    System.out.println("Documents containing both \"" 
            + term1 + "\" AND \"" + term2 + "\":");

    for (Integer docId : result1.keySet()) {

        if (result2.containsKey(docId)) {

            System.out.println(
                "DOC" + docId
                + " → " + term1 + " " + result1.get(docId)
                + ", " + term2 + " " + result2.get(docId)
            );
        }
    }
}


public void orSearch(String term1, String term2) {

    Map<Integer, List<Integer>> result1 = index.get(term1);
    Map<Integer, List<Integer>> result2 = index.get(term2);

    System.out.println("Documents containing \"" 
            + term1 + "\" OR \"" + term2 + "\":");

    Set<Integer> documents = new HashSet<>();

    if (result1 != null) {
        documents.addAll(result1.keySet());
    }

    if (result2 != null) {
        documents.addAll(result2.keySet());
    }

    for (Integer docId : documents) {

        System.out.println("DOC" + docId);
    }
}

public void phraseSearch(String phrase) {

    String[] phraseTokens = Tokenizer.tokenize(phrase);

    if (phraseTokens.length == 0) {
        return;
    }

    System.out.println(
        "Documents containing phrase: \"" + phrase + "\""
    );

    // Get documents containing the first word
    Map<Integer, List<Integer>> firstWord =
            index.get(phraseTokens[0]);

    if (firstWord == null) {
        System.out.println("No documents found.");
        return;
    }

    for (Integer docId : firstWord.keySet()) {

        List<Integer> positions = firstWord.get(docId);

        for (Integer startPosition : positions) {

            boolean match = true;

            // Check remaining words
            for (int i = 1; i < phraseTokens.length; i++) {

                Map<Integer, List<Integer>> wordIndex =
                        index.get(phraseTokens[i]);

                if (wordIndex == null ||
                    !wordIndex.containsKey(docId) ||
                    !wordIndex.get(docId).contains(startPosition + i)) {

                    match = false;
                    break;
                }
            }

            if (match) {

                System.out.println(
                    "DOC" + docId
                    + " → starting position "
                    + startPosition
                );

                break;
            }
        }
    }
}

public void nearSearch(String term1, String term2, int distance) {

    Map<Integer, List<Integer>> result1 = index.get(term1);
    Map<Integer, List<Integer>> result2 = index.get(term2);

    if (result1 == null || result2 == null) {
        System.out.println("No documents found.");
        return;
    }

    System.out.println(
        "Documents where \"" + term1
        + "\" and \"" + term2
        + "\" are within "
        + distance + " positions:"
    );

    for (Integer docId : result1.keySet()) {

        if (!result2.containsKey(docId)) {
            continue;
        }

        List<Integer> positions1 = result1.get(docId);
        List<Integer> positions2 = result2.get(docId);

        boolean found = false;

        for (Integer pos1 : positions1) {

            for (Integer pos2 : positions2) {

                if (Math.abs(pos1 - pos2) <= distance) {

                    System.out.println(
                        "DOC" + docId
                        + " → "
                        + term1 + " at " + pos1
                        + ", "
                        + term2 + " at " + pos2
                    );

                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            }
        }
    }
}



}