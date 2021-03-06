/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 * Copyright (c) 2016, Jessica Beller (jbeller@box.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonpatch.operation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchMessages;
import com.github.fge.jsonpatch.operation.policy.PathMissingPolicy;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Iterables;

import java.io.IOException;

/**
 * RemoveOperationBase implements the basic concept of removing the requested path.
 */
public abstract class RemoveOperationBase extends JsonPatchOperationBase
{
    protected static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonPatchMessages.class);

    @JsonCreator
    public RemoveOperationBase(final String op,
                               @JsonProperty("path") final JsonPointer path,
                               final PathMissingPolicy pathMissingPolicy)
    {
        super(op, path, pathMissingPolicy);
    }

    @Override
    public JsonNode apply(final JsonNode node)
        throws JsonPatchException
    {
        final JsonNode ret = node.deepCopy();
        if (this.getPath().isEmpty())
            return MissingNode.getInstance();
        if (this.getPath().path(node).isMissingNode()) {
            switch (this.getPathMissingPolicy()) {
                case THROW:
                    throw new JsonPatchException(BUNDLE.getMessage(
                        "jsonPatch.noSuchPath"));
                case SKIP:
                    return ret;
            }
        }
        final JsonNode parentNode = this.getPath().parent().get(ret);
        final String raw = Iterables.getLast(this.getPath()).getToken().getRaw();
        if (parentNode.isObject())
            ((ObjectNode) parentNode).remove(raw);
        else
            ((ArrayNode) parentNode).remove(Integer.parseInt(raw));
        return ret;
    }

    @Override
    public void serialize(final JsonGenerator jgen,
        final SerializerProvider provider)
        throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("op", this.getOp());
        jgen.writeStringField("path", this.getPath().toString());
        jgen.writeEndObject();
    }

    @Override
    public void serializeWithType(final JsonGenerator jgen,
        final SerializerProvider provider, final TypeSerializer typeSer)
        throws IOException {
        serialize(jgen, provider);
    }

    @Override
    public String toString()
    {
        return "op: " + this.getOp() + "; path: \"" + this.getPath() + '"';
    }
}
