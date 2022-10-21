import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {

    @Test
     public void SearchAll() throws IOException {
        Path path = Paths.get("indice");
        Indexing.IndexDocs();

        Query query = new MatchAllDocsQuery();

        try (Directory directory = FSDirectory.open(path)) {

            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                TopDocs hits = searcher.search(query,20 );
                if (hits.scoreDocs.length > 0) {
                    for (int i = 0; i < hits.scoreDocs.length; i++) {
                        ScoreDoc scoreDoc = hits.scoreDocs[i];
                        Document doc = searcher.doc(scoreDoc.doc);
                        System.out.println("doc" + scoreDoc.doc + ":" + doc.get("title") + " (" + scoreDoc.score + ")");
                    }
                } else {
                    System.out.println("Document not found");
                }
            } finally {
                try {
                    directory.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Test
    public void ContentSearchTQ() throws Exception {
        Path path = Paths.get("indice");

        Query query = new TermQuery(new Term("content", "inferno"));

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }


    @Test
    public void ContentSearchPQ() throws Exception {
        Path path = Paths.get("indice");

        PhraseQuery query = new PhraseQuery.Builder()
                .add(new Term("content", "autore"))
                .add(new Term("content", "orwell"))
                .build();

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }

    @Test
    public void ContentSearchPQ_bis() throws Exception {
        Path path = Paths.get("indice");

        PhraseQuery query = new PhraseQuery.Builder()
                .add(new Term("content", "entrata"))
                .add(new Term("content", "del"))
                .add(new Term("content", "bosco"))
                .add(new Term("content", "atro"))
                .build();

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }

    @Test
    public void ContentSearchBQ() throws Exception {
        Path path = Paths.get("indice");

        PhraseQuery phraseQuery = new PhraseQuery.Builder()
                .add(new Term("content", "Francis"))
                .add(new Term("content", "Scott"))
                .build();

        TermQuery termQuery = new TermQuery(new Term("title", "gatsby"));

        BooleanQuery query = new BooleanQuery.Builder()
                .add(new BooleanClause(termQuery, BooleanClause.Occur.SHOULD))
                .add(new BooleanClause(phraseQuery, BooleanClause.Occur.SHOULD))
                .build();

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }


    @Test
    public void ContentSearchQP() throws Exception {
        Path path = Paths.get("indice");

        QueryParser parser = new QueryParser("content", new WhitespaceAnalyzer());
        Query query = parser.parse("assassino  monastero");

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }



    @Test
    public void TitleSearchTQ() throws Exception {
        Path path = Paths.get("indice");

        Query query = new TermQuery(new Term("title", "sposi"));

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }


    @Test
    public void TitleSearchPQ() throws Exception {
        Path path = Paths.get("indice");

        PhraseQuery query = new PhraseQuery.Builder()
                .add(new Term("title", "delitto"))
                .add(new Term("title", "e"))
                .add(new Term("title", "castigo"))
                .build();

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }


    @Test
    public void TitleSearchPQ_bis() throws Exception {
        Path path = Paths.get("indice");

        PhraseQuery query = new PhraseQuery.Builder()
                .add(new Term("title", "cime"))
                .add(new Term("title", "tempestose"))
                .build();

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }


    @Test
  public void TitleSearchQP() throws Exception {
        Path path = Paths.get("indice");

        QueryParser parser = new QueryParser("title", new WhitespaceAnalyzer());
        Query query = parser.parse("vecchio mare");

        try (Directory directory = FSDirectory.open(path)) {
            Indexing.IndexDocs();
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                Searching.runQuery(searcher, query);
            } finally {
                directory.close();
            }

        }
    }

}
