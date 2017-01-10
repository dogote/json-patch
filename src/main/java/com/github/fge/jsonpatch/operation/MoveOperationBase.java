package com.github.fge.jsonpatch.operation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.operation.policy.PathMissingPolicy;

/**
 MoveOperationBase implements the basic concept of moving the value from
 one path to a given destination path.
 */
public abstract class MoveOperationBase
    extends DualPathOperation
{
    private final PathMissingPolicy pathMissingPolicy;

    @JsonCreator
    public MoveOperationBase(final String op,
                             @JsonProperty("from") final JsonPointer from,
                             @JsonProperty("path") final JsonPointer path,
                             final PathMissingPolicy pathMissingPolicy)
    {
        super(op, from, path);
        this.pathMissingPolicy = pathMissingPolicy;
    }

    @Override
    public JsonNode apply(final JsonNode node)
        throws JsonPatchException
    {
        final JsonNode ret = node.deepCopy();
        if (from.equals(getPath()))
            return ret;
        final JsonNode movedNode = from.path(node);
        if (movedNode.isMissingNode()) {
            switch (pathMissingPolicy) {
                case THROW:
                    throw new JsonPatchException(BUNDLE.getMessage(
                        "jsonPatch.noSuchPath"));
                case SKIP:
                    return ret;
            }
        }
        final JsonPatchOperation remove = new RemoveOperation(from);
        final JsonPatchOperation add = new AddOperation(getPath(), movedNode);
        return add.apply(remove.apply(node));
    }
}
