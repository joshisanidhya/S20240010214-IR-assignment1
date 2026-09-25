import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
long startTime = System.currentTimeMillis();
      String filePath = "../data/hindi_10k.txt";

        PositionalIndex index = new PositionalIndex();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath),
                        StandardCharsets.UTF_8))) {

            String line;
            int docId = 1;

           while ((line = reader.readLine()) != null) {

    String[] tokens = Tokenizer.tokenize(line);

    index.addDocument(docId, tokens);

    docId++;
}

System.out.println("Documents indexed: " + (docId - 1));

long endTime = System.currentTimeMillis();

System.out.println(
    "Indexing time: "
    + (endTime - startTime)
    + " ms"
);

// index.printIndex();

            System.out.println("\n===== SEARCH =====");

index.search("भारत");
System.out.println("\n===== AND SEARCH =====");

index.andSearch("भारत", "हिंदी");


System.out.println("\n===== OR SEARCH =====");

index.orSearch("भारत", "हिंदी");

System.out.println("\n===== PHRASE SEARCH =====");

index.phraseSearch("भारत की राजधानी");

System.out.println("\n===== NEAR SEARCH =====");

index.nearSearch("भारत", "दिल्ली", 3);


        } catch (IOException e) {

            System.out.println(
                "Error reading file: " + e.getMessage()
            );
        }
    }
}