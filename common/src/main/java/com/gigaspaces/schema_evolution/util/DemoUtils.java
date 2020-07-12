package com.gigaspaces.schema_evolution.util;

import com.gigaspaces.datasource.SpaceDataSourceLoadRequest;
import com.gigaspaces.datasource.SpaceTypeSchemaAdapter;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.metadata.index.SpaceIndexType;
import com.gigaspaces.persistency.MongoSpaceDataSourceFactory;
import com.gigaspaces.schema_evolution.adapters.PersonSchemaAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class DemoUtils {
    public final static String PERSON_DOCUMENT = "PersonDocument";
    public final static int BATCH_SIZE = 100;
    private final static Random random = new Random();

    public static SpaceTypeDescriptor getPersonTypeDescriptor(){
        return new SpaceTypeDescriptorBuilder(PERSON_DOCUMENT)
                .idProperty("userId", false)
                .routingProperty("routing")
                .addPropertyIndex("created", SpaceIndexType.EQUAL)
                .create();
    }

    public static SpaceTypeDescriptor getOrderTypeDescriptor(){
        return null;
    }

    public static SpaceDocument createPersonDocument(){
        SpaceDocument result = new SpaceDocument().setTypeName(PERSON_DOCUMENT);
        int id = Math.abs(random.nextInt());
        result.setProperty("userId", id);
        result.setProperty("routing", id);
        result.setProperty("created", new Date(System.currentTimeMillis()));
        result.setProperty("removedField", createRandomString(10));
        result.setProperty("typeChangeField", createRandomString(10));

        return result;
    }

    public static SpaceDocument createOrderDocument(){
        return null;
    }

    public static SpaceDataSourceLoadRequest createDataLoadRequest(){
        MongoSpaceDataSourceFactory mongoSpaceDataSourceFactory = new MongoSpaceDataSourceFactory().setHost("127.0.1.1").setPort(27017).setDb("v1-db");
        Collection<SpaceTypeSchemaAdapter> adapters = Collections.singleton(new PersonSchemaAdapter());
        return new SpaceDataSourceLoadRequest(mongoSpaceDataSourceFactory, adapters);
    }

    public static String createRandomString(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
