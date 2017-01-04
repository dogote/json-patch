package com.github.fge.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jsonpatch.operation.*;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.io.IOException;

/**
 * StandardJsonPatchfactory can create a JsonPatchFactory configured to work with the RFC6902 set of JSON Patch operations.
 */
public class StandardJsonPatchFactory
{
    public static JsonPatchFactory create()
    {
        ObjectMapper mapper = JacksonUtils.newMapper();
        mapper.registerModule(new JsonPatchModule());
        return new JsonPatchFactory(mapper);
    }
}
