import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Searching {

    public static void Search() throws IOException {

        Path path = Paths.get("indice");
        Directory directory = FSDirectory.open(path);

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query;


        System.out.print("Ricerca per titolo o contenuto? ");
        Scanner in = new Scanner(System.in);
        String search_type = in.nextLine();

        while( !((search_type.equals("titolo") || search_type.equals("contenuto")))){
            System.out.print("Ricerca per titolo o contenuto? ");
            search_type = in.nextLine();
            if(search_type.equals("q")){
                break;
            }
        }

        if(search_type.equals("titolo")) {
            try {
                query=title_query();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        else{

             query = content_query();
            }

        try {

            runQuery(searcher,query);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

        private static Query title_query() throws ParseException {
            Query query;
            System.out.print("Cerca titolo: ");
            Scanner in = new Scanner(System.in);
            String title = in.nextLine().toLowerCase();
            if(!title.contains("\"")){
                QueryParser queryParser = new QueryParser("title", new
                        WhitespaceAnalyzer());
                       query = queryParser.parse(title);
            }
            else{
                PhraseQuery.Builder builder = new PhraseQuery.Builder();
                String true_title = title.substring(1,title.length()-1);
                String[] tokens = true_title.split(" ");
                for(String s: tokens){
                    builder.add(new Term("title", s));
                }
                PhraseQuery pq = builder.build();
                query = pq;
            }
            return query;
        }

        private static Query content_query(){
            Query query;
            Scanner in = new Scanner(System.in);
            System.out.print("Cerca contenuto: ");
            String content = in.nextLine().toLowerCase();
            if(!content.contains("\"")){
                QueryParser queryParser = new QueryParser("content", new
                        WhitespaceAnalyzer());
                try {
                    query = queryParser.parse(content);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                PhraseQuery.Builder builder = new PhraseQuery.Builder();
                String true_content = content.substring(1,content.length()-1);
                String[] tokens = true_content.split(" ");
                for(String s: tokens){
                    builder.add(new Term("content", s));
                }
                PhraseQuery pq = builder.build();
                query = pq;
            }
            return query;
    }


        public static void runQuery(IndexSearcher searcher, Query query) throws IOException {
            TopDocs hits = searcher.search(query, 10);
            if (hits.scoreDocs.length > 0) {
                for (int i = 0; i < hits.scoreDocs.length; i++) {
                    ScoreDoc scoreDoc = hits.scoreDocs[i];
                    Document doc = searcher.doc(scoreDoc.doc);
                    System.out.println("doc" + scoreDoc.doc + ":" + doc.get("title") + " (" + scoreDoc.score + ")");
                }
            } else {
                System.out.println("Document not found");
            }

        }


    }




