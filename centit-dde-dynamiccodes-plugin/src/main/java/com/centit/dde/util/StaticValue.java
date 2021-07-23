package com.centit.dde.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StaticValue {
	private static final Logger LOG = LoggerFactory.getLogger(StaticValue.class);
	public static final String PREFIX = "dynamic_";
	public static final String HOME = getValueOrCreate("user.dir", new File(System.getProperty("user.dir"), "centit-dde-dynamiccodes-plugin"+File.separator+"temp").getAbsolutePath());


	public static String getValueOrCreate(String key, String def) {
		String value = System.getProperty(PREFIX + key);
		if (LOG.isDebugEnabled()) {
			LOG.debug("get property " + key + ":" + value);
		}
		if (value == null) {
			if (def != null) {
				System.setProperty(PREFIX + key, def);
			}
			return def;
		} else {
			return value;
		}
	}

    //pom.xml文件内容
    public static String mavenDeps(String content) {
        return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
            + "	<modelVersion>4.0.0</modelVersion>\n" + "	<groupId>org.nlpcn</groupId>\n" + "	<artifactId>jcoder</artifactId>\n" + "	<version>0.1</version>\n"
            + "	\n" + "	<dependencies>\n" + content + "	</dependencies>\n" + "\n" + "	<build>\n" + "		<sourceDirectory>src/main/java</sourceDirectory>\n"
            + "		<testSourceDirectory>src/test/java</testSourceDirectory>\n" + "		\n" + "		<plugins>\n" + "			<plugin>\n"
            + "				<artifactId>maven-compiler-plugin</artifactId>\n" + "				<version>3.3</version>\n" + "				<configuration>\n"
            + "					<source>1.8</source>\n" + "					<target>1.8</target>\n" + "					<encoding>UTF-8</encoding>\n"
            + "					<compilerArguments>\n" + "						<extdirs>lib</extdirs>\n" + "					</compilerArguments>\n"
            + "				</configuration>\n" + "			</plugin>\n" + "		</plugins>\n" + "	</build>\n" + "</project>\n" + "";
    }

    /**
     * 创建目录并生产对应文件
     * @param JcoderHome
     * @param logPath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void makeFiles(String JcoderHome, String... logPath) throws FileNotFoundException, IOException {
        File libDir = new File(JcoderHome, "lib"); // create jar dir
        if (!libDir.exists()) {
            libDir.mkdirs();
            wirteFile(new File(libDir, "pom.xml").getAbsolutePath(), "utf-8", mavenDeps(""));
        }
    }

    public static void wirteFile(String filePath, String encoding, String content) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(content.getBytes(encoding));
            fos.flush();
        }
    }
}
