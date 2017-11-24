package com.lanou.lucene;
import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by dllo on 17/11/24.
 */
public class Index {

    //使用Lucene做文件索引
    //索引的写入
    public  void index(){
        IndexWriter indexWriter =null;
        //创建索引的目录对象
        try {
            FSDirectory directory =
                    FSDirectory.open(FileSystems.getDefault().getPath("/Users/dllo/Documents/Lucene/index"));
        //创建分词器
//            Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new HanLPAnalyzer();
        //创建写入索对象的配置
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //创建写入索引的对象
        //需要传入索引的保存路径和分词器配置
            indexWriter = new IndexWriter(directory,indexWriterConfig);
        //写入之前请清除之前使用的所有索引
        //**实际使用删除这句话
            indexWriter.deleteAll();

        //读取要进行索引的文件
            File datafile = new File("/Users/dllo/Documents/Lucene/data");
        //获取目录下的所有txt文件
            File[] txtFile = datafile.listFiles();
            for (File file : txtFile) {
        //将每个文件写入到索引中
                Document document = new Document();
        //Filed构造函数的三个参数
        //name:自定义的key值,方便收缩时确认范围
        //value:实际需要写入索引的内容
        //type:是否需要持久化
                document.add(new Field("content", FileUtils.readFileToString(file,"UTF-8"), TextField.TYPE_STORED));
                document.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
        //写入
                indexWriter.addDocument(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (indexWriter!=null){
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
