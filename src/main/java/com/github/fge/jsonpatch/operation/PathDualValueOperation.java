package com.github.fge.jsonpatch.operation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatchMessages;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.io.IOException;

/**
 * Base class for JSON Patch operations taking one JSON Pointer and two values as arguments
 */
public abstract class PathDualValueOperation
    implements JsonPatchOperation
{
    protected static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonPatchMessages.class);

    protected final String op;

    @JsonSerialize(using = ToStringSerializer.class)
    protected final JsonPointer path;

    @JsonSerialize
    protected final JsonNode fromValue;

    @JsonSerialize
    protected final JsonNode toValue;

    /**
     * Protected constructor
     *
     * @param op operation name
     * @param path source path
     * @param fromValue original path
     * @param toValue new value
     */
    protected PathDualValueOperation(final String op, final JsonPointer path,
        final JsonNode fromValue, final JsonNode toValue)
    {
        this.op = op;
        this.path = path;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public String getOp() {
        return this.op;
    }

    public JsonPointer getPath() {
        return this.path;
    }

    @Override
    public final void serialize(final JsonGenerator jgen,
        final SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        jgen.writeStartObject();
        jgen.writeStringField("op", op);
        jgen.writeStringField("path", path.toString());
        jgen.writeFieldName("from");
        jgen.writeTree(fromValue);
        jgen.writeFieldName("value");
        jgen.writeTree(toValue);
        jgen.writeEndObject();
    }

    @Override
    public final void serializeWithType(final JsonGenerator jgen,
        final SerializerProvider provider, final TypeSerializer typeSer)
        throws IOException, JsonProcessingException
    {
        serialize(jgen, provider);
    }

    @Override
    public final String toString()
    {
        return "op: " + op + "; path: \"" + path + "\"; from: " + fromValue + "; value: " + toValue;
    }
}
