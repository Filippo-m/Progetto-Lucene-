import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;


public class Indexing {

    public static void IndexDocs() throws IOException {


        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
        Path path = Paths.get("indice");

        Directory directory = FSDirectory.open(path);


        Analyzer title_analyzer = CustomAnalyzer.builder()
                .withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                .build();


        Analyzer content_analyzer = CustomAnalyzer.builder()
                .withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                .build();

        perFieldAnalyzers.put("title", title_analyzer);
        perFieldAnalyzers.put("content", content_analyzer);
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new ItalianAnalyzer(), perFieldAnalyzers);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        Codec codec = new SimpleTextCodec();
        if(codec !=null){
            config.setCodec(codec);
        }

        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll();

        long startTime = System.nanoTime();
        File directory_txt = new File("archivio");
        for( File fileEntry: directory_txt.listFiles()){
             String title_extension = fileEntry.getName();
             int dotIndex = title_extension.lastIndexOf('.');
             String title_book = title_extension.substring(0, dotIndex);
             String content_book = "";
             Scanner reader = new Scanner(fileEntry);
             while(reader.hasNextLine()) {
                 content_book = content_book +" "+ reader.nextLine();
             }
                 Document doc  = new Document();
                 doc.add(new TextField("title",title_book, Field.Store.YES));
                 doc.add(new TextField("content",content_book, Field.Store.YES));
                 writer.addDocument(doc);
             }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Tempo di indicizzazione: " + timeElapsed / 1000000 + "ms");

        writer.commit();
        writer.close();


        }

}





