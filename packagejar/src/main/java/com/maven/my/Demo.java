package com.maven.my;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;

// <root>
//     <test>
//         <item>resources</item>
//     </test>
// </root>
public class Demo {
    public void readXml(String xml) {
        SAXReader reader = new SAXReader();
        File f = new File(xml);
        System.out.println(f.getPath() + " abs path " +f.getAbsolutePath());
        try {
            Document doc = reader.read(f);
            Node ndItem       = doc.selectSingleNode("/root/test/item");
            if (ndItem != null) {
                System.out.println("key=" + ndItem.getName() + ", value=" + ndItem.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello");
        Demo dm = new Demo();
        dm.readXml("resources.xml");
    }
}
