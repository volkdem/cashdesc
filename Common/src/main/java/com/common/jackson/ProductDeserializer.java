package com.common.jackson;

import com.common.model.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import java.io.IOException;

/**
 * Created by Vadim on 11.06.2016.
 */
public class ProductDeserializer extends SimpleModule {


    public ProductDeserializer() {

        addKeySerializer(Product.class, new ProductAsKeyJsonKeySerializer());

        addKeyDeserializer(
                Product.class,
                new ProductAsKeyJsonKeyDeserializer());
    }



    class ProductAsKeyJsonKeyDeserializer extends KeyDeserializer {

        public Object deserializeKey(String s, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();

            System.out.println("Key name is s: " + s);

            //System.out.println("Key name is s: " + s.substring(7));

            return mapper.readValue(s, Product.class);
        }


    }


    class ProductAsKeyJsonKeySerializer extends JsonSerializer<Product> {

        @Override
        public void serialize(Product value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                ObjectMapper mapper = new ObjectMapper();
                jgen.writeFieldName(mapper.writeValueAsString(value));
        }
    }



}
