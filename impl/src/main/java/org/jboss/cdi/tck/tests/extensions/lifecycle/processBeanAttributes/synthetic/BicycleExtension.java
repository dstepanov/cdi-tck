/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.synthetic;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;

public class BicycleExtension implements Extension {

    private boolean vetoed;
    private BeanAttributes<Bicycle> bicycleAttributesBeforeRegistering = null;
    private BeanAttributes<Bicycle> bicycleAttributesBeforeModifying = null;

    public void vetoBicycleClass(@Observes ProcessAnnotatedType<Bicycle> event) {
        event.veto();
        vetoed = true;
    }

    public void registerBicycle(@Observes AfterBeanDiscovery event, BeanManager manager) {
        bicycleAttributesBeforeRegistering = manager.createBeanAttributes(manager.createAnnotatedType(Bicycle.class));
        event.addBean(new BicycleBean(bicycleAttributesBeforeRegistering));
    }

    public void modifyBicycle(@Observes ProcessBeanAttributes<Bicycle> event) {
        // This should be never called - PBA is not fired for synthetic beans
        bicycleAttributesBeforeModifying = event.getBeanAttributes();
    }

    public boolean isVetoed() {
        return vetoed;
    }

    public BeanAttributes<Bicycle> getBicycleAttributesBeforeRegistering() {
        return bicycleAttributesBeforeRegistering;
    }

    public BeanAttributes<Bicycle> getBicycleAttributesBeforeModifying() {
        return bicycleAttributesBeforeModifying;
    }

}