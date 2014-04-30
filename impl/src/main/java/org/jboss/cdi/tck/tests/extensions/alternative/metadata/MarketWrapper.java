/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.extensions.alternative.metadata;

import org.jboss.cdi.tck.util.annotated.AnnotatedConstructorWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedMethodWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedParameterWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class MarketWrapper extends AnnotatedTypeWrapper<Market> {

    private final Set<Type> typeClosure = new HashSet<Type>();
    private static boolean getBaseTypeOfMarketConstructorParameterUsed = false;
    private static boolean getBaseTypeOfBillProducerParameterUsed = false;

    public MarketWrapper(AnnotatedType<Market> delegate) {
        super(delegate, true);
        typeClosure.add(Market.class);
        typeClosure.add(Object.class);

    }

    @Override
    public Set<Type> getTypeClosure() {
        return typeClosure;
    }

    @Override
    public Set<AnnotatedConstructor<Market>> getConstructors() {
        Set<AnnotatedConstructor<Market>> constructors = new HashSet<AnnotatedConstructor<Market>>();
        for (AnnotatedConstructor<Market> constructor : super.getConstructors()) {
            if (constructor.getParameters().size() == 1) {
                constructors.add(wrapConstructor(constructor, true));
            } else {
                constructors.add(constructor);
            }
        }
        return constructors;
    }

    @Override
    public Set<AnnotatedMethod<? super Market>> getMethods() {
        Set<AnnotatedMethod<? super Market>> methods = new HashSet<AnnotatedMethod<? super Market>>();
        for (AnnotatedMethod<? super Market> method : super.getMethods()) {
            if (method.getJavaMember().getName().equals("createBill")) {
                methods.add(wrapMethodParameter(method, true));
            } else {
                methods.add(method);
            }

        }
        return methods;
    }

    public AnnotatedMethodWrapper<? super Market> wrapMethodParameter(AnnotatedMethod<? super Market> delegate, boolean keepOriginalAnnotations) {

        AnnotatedMethodWrapper<Market> methodWrapper = new AnnotatedMethodWrapper(delegate, this, keepOriginalAnnotations);
        methodWrapper.replaceParameters(new AnnotatedParameterWrapper<Market>(methodWrapper.getParameter(0),methodWrapper, true) {
            @Override
            public Type getBaseType() {
                getBaseTypeOfBillProducerParameterUsed = true;
                return TropicalFruit.class;
            }
        });

        return methodWrapper;
    }

    private AnnotatedConstructor<Market> wrapConstructor
            (AnnotatedConstructor<Market> delegate, final boolean keepOriginalAnnotations, final Annotation... annotations) {
        AnnotatedConstructorWrapper<Market> constructor = new AnnotatedConstructorWrapper(delegate, this, keepOriginalAnnotations, annotations);
        constructor.replaceParameters(new AnnotatedParameterWrapper(constructor.getParameter(0),constructor,  keepOriginalAnnotations, annotations) {
            @Override
            public Type getBaseType() {
                getBaseTypeOfMarketConstructorParameterUsed = true;
                return TropicalFruit.class;
            }

        });
        return constructor;
    }

    public static boolean isGetBaseTypeOfMarketConstructorParameterUsed() {
        return getBaseTypeOfMarketConstructorParameterUsed;
    }

    public static boolean isGetBaseTypeOfBillProducerParameterUsed() {
        return getBaseTypeOfBillProducerParameterUsed;
    }
}
