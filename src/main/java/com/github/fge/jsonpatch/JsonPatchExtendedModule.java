package com.github.fge.jsonpatch;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.fge.jsonpatch.operation.AddOperation;
import com.github.fge.jsonpatch.operation.CopyOperation;
import com.github.fge.jsonpatch.operation.MoveOperation;
import com.github.fge.jsonpatch.operation.OmitOperation;
import com.github.fge.jsonpatch.operation.OmitOptionalOperation;
import com.github.fge.jsonpatch.operation.RemoveOperation;
import com.github.fge.jsonpatch.operation.RemoveOptionalOperation;
import com.github.fge.jsonpatch.operation.ReplaceOperation;
import com.github.fge.jsonpatch.operation.TestOperation;
import com.github.fge.jsonpatch.operation.TranslateOperation;
import com.github.fge.jsonpatch.operation.TranslateOptionalOperation;

/**
 * This module registers the standard JSON-PATCH operations with Jackson.
 */
public class JsonPatchExtendedModule extends SimpleModule {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public JsonPatchExtendedModule() {
        registerSubtypes(
                new NamedType(AddOperation.class, AddOperation.OPERATION_NAME),
                new NamedType(CopyOperation.class, CopyOperation.OPERATION_NAME),
                new NamedType(MoveOperation.class, MoveOperation.OPERATION_NAME),
                new NamedType(RemoveOperation.class, RemoveOperation.OPERATION_NAME),
                new NamedType(RemoveOptionalOperation.class, RemoveOptionalOperation.OPERATION_NAME),
                new NamedType(ReplaceOperation.class, ReplaceOperation.OPERATION_NAME),
                new NamedType(TestOperation.class, TestOperation.OPERATION_NAME),
                new NamedType(OmitOperation.class, OmitOperation.OPERATION_NAME),
                new NamedType(OmitOptionalOperation.class, OmitOptionalOperation.OPERATION_NAME),
                new NamedType(TranslateOperation.class, TranslateOperation.OPERATION_NAME),
                new NamedType(TranslateOptionalOperation.class, TranslateOptionalOperation.OPERATION_NAME)
        );
    }
}
