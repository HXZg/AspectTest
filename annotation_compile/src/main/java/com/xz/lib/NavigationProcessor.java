package com.xz.lib;

import com.google.auto.service.AutoService;
import com.google.common.base.Charsets;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
public class NavigationProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;

    private static final String OUTPUT_FILE_NAME = "destination.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(FragmentDestination.class.getCanonicalName());
        types.add(ActivityDestination.class.getCanonicalName());
        types.add(FixFragmentDestination.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> fragments = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityS = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);
        Set<? extends Element> fixFragments = roundEnvironment.getElementsAnnotatedWith(FixFragmentDestination.class);

        Map<String, List<Destination>> map = new HashMap<>();
        handlerElement(fragments,FragmentDestination.class,map);
        handlerElement(activityS,ActivityDestination.class,map);
        handlerElement(fixFragments,FixFragmentDestination.class,map);

        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
            FileObject object = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
            String resourcePath = object.toUri().getPath();
            messager.printMessage(Diagnostic.Kind.NOTE,"resourcePath:::" + resourcePath);

            String app = resourcePath.substring(0, resourcePath.indexOf("build"));
            messager.printMessage(Diagnostic.Kind.NOTE,app);
            String assetsPath = app + "src/main/assets/";

            File file = new File(assetsPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File outPutFile = new File(file,OUTPUT_FILE_NAME);
            if (outPutFile.exists()) {
                outPutFile.delete();
            }
            outPutFile.createNewFile();

            String content = new Gson().toJson(map);
            fos = new FileOutputStream(outPutFile);
            writer = new OutputStreamWriter(fos, Charsets.UTF_8);
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) fos.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void handlerElement(Set<? extends Element> elements, Class<? extends Annotation> clazz,Map<String, List<Destination>> map) {
        for (Element element: elements) {
            // 所有注解  只注解在 类上  ，可以直接强转为 TypeElement
            TypeElement typeElement = (TypeElement) element;
            String name = typeElement.getQualifiedName().toString();

            int type = 0;
            boolean isStart = false;
            String intercept = "";
            String control = "";
            int id = 0;
            String pageUrl = "";

            Annotation annotation = typeElement.getAnnotation(clazz);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination destination = (FragmentDestination) annotation;
                type = 0;
                isStart = destination.isStart();
                intercept = destination.intercept();
                control = destination.control();
                pageUrl = destination.pageUrl();
                id = destination.id();
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination destination = (ActivityDestination) annotation;
                type = 1;
                isStart = destination.isStart();
                intercept = destination.intercept();
                control = destination.control();
                pageUrl = destination.pageUrl();
                id = destination.id();
            } else if (annotation instanceof FixFragmentDestination) {
                FixFragmentDestination destination = (FixFragmentDestination) annotation;
                type = 2;
                isStart = destination.isStart();
                intercept = destination.intercept();
                control = destination.control();
                pageUrl = destination.pageUrl();
                id = destination.id();
            } else {
                // dialog  导航器
            }

            Destination destination = new Destination(name,intercept,pageUrl,control,isStart,id,type);

            List<Destination> destinations = map.get(control);
            if (destinations == null) {
                destinations = new ArrayList<>();
                destinations.add(destination);
                map.put(control,destinations);
            } else {
                if (!destinations.contains(destination)) {
                    destinations.add(destination);
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR,"clazzName:" + name + " id::" + id);
                }
            }
        }
    }
}
