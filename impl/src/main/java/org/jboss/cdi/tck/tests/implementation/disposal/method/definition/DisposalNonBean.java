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
package org.jboss.cdi.tck.tests.implementation.disposal.method.definition;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;

@Dependent
public class DisposalNonBean {
    private static boolean tarantulaDestroyed = false;
    private static boolean webSpiderdestroyed = false;

    public DisposalNonBean(String someString) {

    }

    public void destroyDeadliestTarantula(@Disposes @Deadliest Tarantula spider) {
        tarantulaDestroyed = true;
    }

    public static boolean isTarantulaDestroyed() {
        return tarantulaDestroyed;
    }

    /*public static void setSpiderDestroyed(boolean spiderDestroyed) {
        DisposalNonBean.spiderDestroyed = spiderDestroyed;
    } */


    public static boolean isWebSpiderdestroyed() {
        return webSpiderdestroyed;
    }

    public static void setWebSpiderdestroyed(boolean webSpiderdestroyed) {
        DisposalNonBean.webSpiderdestroyed = webSpiderdestroyed;
    }

}
