package com.centit.dde.agent;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;
import static com.sun.javafx.fxml.expression.Expression.add;
import static org.springframework.boot.WebApplicationType.NONE;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

@Test
public  void test() {
    Map<String, Object> param = new HashMap<>();
    param.put("hi", "112" );
    param.put("ee",null);
    String text = JSONObject.toJSONString(param,WriteMapNullValue);
    System.out.println(text);
    }
}

